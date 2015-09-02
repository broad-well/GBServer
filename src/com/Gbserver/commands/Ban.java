package com.Gbserver.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Ban implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg2.equalsIgnoreCase("tellraw") || arg2.equalsIgnoreCase("ban") || arg2.equalsIgnoreCase("banip")
				|| arg2.equalsIgnoreCase("stop") || arg2.equalsIgnoreCase("restart")){
			if(arg0 instanceof Player && !(((Player) arg0).getName().equals("_Broadwell"))){
				arg0.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "This command is not allowed, sorry!"));
				return true;
			}
		}
		return false;
	}

}
