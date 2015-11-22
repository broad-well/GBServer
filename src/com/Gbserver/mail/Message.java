package com.Gbserver.mail;

import com.Gbserver.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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
    public Message(OfflinePlayer sender, OfflinePlayer receiver, Date timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }
    public void setMessage(String sbj, String msg){
        this.subject = sbj;
        this.message = msg;
    }
    public void setRead() {
        read = true;
    }
    public String getConfigEntry() {
        return "-={\n" +
                "---HEADER START---\n" +
                "SENDER=" + sender.getUniqueId() + "\n" +
                "RECEIVER=" + receiver.getUniqueId() + "\n" +
                "TIME=" + sdf.format(timestamp) + "\n" +
                "SUBJECT=" + subject + "\n" +
                "READ=" + read +
                "\n---HEADER END---\n" +
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
                        sender = Bukkit.getOfflinePlayer(UUID.fromString(datas[1]));
                        content++;
                        break;
                    case "RECEIVER":
                        receiver = Bukkit.getOfflinePlayer(UUID.fromString(datas[1]));
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
                    default:
                        throw new ParseException("Unknown header field: " + datas[0], 1);
                }
            }else{
                //This is part of the message.
                message += line + "\n";
            }
        }
        if(header != PASSED$HEADER && content < 5) throw new ParseException("Header not complete", 1);
        Message output = new Message(sender, receiver, time);
        output.setMessage(subject, message);
        if(read) output.setRead();
        return output;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss,yyyy.MM.dd");


}
