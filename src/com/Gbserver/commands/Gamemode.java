package com.Gbserver.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Gamemode implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("gm")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
				return false;
			}
			Player p = (Player) sender;
			if(args.length < 1){
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
				return false;
			}
			boolean isInGame = (Runner.isRunner(p)) || (BL.players.contains(p.getName())) || TF.getAllPlayers().contains(p);
			switch(args[0]){
			case "c":
				if(!isInGame){
				p.setGameMode(GameMode.CREATIVE);
				}else{
					p.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You cannot change gamemode in games!"));
				}
				return true;
			case "s":
				if(!isInGame){
					p.setGameMode(GameMode.SURVIVAL);
					}else{
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You cannot change gamemode in games!"));
					}
				return true;
			case "p":
				if(!isInGame){
					p.setGameMode(GameMode.SPECTATOR);
					}else{
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You cannot change gamemode in games!"));
					}
				return true;
			}
		}
		return false;
	}
}
