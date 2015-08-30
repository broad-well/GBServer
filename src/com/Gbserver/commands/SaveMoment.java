package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

// BUGGED!
public class SaveMoment implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("save")){
			if(args.length < 1){
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
				return false;
			}
			Player p = Bukkit.getServer().getPlayer(args[0]);
			p.saveData();
			sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Successfully saved "+p.getName()+"'s Inventory, Location, and Active Potion Effects."));
			return true;
		}
		if(label.equalsIgnoreCase("rest")){
			if(args.length < 1){
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
				return false;
			}
			Player p = Bukkit.getServer().getPlayer(args[0]);
				p.loadData();
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Successfully restored "+p.getName()+"'s Inventory, Location, and Active Potion Effects."));
				return true;

			
		}
		return false;
	}
}
