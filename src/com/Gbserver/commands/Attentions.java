package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Attentions implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		if (arg2.equalsIgnoreCase("broadcast")) {
			Bukkit.broadcastMessage(ChatColor.BLUE + "Announcement> " + ChatColor.YELLOW + concat(arg3));
			return true;
		}
		return false;
	}

	private String concat(String[] arg) {
		String output = "";
		for (String s : arg) {
			if (arg[arg.length - 1] == s) {
				output += s;
			} else {
				output += s + " ";
			}
		}
		return output;
	}

}
