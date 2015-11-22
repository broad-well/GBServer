package com.Gbserver.commands;


import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.mail.FileParser;
import com.Gbserver.mail.MailMan;
import com.Gbserver.mail.Message;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Mail implements CommandExecutor{
    public static HashMap<Player, Integer> mailWriteStatus = new HashMap<>();
    public static final int NOT_PENDING = 0;
    public static final int PENDING_SUBJECT = 1;
    public static final int PENDING_MESSAGE = 2;
    // ***IMPORTANT: Listener module is in ChatFormatter.
    HelpTable read = new HelpTable("/mail read [new,<msgNumber>]", "To read your messages", "", "mail read");
    HelpTable write = new HelpTable("/mail write <recipient name>", "To send new messages", "", "mail write");
    HelpTable writesend = new HelpTable("/mail send <recipient name>", "To send your message in the buffer", "", "mail send");
    List<Player> lastShowedWasUnreadMsgs = new LinkedList<>();
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            if(args.length == 0){
                sender.sendMessage(ChatColor.GOLD + "Please choose a subcommand:");
                sender.sendMessage(ChatColor.YELLOW + "/mail read");
                sender.sendMessage(ChatColor.YELLOW + "/mail write");
                sender.sendMessage(ChatColor.YELLOW + "/mail send");
                return true;
            }
            if(args.length > 0){
                switch(args[0]){
                    case "read":
                        MailMan man = MailMan.getPersonalAssistant((Player) sender);
                        List<Message> messages;
                        try {
                            messages = FileParser.getInstance().getMessagesOf((Player) sender);
                        } catch (Exception e){
                            e.printStackTrace();
                            return true;
                        }
                        if(args.length == 2) {
                            if(isNumber(args[1])){
                                //specific msgs
                                displayMessage(sender, lastShowedWasUnreadMsgs.contains(sender) ?
                                        man.yourUnreadMessages().get(Integer.parseInt(args[1])) : messages.get(Integer.parseInt(args[1])));
                            }else{
                                //new msgs
                                displayMessages(sender, man.yourUnreadMessages());
                                lastShowedWasUnreadMsgs.add((Player) sender);
                            }
                        }else {
                            //read messages
                            displayMessages(sender, messages);
                            lastShowedWasUnreadMsgs.remove(sender);
                        }
                        break;
                    case "write":
                        mailWriteStatus.put((Player) sender, PENDING_SUBJECT);
                        ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "You may now write the subject of the message.");
                        break;
                    case "send":


                                List<String> msgdata = ChatFormatter.activeBuffer.get(sender);
                                mailWriteStatus.remove((Player) sender);
                                ChatWriter.writeTo(sender, ChatWriterType.POSTMAN,
                                        MailMan.getPersonalAssistant((Player) sender).deliverMessage(Bukkit.getOfflinePlayer(args[1]), msgdata.get(0), msgdata.get(1)) ?
                                                "Succesfully sent the message to " + ChatColor.YELLOW + args[1] : "Failed to send message.");



                        return true;
                    default:
                        sender.sendMessage(ChatColor.GOLD + "Please choose a subcommand:");
                        sender.sendMessage(ChatColor.YELLOW + "/mail read");
                        sender.sendMessage(ChatColor.YELLOW + "/mail write");
                        sender.sendMessage(ChatColor.YELLOW + "/mail send");
                        return true;
                }
            }
        }else{
            //admin cannot send messages
            try {
                displayMessages(sender, FileParser.getInstance().getAllStored());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return true;
    }



    private boolean isNumber(String str){
        try{
            Integer.parseInt(str);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    private void displayMessages(CommandSender to, List<Message> msgs){
        ChatWriter.writeTo(to, ChatWriterType.POSTMAN, "Displaying " + msgs.size() + " messages");
        for(Message msg : msgs){
            String tell = ChatColor.GOLD.toString() + "[" + msgs.indexOf(msg) + "] " +
                    ChatColor.DARK_GRAY + msg.getSubject() + " from " + msg.getSender().getName();
            if(!msg.isRead()) tell += ChatColor.RED.toString() + ChatColor.BOLD + "UNREAD";
            to.sendMessage(tell);
        }
        ChatWriter.writeTo(to, ChatWriterType.POSTMAN, "Use " + ChatColor.YELLOW + "/mail read <message number>" + ChatColor.GRAY + " to read a specific message.");
    }
    private void displayMessage(CommandSender to, Message subject){
        ChatWriter.writeTo(to, ChatWriterType.POSTMAN, "Reading message from " + subject.getSender().getName());
        to.sendMessage("");
        to.sendMessage(ChatColor.BOLD + subject.getSubject());
        to.sendMessage(subject.getMessage());
        ChatWriter.writeTo(to, ChatWriterType.POSTMAN, "Message finished");
    }
}
