package com.Gbserver.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.Main;
import com.Gbserver.listener.Announce;

public class Protection implements CommandExecutor {
	public static Main instance;

	public Protection(Main instance) {
		Protection.instance = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (label.equalsIgnoreCase("protection")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Needs to be a player.");
				return false;
			}
			Announce.registerEvents();
		}
		return false;
	}

}
