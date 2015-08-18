package com.Gbserver.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Afk implements CommandExecutor{
	public static List<Player> afkList = new ArrayList<Player>();
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("afk")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Only players are allowed to AFK.");
				return false;
			}
			Player p = (Player) sender;
			doAFK(p);
			return true;
		}
		if(label.equalsIgnoreCase("isafk")){
			if(args.length != 1){
				sender.sendMessage("Invalid Syntax. /isafk <player>");
				return false;
			}
			Player target = Bukkit.getServer().getPlayer(args[0]);
			if(afkList.contains(target)){
				sender.sendMessage(ChatColor.GRAY + target.getName() + " is AFK.");
			}else{
				sender.sendMessage(ChatColor.GRAY + target.getName() + " is not AFK.");
			}
			return true;
		}
		return false;
	}
	
	public static void doAFK(Player target){
		if(!afkList.contains(target)){
			afkList.add(target);
			Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Server: " + target.getName() + " is now AFK.");
		}else{
			afkList.remove(target);
			Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Server: " + target.getName() + " is no longer AFK.");
		}
	}
}
