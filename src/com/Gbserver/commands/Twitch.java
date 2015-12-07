package com.Gbserver.commands;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by michael on 12/6/15.
 */
public class Twitch implements CommandExecutor{
    String currentPlayer = "";
    String currentUser = "";
    HelpTable ht = new HelpTable("/twitch <[twitch username] / [end] / [announce] / [who]>", "To automate stream announcements.", "", "twitch");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            ht.show(sender);
        }else{
            switch(args[0]){
                case "end":
                   if(currentUser.isEmpty() && currentPlayer.isEmpty()){
                       ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "There's nobody streaming right now. To start streaming, use " +
                               ChatColor.YELLOW + "/twitch <your twitch username>");
                   }else{
                       ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "Ended current Twitch stream: " + ChatColor.YELLOW + currentUser);
                       currentUser = "";
                       currentPlayer = "";
                   }
                    break;
                case "announce":
                    if(currentUser.isEmpty()){
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "There's nobody streaming right now. To start streaming, use " +
                                ChatColor.YELLOW + "/twitch <your twitch username>");
                    }else{
                        ChatWriter.write(ChatWriterType.TWITCH, ChatColor.BOLD.toString() + ChatColor.YELLOW + currentPlayer
                                + ChatColor.GRAY + " is streaming at " + ChatColor.YELLOW + "http://twitch.tv/" + currentUser
                                + ChatColor.GRAY + ", check it out!");
                    }
                    break;
                case "who":
                    if(currentUser.isEmpty()){
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "There's nobody streaming right now.");
                    }else{
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, ChatColor.YELLOW + currentPlayer + ChatColor.GRAY + " is streaming right now.");
                    }
                    break;
                default:
                    if(!(currentUser.isEmpty() && currentPlayer.isEmpty())) {
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "Somebody is streaming!");
                    }else{
                        currentPlayer = sender.getName();
                        currentUser = args[0];
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "You started streaming at " + ChatColor.YELLOW +
                                "http://twitch.tv/" + args[0]);
                    }
                    break;
            }
        }
        return true;
    }
}
