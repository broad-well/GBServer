package com.Gbserver.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Nick implements CommandExecutor {

	public static boolean clickPending = false;
	public static Entity target = null;
	public static boolean clicked = false;
	public static Player sender;
	public static String arg;
	public static boolean isNaming;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("nick")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Must be a player.");
				return false;
			}
			if (args.length < 1) {
				sender.sendMessage("Invalid Syntax. /nick <name/uname> <nickname>");
				return false;
			}
			switch(args[0]){
			case "name":
				if (args.length < 2) {
					sender.sendMessage("Invalid Syntax. /nick <name> <nickname>");
					return false;
				}
				Nick.isNaming = true;
				Nick.arg = args[1];
				Nick.sender = (Player) sender;
				sender.sendMessage(ChatColor.ITALIC + "Please right click an entity to be named.");
				clickPending = true;
				return true;
			case "uname":
				Nick.isNaming = false;
				Nick.arg = "";
				Nick.sender = (Player) sender;
				sender.sendMessage(ChatColor.ITALIC + "Please right click an entity to be un-named.");
				clickPending = true;
				return true;
			default:
				sender.sendMessage("Invalid Syntax. /nick <name/uname> <nickname>");
				return false;
			}
		}
		return false;
	}

}
