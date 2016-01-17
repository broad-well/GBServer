package com.Gbserver.commands;


import com.Gbserver.variables.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class Marriage implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /*
        Usage:
        /marry list
        /marry create <groom> <bride>
        /marry delete <PUID>
         */
        if(args.length == 0){
            displayUsage(sender);
            return true;
        }

        switch(args[0]){
            case "list":
                sender.sendMessage("----------");
                for(HashMap<String, String> couple : Couple.data){
                    sender.sendMessage(ChatColor.BLUE + Identity.deserializeIdentity(couple.get("Proactive")).getName() +
                            ChatColor.RED + " <3 " + ChatColor.LIGHT_PURPLE + Identity.deserializeIdentity(couple.get("Reactive")).getName() + ChatColor.RESET + ":");
                    sender.sendMessage(ChatColor.DARK_AQUA + "  PUID : " + ChatColor.AQUA + couple.get("PUID"));
                    sender.sendMessage(ChatColor.DARK_AQUA + "  Time Of Marriage : " + ChatColor.AQUA + couple.get("TimeOfMarriage"));
                    sender.sendMessage("----------");
                }
                break;
            case "create":
                if(args.length < 3){
                    displayUsage(sender);
                }else{
                    if(Couple.newCouple(args[1], args[2])){
                        ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Marriage created!");
                        ChatWriter.write(ChatWriterType.ANNOUNCEMENT, sender.getName() + " has declared (" + ChatColor.BLUE + args[1] +
                                ChatColor.RED + " <3 " + ChatColor.LIGHT_PURPLE + args[2] + ChatColor.YELLOW + ") to be married!");
                    }else{
                        ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Creation failed.");
                    }
                }
                break;
            case "delete":
                if(args.length < 2){
                    displayUsage(sender);
                }else {
                    HashMap<String, String> marked = null;
                    for (HashMap<String, String> couple : Couple.data) {
                        if (couple.get("PUID").equals(args[1])) {
                            marked = couple;
                        }
                    }
                    if(marked == null){
                        ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Unable to find a marriage with this PUID.");
                    }else{
                        ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Deletion " + (Couple.purgeCouple(new PUID(args[1])) ?
                                "" : "un") + "successful.");
                    }
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown option: " + args[0]);
                break;
        }
        return true;
    }

    private static void displayUsage(CommandSender sender){
        sender.sendMessage(ChatColor.RED + "<3 /marry Usage <3");
        sender.sendMessage(ChatColor.GOLD + "/marry list");
        sender.sendMessage(ChatColor.GOLD + "/marry create <proactive IGN> <reactive IGN>");
        sender.sendMessage(ChatColor.GOLD + "/marry delete <puid>");
    }
}
