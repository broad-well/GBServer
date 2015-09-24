package com.Gbserver.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;

public class Gamemode implements CommandExecutor{
	
	private HelpTable ht = new HelpTable("/gm <c/s/p> (c=creative, s=survival, p=spectator)", "/gm is used to change a player's gamemode.", "", "gm");
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("gm")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
				return false;
			}
			Player p = (Player) sender;
			if(args.length < 1){
				ht.show(sender);
				return true;
			}
			boolean isInGame = (BL.players.contains(p.getName())) || TF.getAllPlayers().contains(p);
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
