package com.Gbserver.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class NoFall implements CommandExecutor{
	public static boolean noFall = false;
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg2.equalsIgnoreCase("nofall")){
			noFall = !noFall;
			arg0.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You have toggled No falling damage."));
			return true;
		}
		return false;
	}
	
}
