package com.Gbserver.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.Main;
import com.Gbserver.listener.Announce;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Attentions implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		if (arg2.equalsIgnoreCase("broadcast")) {
			ChatWriter.write(ChatWriterType.ANNOUNCEMENT, concat(arg3));
			return true;
		}
		
		if (arg2.equalsIgnoreCase("announce")) {
			if(arg0 instanceof Player){
				if(!(arg0.getName().equals("_Broadwell"))){
					arg0.sendMessage("You are not authorized to use this command.");
					return true;
				}
			}
			List<String> a = Announce.getPlugin().getConfig().getStringList("announcements");
			a.add(concat(arg3));
			Announce.getPlugin().getConfig().set("announcements", a);
			Announce.announcement.add(concat(arg3));
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
