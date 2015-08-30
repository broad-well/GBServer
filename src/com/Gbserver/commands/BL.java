package com.Gbserver.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class BL implements CommandExecutor {
	public static Collection<String> players = new LinkedList<String>();
	public static boolean isRunning = false;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("bl")) {
			if (args.length < 1) {
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
				return false;
			}
			switch (args[0]) {
			case "addPlayer":
				if (args.length < 2) {
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
					return false;
				}
				for (int i = 1; i < args.length; i++) {
					Player p = Bukkit.getServer().getPlayer(args[i]);
					players.add(p.getName());
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully added " + p.getName() + " to BL players list."));
				}
				return true;
			case "start":
				isRunning = true;
				for(Object p : players.toArray()){
					Player pl = Bukkit.getServer().getPlayer((String) p);
				}
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully started the game."));
				return true;
			case "reset":
				isRunning = false;
				players.clear();
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully reset the game."));
				return true;
			}
		}
		return false;
	}
}
