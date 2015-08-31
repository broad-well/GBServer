package com.Gbserver;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Utilities {
	public static boolean validateSender(CommandSender cs){
		if(!(cs instanceof Player)){
			cs.sendMessage(ChatWriter.getMessage(ChatWriterType.CHAT, "Only players are allowed."));
			return false;
		}else{
			return true;
		}
	}
}
