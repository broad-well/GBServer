package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class Say implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof ConsoleCommandSender)) {
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You are not authorized to use this command.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("You should add a message, console!");
            return true;
        }
        String text = ChatColor.DARK_RED + "" + ChatColor.BOLD + "OP " + ChatColor.RESET + "" + ChatColor.GRAY
                + "Server " + ChatColor.RESET + Utilities.concat(args);
        Bukkit.broadcastMessage(text);
        return true;
    }
}
