package com.Gbserver.commands;

import org.bukkit.ChatColor;

public enum Team {
	RED,
	BLUE,
	undefined;
	public static String toString(Team t){
		switch(t){
		case RED:
			return ChatColor.RED + "RED";
		case BLUE:
			return ChatColor.BLUE + "BLUE";
		case undefined:
			return ChatColor.GRAY + "undefined";
		}
		return "";
	}
	public static Team opposite(Team t){
		switch(t){
		case RED:
			return BLUE;
		case BLUE:
			return RED;
		case undefined:
			return undefined;
		}
		return null;
	}
}
