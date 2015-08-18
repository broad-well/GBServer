package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Spawn implements CommandExecutor {
	@EventHandler
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("spawn")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Needs to be a player to execute this command.");
				return false;
			}
			Player player = (Player) sender;
			World world = Bukkit.getServer().getWorld("world");
			Location spawn = new Location(world, 145, 119, 413);
			player.teleport(spawn);
			player.sendMessage("Successfully teleported " + player.getName() + " to the spawn.");
			Runner.isRunning = false;
			return true;
		} else {
			return false;
		}
	}

}