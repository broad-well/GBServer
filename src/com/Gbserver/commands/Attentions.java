package com.Gbserver.commands;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Gbserver.listener.Announce;
import com.Gbserver.variables.AnnounceTask.Tasks;

public class Attentions implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		if (arg2.equalsIgnoreCase("broadcast")) {
			Bukkit.broadcastMessage(ChatColor.BLUE + "Announcement> " + ChatColor.YELLOW + concat(arg3));
			return true;
		}
		if (arg2.equalsIgnoreCase("announce")) {
			if(arg3.length < 2){
				arg0.sendMessage("Bad Syntax.");
				return false;
			}
			switch(arg3[0]){
			case "add":
				String m = concat(Arrays.copyOfRange(arg3, 1, arg3.length));
				Announce.announcements.add(m);
				arg0.sendMessage("Added announcement: "+m);
				Tasks.reload();
			}
			return true;
		}
		return false;
	}

	private String concat(String[] arg) {
		String output = "";
		for (int i = 0; i < arg.length; i++) {
			String s = arg[i];
			if (i == arg.length-1) {
				output += s;
			} else {
				output += s + " ";
			}
		}
		return output;
	}
	

}
