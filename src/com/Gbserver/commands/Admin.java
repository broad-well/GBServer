package com.Gbserver.commands;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.variables.CPrefix;
import com.Gbserver.variables.Sandbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Admin implements CommandExecutor{

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(Main.onEvent){
            if(Sandbox.check(sender)) return true;
            String message = Utilities.concat(args);
            for(String str : ChatFormatter.staff){
                Player p = Bukkit.getPlayer(str);
                if(p != null){
                    p.sendMessage(ChatColor.LIGHT_PURPLE + sender.getName() + " > " + ChatColor.RESET + message);
                    sender.sendMessage(ChatColor.AQUA + "Sent message to " + p.getName());
                }
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + sender.getName() + " > " + ChatColor.RESET + message);
            sender.sendMessage(ChatColor.AQUA + "Sent message to console");
            return true;
        }else{
            sender.sendMessage(CPrefix.Prf.EVENT + "Event is not in progress. Sorry!");
            return true;
        }
    }
}
