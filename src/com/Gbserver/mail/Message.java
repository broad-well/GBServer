package com.Gbserver.mail;

import org.bukkit.OfflinePlayer;


/**
 * Created by michael on 11/21/15.
 */
public class Message {
    private OfflinePlayer sender;
    private OfflinePlayer receiver;
    private String timestamp;
    private String subject;
    private String message;
    public Message(OfflinePlayer sender, OfflinePlayer receiver, String timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }
    public void setMessage(String sbj, String msg){
        this.subject = sbj;
        this.message = msg;
    }

    public String getConfigEntry() {
        /*
        entry{name:xxx;sender:xxx;receiver:xxx.....}
         */
        return "!{\n" +
                "subject:" + subject + "\n" +
                "sender:" + sender.getUniqueId().toString() + "\n" +
                "receiver:" + receiver.getUniqueId().toString() + "\n"+
                "time:" + timestamp + "\n" +
                "message:" + message +
                "\n}";
    }
    public static Message parseConfigEntry(String entry) {
        if(!entry.startsWith("!{")) return null;
        String subject;
        String message;
        String time;
        OfflinePlayer receiver;
        OfflinePlayer sender;
        String[] lines = entry.split("\\r?\\n");
        for(String str : lines){
            if(!str.contains("{") && !str.contains("}")){
                switch(str.split(":")[0]){
                    case "subject":
                        subject = str.split
                }
            }
        }

    }
}
