package com.Gbserver.mail;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MailMan {
    public static final String NAME = "Broadwell Server Mail Subsystem";
    public static final Path datfile = ConfigManager.getPathInsidePluginFolder("mails.dat");
    private static final FileParser fileio = FileParser.getInstance();
    public static MailMan getPersonalAssistant(Player p){
        return new MailMan(p);
    }
    public static void setupMailChecker() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Utilities.getInstance(), new Runnable() {
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()){
                    List<Message> unread = getPersonalAssistant(p).yourUnreadMessages();
                    if(!unread.isEmpty()){
                        ChatWriter.writeTo(p, ChatWriterType.POSTMAN, ChatColor.ITALIC + "You have " +
                                unread.size() + " message" + (unread.size() == 1 ? "" : "s") + ".");
                    }
                }
            }
        }, 0L, 20L*60L*10L);
    }
    //----------
    /* file format:

    -={
    ---HEADER START---
    SENDER=
    RECEIVER=
    TIME=
    SUBJECT=
    ---HEADER END---
    Hello!

    Bye.
    }=-
     */
    private OfflinePlayer owner;
    private MailMan(OfflinePlayer owner) {
        this.owner = owner;
    }
    public boolean deliverMessage(OfflinePlayer to, String subject, String message){
        Message build = new Message(owner, to, new Date());
        build.setMessage(subject, message);
        try {
            FileParser.msgs.add(build);
            FileParser.configUser.unload();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Message> yourUnreadMessages() {
        List<Message> output = new LinkedList<>();
        try {
            for (Message msg : fileio.getMessagesOf(owner)) if(!msg.isRead()) output.add(msg);
            return output;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> yourSentMessages() {
        List<Message> output = new LinkedList<>();
        try{
            for (Message msg : FileParser.msgs) if(msg.getSender().equals(owner)) output.add(msg);
            return output;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
