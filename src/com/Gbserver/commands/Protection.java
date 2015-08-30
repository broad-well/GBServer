package com.Gbserver.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.Main;
import com.Gbserver.listener.Announce;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Protection implements CommandExecutor {
	public static Main instance;

	public Protection(Main instance) {
		Protection.instance = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (label.equalsIgnoreCase("protection")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
				return false;
			}
			Announce.registerEvents();
		}
		return false;
	}

}
