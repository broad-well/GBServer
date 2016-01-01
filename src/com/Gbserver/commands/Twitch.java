package com.Gbserver.commands;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import com.Gbserver.variables.Sandbox;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 12/6/15.
 */
public class Twitch implements CommandExecutor{
    public static HashMap<String, String> streamers = new HashMap<>(); // ign, twitch username
    HelpTable ht = new HelpTable("/twitch <[twitch username] / [end] / [announce] / [who]>", "To automate stream announcements.", "", "twitch");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (args.length == 0) {
            ht.show(sender);
        }else{
            switch(args[0]){
                case "end":
                   if(streamers.isEmpty()){
                       ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "There's nobody streaming right now. To start streaming, use " +
                               ChatColor.YELLOW + "/twitch <your twitch username>");
                   }else{
                       if(streamers.keySet().contains(sender.getName())) {
                           streamers.remove(sender.getName());
                           ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "Ended current Twitch stream: " + ChatColor.YELLOW + sender.getName());
                       }else{
                           ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "You are not streaming, you have no privilege to end the current stream(s).");
                       }
                   }
                    break;
                case "announce":
                    if(streamers.isEmpty()){
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "There's nobody streaming right now. To start streaming, use " +
                                ChatColor.YELLOW + "/twitch <your twitch username>");
                    }else{
                        if(streamers.keySet().contains(sender.getName())) {
                            ChatWriter.write(ChatWriterType.TWITCH, ChatColor.BOLD.toString() + ChatColor.YELLOW + sender.getName()
                                    + ChatColor.GRAY + " is streaming at " + ChatColor.YELLOW + "http://twitch.tv/" + streamers.get(sender.getName())
                                    + ChatColor.GRAY + ", check it out!");
                        }else{
                            ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "You are not streaming, you cannot announce.");
                        }
                    }
                    break;
                case "who":
                    if(streamers.isEmpty()){
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "There's nobody streaming right now.");
                    }else{
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "A list of live streamers right now:");
                        for(Map.Entry<String, String> entry : streamers.entrySet()){
                            ChatWriter.writeTo(sender, ChatWriterType.TWITCH, ChatColor.YELLOW + entry.getKey() + ChatColor.GRAY + " streaming at " +
                                    ChatColor.YELLOW + "http://twitch.tv/" + entry.getValue());
                        }
                    }
                    break;
                default:
                    if(streamers.containsKey(sender.getName())){
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "You already have a stream in progress! Use " + ChatColor.YELLOW + "/twitch end" +
                                ChatColor.GRAY + " to end your current stream.");
                    }else {
                        streamers.put(sender.getName(), args[0]);
                        ChatWriter.writeTo(sender, ChatWriterType.TWITCH, "You started streaming at " + ChatColor.YELLOW +
                                "http://twitch.tv/" + args[0]);
                    }
                    break;
            }
        }
        return true;
    }
}
