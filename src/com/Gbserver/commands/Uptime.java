package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.listener.StatOnlineTime;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Preferences;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class Uptime implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(Utilities.validateSender(sender)){
            //Calculate one's uptime using hs LogEntries.
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Calculating your uptime...");
            long lastLogin = 0;
            long sumTime = 0;
            List<String> responseStack = new LinkedList<>();
            boolean loggedIn = false;
            for(StatOnlineTime.LogEntry entry : StatOnlineTime.cache.get(((Player) sender).getUniqueId())){
                if(entry.isLogIn == !loggedIn){ //If IsCompatible
                    if(entry.isLogIn){
                        lastLogin = entry.timeframe.getTime();
                        loggedIn = true;
                    }else{
                        sumTime += (entry.timeframe.getTime() - lastLogin) / 1000;
                        responseStack.add("Successfully added " + ((entry.timeframe.getTime() - lastLogin) / 1000) + " seconds to the sumTime stack, date " +
                                StatOnlineTime.LogEntry.parser.format(entry.timeframe));
                        loggedIn = false;
                    }
                }else{
                    responseStack.add("Unmatched login/logout found. This is likely because of a faulty save. Time " +
                            StatOnlineTime.LogEntry.parser.format(entry.timeframe));
                }
            }
            if(Preferences.get().get("Debug").equals("true")) {
                sender.sendMessage(ChatColor.AQUA + "Calculation finished. Debug logs below");
                sender.sendMessage(responseStack.toArray(new String[1]));
            }
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You have been online for " + sumTime + " seconds.");
        }
        return true;
    }
}
