package com.Gbserver.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {
	public static boolean pending = false;
	public static Player sender;
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("heal")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Needs to be a player to execute this command.");
				return false;
			}
			pending = true;
			Heal.sender = (Player) sender;
			sender.sendMessage(ChatColor.ITALIC + "Please click on a living entity to heal it.");
			return true;
		}
		return false;
	}
			

}
