package com.Gbserver.commands;


import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.mail.FileParser;
import com.Gbserver.mail.MailMan;
import com.Gbserver.mail.Message;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import com.Gbserver.variables.Sandbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class Mail implements CommandExecutor{
    public static HashMap<Player, Integer> mailWriteStatus = new HashMap<>();
    public static final int NOT_PENDING = 0;
    public static final int PENDING_SUBJECT = 1;
    public static final int PENDING_MESSAGE = 2;
    // ***IMPORTANT: Listener module is in ChatFormatter.
    HelpTable read = new HelpTable("/mail read [unread,<msgNumber>,sent]", "To read your messages", "", "mail read");
    HelpTable write = new HelpTable("/mail write <recipient name>", "To send new messages", "", "mail write");
    HelpTable writesend = new HelpTable("/mail send <recipient name>", "To send your message in the buffer", "", "mail send");
    HelpTable del = new HelpTable("/mail delete <message number>", "To delete messages for everyone", "", "mail delete");
    HashMap<CommandSender, Integer> status = new HashMap<>();
    static final int ALL = 1;
    static final int UNREAD = 2;
    static final int SENT = 3;
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(Sandbox.check(sender)) return true;
        if(sender instanceof Player){
            if(args.length == 0){
                sender.sendMessage(ChatColor.GOLD + "Please choose a subcommand:");
                sender.sendMessage(ChatColor.YELLOW + "/mail read");
                sender.sendMessage(ChatColor.YELLOW + "/mail write");
                sender.sendMessage(ChatColor.YELLOW + "/mail send");
                sender.sendMessage(ChatColor.YELLOW + "/mail delete");
                sender.sendMessage(ChatColor.DARK_AQUA + "For help, use /mail help <subcommand>");
                return true;
            }
            if(args.length > 0){
                MailMan man = MailMan.getPersonalAssistant((Player) sender);
                switch(args[0]){
                    case "read":

                        List<Message> messages;
                        try {
                            messages = FileParser.getInstance().getMessagesOf((Player) sender);
                        } catch (Exception e){
                            e.printStackTrace();
                            return true;
                        }
                        if(args.length == 2) {
                            if(isNumber(args[1])) {
                                //specific msgs
                                if (messages.size() - 1 < Integer.parseInt(args[1])) {
                                    ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "That selection is out of range.");
                                    return true;
                                }
                                    int selection = ALL;
                                    if (status.get(sender) != null) {
                                        selection = status.get(sender);
                                    }

                                    Message sel;
                                    switch (selection) {
                                        case ALL:
                                            sel = messages.get(Integer.parseInt(args[1]));
                                            break;
                                        case UNREAD:
                                            sel = man.yourUnreadMessages().get(Integer.parseInt(args[1]));
                                            break;
                                        case SENT:
                                            sel = man.yourSentMessages().get(Integer.parseInt(args[1]));
                                            break;
                                        default:
                                            sender.sendMessage("Server is drunk, please standby.");
                                            return true;
                                    }
                                    displayMessage(sender, sel);
                                } else if (args[1].equals("unread")) {
                                    //new msgs
                                    displayMessages(sender, man.yourUnreadMessages());
                                    status.put(sender, UNREAD);
                                } else if (args[1].equals("sent")) {

                                    displayMessages(sender, man.yourSentMessages());
                                    status.put(sender, SENT);
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Invalid Option: " + args[1]);
                                }
                            } else {
                            //read messages
                            displayMessages(sender, messages);
                            status.remove(sender);
                        }

                        break;
                    case "write":
                        if(!mailWriteStatus.containsKey(sender) || mailWriteStatus.get(sender) == NOT_PENDING) {
                            mailWriteStatus.put((Player) sender, PENDING_SUBJECT);
                            ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "You may now write the subject of the message.");
                        }else{
                            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You already started writing your message. To send the message, use " +
                                    ChatColor.YELLOW + "/mail send <recipient>");
                        }
                        break;
                    case "send":
                        if (!ChatFormatter.activeBuffer.containsKey(sender)) {

                            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You do not have a message buffer. To write, use " + ChatColor.YELLOW + "/mail write");
                        }else {
                            if(ChatFormatter.activeBuffer.get(sender).size() < 2){
                                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Your buffer is missing a subject and/or a body. Type your subject/body in the chat.");
                            } else {

                                List<String> msgdata = ChatFormatter.activeBuffer.get(sender);
                                mailWriteStatus.remove(sender);
                                ChatWriter.writeTo(sender, ChatWriterType.POSTMAN,
                                        MailMan.getPersonalAssistant((Player) sender).deliverMessage(Bukkit.getOfflinePlayer(args[1]), msgdata.get(0), msgdata.get(1)) ?
                                                "Succesfully sent the message to " + ChatColor.YELLOW + args[1] : "Failed to send message.");

                                ChatFormatter.activeBuffer.remove(sender);
                            }
                        }
                        return true;
                    case "delete":
                        if(args.length == 1 || !isNumber(args[1])){
                            del.show(sender);
                            return true;
                        }
                        int selection = ALL;
                        if (status.get(sender) != null) {
                            selection = status.get(sender);
                        }
                        int input = Integer.parseInt(args[1]);
                        if(input >= FileParser.getInstance().getMessagesOf((Player) sender).size()){
                            ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "That selection is out of range.");
                            return true;
                        }
                        switch (selection) {
                            case ALL:
                                if (FileParser.msgs.remove(FileParser.getInstance().getMessagesOf((Player) sender).get(input))){
                                    ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "Successfully deleted that message.");
                                }else{
                                    ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "Failed to delete. " +
                                            "Are you sure that number is not out of range?");
                                }

                                break;
                            case UNREAD:
                                if(FileParser.msgs.remove(man.yourUnreadMessages().get(input))){
                                    ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "Successfully deleted that message.");
                                }else{
                                    ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "Failed to delete. " +
                                            "Are you sure that number is not out of range?");
                                }
                                break;
                            case SENT:
                                if(FileParser.msgs.remove(man.yourSentMessages().get(input))){
                                    ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "Successfully deleted that message.");
                                }else{
                                    ChatWriter.writeTo(sender, ChatWriterType.POSTMAN, "Failed to delete. " +
                                            "Are you sure that number is not out of range?");
                                }
                                break;
                            default:
                                sender.sendMessage("Server is drunk, please standby.");
                                return true;
                        }
                        break;
                    case "help":
                        if(args.length == 1){
                            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Facepalm...");
                            return true;
                        }
                        switch(args[1]){
                            case "read":
                                read.show(sender);
                                break;
                            case "write":
                                write.show(sender);
                                break;
                            case "send":
                                writesend.show(sender);
                                break;
                            case "delete":
                                del.show(sender);
                                break;
                            default:
                                sender.sendMessage(ChatColor.RED + "No such option: " + args[1]);
                        }
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "Unknown option: " + args[0]);
                        sender.sendMessage(ChatColor.GOLD + "Please choose a subcommand:");
                        sender.sendMessage(ChatColor.YELLOW + "/mail read");
                        sender.sendMessage(ChatColor.YELLOW + "/mail write");
                        sender.sendMessage(ChatColor.YELLOW + "/mail send");
                        sender.sendMessage(ChatColor.YELLOW + "/mail delete");
                        sender.sendMessage(ChatColor.DARK_AQUA + "For help, use /mail help <subcommand>");
                        return true;
                }
            }
        }else{
            //admin cannot send messages
            try {
                if(args.length == 0) {
                    displayMessages(sender, FileParser.msgs);
                }else{
                    displayMessage(sender, FileParser.msgs.get(Integer.parseInt(args[0])));
                }
            } catch (Exception e){
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
            if(!msg.isRead()) tell += ChatColor.RED.toString() + ChatColor.BOLD + " UNREAD";
            to.sendMessage(tell);
        }
        ChatWriter.writeTo(to, ChatWriterType.POSTMAN, "Use " + ChatColor.YELLOW + "/mail read <message number>" + ChatColor.GRAY + " to read a specific message.");
    }
    private void displayMessage(CommandSender to, Message subject){
        ChatWriter.writeTo(to, ChatWriterType.POSTMAN, "Reading message from " + subject.getSender().getName());
        to.sendMessage("");
        to.sendMessage(ChatColor.BOLD + subject.getSubject());
        to.sendMessage(subject.getMessage());
        to.sendMessage("");
        ChatWriter.writeTo(to, ChatWriterType.POSTMAN, "Message finished");
        subject.setRead();
    }
}
