package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Attentions implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		if(arg2.equalsIgnoreCase("broadcast")){
			String message = arg3[0];
			Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + message);
			return true;
		}
		return false;
	}
	
}
