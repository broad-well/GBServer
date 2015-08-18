package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveMoment implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("save")){
			if(args.length < 1){
				sender.sendMessage("Missing player. /save <player to save>");
				return false;
			}
			Player p = Bukkit.getServer().getPlayer(args[0]);
			p.saveData();
			sender.sendMessage("Successfully saved "+p.getName()+"'s Inventory, Location, and Active Potion Effects.");
			return true;
		}
		if(label.equalsIgnoreCase("rest")){
			if(args.length < 1){
				sender.sendMessage("Missing player. /restore <player to restore>");
				return false;
			}
			Player p = Bukkit.getServer().getPlayer(args[0]);
				p.loadData();
				sender.sendMessage("Successfully restored "+p.getName()+"'s Inventory, Location, and Active Potion Effects.");
				return true;

			
		}
		return false;
	}
}
