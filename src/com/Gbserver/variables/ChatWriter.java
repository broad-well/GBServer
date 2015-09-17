package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatWriter {

	
	public static void write(ChatWriterType cwt, String message){
		
		Bukkit.broadcastMessage(getMessage(cwt, message));
	}
	
	public static String getMessage(ChatWriterType cwt, String message){
		String output = ChatColor.BLUE + "";
		switch(cwt){
		case ANNOUNCEMENT:
			output += "Announcement> " + ChatColor.YELLOW;
			break;
		case CHAT:
			output += "Chat> " + ChatColor.GRAY;
			break;
		case CONDITION:
			output += "Condition> " + ChatColor.GRAY;
			break;
		case ERROR:
			output += "Error> " + ChatColor.GRAY;
			break;
		case GAME:
			output += "Game> " + ChatColor.GRAY;
			break;
		case COMMAND:
			output += "Command> " + ChatColor.GRAY;
			break;
		case SERVER:
			output += "Server> " + ChatColor.YELLOW;
			break;
		case VOTE:
			output += "Vote> " + ChatColor.GRAY;
			break;
		case JOIN:
			output += ChatColor.DARK_AQUA + "Join> " + ChatColor.GRAY;
			break;
		case QUIT:
			output += ChatColor.DARK_AQUA + "Quit> " + ChatColor.GRAY;
			break;
		case HELP:
			output += ChatColor.LIGHT_PURPLE + "Help> " + ChatColor.WHITE;
			break;
		}
		output += message;
		return output;
	}
}
