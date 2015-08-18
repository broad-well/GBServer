package com.Gbserver.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("gm")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Must be a player.");
				return false;
			}
			Player p = (Player) sender;
			if(args.length < 1){
				sender.sendMessage("Missing operand. c/s/p.");
				return false;
			}
			boolean isInGame = (Runner.isRunner(p)) || (BL.players.contains(p.getName()));
			switch(args[0]){
			case "c":
				if(!isInGame){
				p.setGameMode(GameMode.CREATIVE);
				}else{
					p.sendMessage("You cannot change gamemode in games!");
				}
				return true;
			case "s":
				if(!isInGame){
					p.setGameMode(GameMode.SURVIVAL);
					}else{
						p.sendMessage("You cannot change gamemode in games!");
					}
				return true;
			case "p":
				if(!isInGame){
					p.setGameMode(GameMode.SPECTATOR);
					}else{
						p.sendMessage("You cannot change gamemode in games!");
					}
				return true;
			}
		}
		return false;
	}
}
