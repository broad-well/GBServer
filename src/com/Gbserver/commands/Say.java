package com.Gbserver.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Say implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof ConsoleCommandSender)){
			ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You are not authorized to use this command.");
			return true;
		}
		if(args.length == 0){
			sender.sendMessage("You should add a message, console!");
			return true;
		}
		String text = ChatColor.DARK_RED + "" + ChatColor.BOLD + "OP " + ChatColor.RESET + "" + ChatColor.GRAY + "Server " + ChatColor.RESET + args[0];
		return true;
	}
}
