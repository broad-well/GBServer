package com.Gbserver.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Ride implements CommandExecutor {
	public static boolean pending = false;
	public static Player p;
	public static boolean ridingOthers = false;
	public static boolean hasPassenger = false;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("ride")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
				return false;
			}
			ridingOthers = true;
			pending = true;
			p = (Player) sender;
			sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, ChatColor.ITALIC + "Please right click an entity to ride on it."));
			return true;
		}
		if (label.equalsIgnoreCase("rideme")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
				return false;
			}
			

			p = (Player) sender;
			if (hasPassenger) {
				p.eject();
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, ChatColor.ITALIC + "Successfully dismounted your passenger."));
				hasPassenger = false;
			} else {
				ridingOthers = false;
				pending = true;
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, ChatColor.ITALIC + "Please right click an entity to make it ride on you."));
			}
			return true;
		}
		return false;
	}
}
