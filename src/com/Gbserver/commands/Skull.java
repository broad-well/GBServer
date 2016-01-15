package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Skull implements CommandExecutor{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(Sandbox.check(sender)) return true;
        if(Utilities.validateSender(sender)) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.AQUA + "/skull usage: /skull <player name>");
                return true;
            }
            Player p = (Player) sender;
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + p.getName() + " skull 1 3 {SkullOwner:\"" + args[0] + "\"}");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Successfully given the skull of " + ChatColor.YELLOW + args[0]);
        }
        return true;
    }
}
