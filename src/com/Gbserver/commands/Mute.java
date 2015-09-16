package com.Gbserver.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;

public class Mute implements CommandExecutor{
	public static List<Player> list = new LinkedList<>();
	private HelpTable ht = new HelpTable("/mute <player to mute>", "/mute is used for general muting funtionality.", "", "mute");
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("mute")){
			if(sender instanceof Player && !(((Player) sender).getName().equals("_Broadwell"))){
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "You are not authorized to mute."));
				return true;
			}
			if(args.length == 0){
				ht.show(sender);
				return true;
			}
			if(list.contains(Bukkit.getPlayer(args[0]))){
				list.remove(Bukkit.getPlayer(args[0]));
				Bukkit.getPlayer(args[0]).sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You have been unmuted."));
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Removed " + Bukkit.getPlayer(args[0]).getName()) + " from muted list."); 
			}else{
				list.add(Bukkit.getPlayer(args[0]));
				Bukkit.getPlayer(args[0]).sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You have been muted."));
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Added " + Bukkit.getPlayer(args[0]).getName()) + " from muted list.");
			}
			return true;
		}
		return false;
	}
}
