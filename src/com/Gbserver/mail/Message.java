package com.Gbserver.mail;

import com.Gbserver.variables.Identity;
import org.bukkit.OfflinePlayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by michael on 11/21/15.
 */
public class Message {
    private OfflinePlayer sender;
    private OfflinePlayer receiver;
    private Date timestamp;
    private String subject;
    private String message;
    private boolean read;

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {

        this.uid = uid;
    }

    private UUID uid;
    public Message(OfflinePlayer sender, OfflinePlayer receiver, Date timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        uid = UUID.randomUUID();
    }
    public void setMessage(String sbj, String msg){
        this.subject = sbj;
        this.message = msg;
    }
    public void setRead() {
        read = true;
        try {
            FileParser.getInstance().letMessageRead(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return "Message{From: " + sender.toString() +
                ", To: " + receiver.toString() +
                ", Time: " + sdf.format(timestamp) +
                ", UUID: " + uid.toString() +
                ", Subject: " + subject + "}";
    }

    public HashMap<String, String> getYamlDump() {
        return new HashMap<String, String>(){{
            put("from", Identity.serializeIdentity(sender));
            put("to", Identity.serializeIdentity(receiver));
            put("time", sdf.format(timestamp));
            put("uuid", uid.toString());
            put("subject", subject);
            put("isread", Boolean.toString(read));
            put("message", message);
        }};
    }
    /*
    -={
    ---HEADER START---
    SENDER=
    RECEIVER=
    TIME=
    SUBJECT=
    READ=false/true
    ---HEADER END---
    Hello!

    Bye.
    }=-
     */

    public String getMessage() {
        return message;
    }

    public OfflinePlayer getSender() {
        return sender;
    }

    public OfflinePlayer getReceiver() {
        return receiver;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isRead() {
        return read;
    }

    public static Message parseDump(HashMap<String, String> dump) throws ParseException {
        Message output = new Message(Identity.deserializeIdentity(dump.get("from")),
                Identity.deserializeIdentity(dump.get("to")),
                sdf.parse(dump.get("time")));
        output.setUid(UUID.fromString(dump.get("uuid")));
        output.subject = dump.get("subject");
        output.read = Boolean.parseBoolean(dump.get("isread"));
        output.message = dump.get("message");
        return output;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss,yyyy.MM.dd");


}
