package com.Gbserver.mail;

import com.Gbserver.Utilities;
import com.Gbserver.variables.Identity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    public String getConfigEntry() {
        return "-={\n" +
                "---HEADER START---\n" +
                "SENDER=" + Identity.serializeIdentity(sender) + "\n" +
                "RECEIVER=" + Identity.serializeIdentity(receiver) + "\n" +
                "TIME=" + sdf.format(timestamp) + "\n" +
                "SUBJECT=" + subject + "\n" +
                "UUID=" + uid.toString() + "\n" +
                "READ=" + read +
                "\n---HEADER END---\n" +
                message +
                "}=-\n";
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
    private static final int NOT_STARTED$HEADER = 0;
    private static final int IN$HEADER = 1;
    private static final int PASSED$HEADER = 2;
    public static Message parseConfigEntry(String entry) throws ParseException {
        String subject = null;
        String message = "";
        Date time = null;
        OfflinePlayer receiver = null;
        OfflinePlayer sender = null;
        UUID uid = null;
        boolean read = false;
        String[] lines = entry.split("\\r?\\n");
        int header = NOT_STARTED$HEADER;
        int content = 0;
        for(String line : lines){
            if(line.contains("---HEADER ")){
                if(line.contains("START---")){
                    //Header start
                    if(header == PASSED$HEADER || header == IN$HEADER) throw new ParseException("Multiple header starts", 1);
                    header = IN$HEADER;
                }else if(line.contains("END---")){
                    //Header end
                    if(header == NOT_STARTED$HEADER || header == PASSED$HEADER) throw new ParseException("Invalid header endpoint", 1);
                    header = PASSED$HEADER;
                }
            }else if(header == IN$HEADER){
                String[] datas = line.split("=");
                switch(datas[0]){
                    case "SENDER":
                        sender = Identity.deserializeIdentity(datas[1]);
                        content++;
                        break;
                    case "RECEIVER":
                        receiver = Identity.deserializeIdentity(datas[1]);
                        content++;
                        break;
                    case "TIME":
                        time = sdf.parse(datas[1]);
                        content++;
                        break;
                    case "SUBJECT":
                        subject = datas[1];
                        content++;
                        break;
                    case "READ":
                        read = Boolean.valueOf(datas[1]);
                        content++;
                        break;
                    case "UUID":
                        uid = UUID.fromString(datas[1]);
                        content++;
                        break;
                    default:
                        throw new ParseException("Unknown header field: " + datas[0], 1);
                }
            }else{
                //This is part of the message.
                message += line + "\n";
            }
        }
        if(header != PASSED$HEADER && content < 6) throw new ParseException("Header not complete", 1);
        Message output = new Message(sender, receiver, time);
        output.setMessage(subject, message);
        output.setUid(uid);
        if(read) output.setRead();
        return output;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss,yyyy.MM.dd");


}
