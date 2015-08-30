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
