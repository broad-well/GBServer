package com.Gbserver.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import com.Gbserver.variables.Vaults;

public class Quit implements CommandExecutor{
	
	public static List<Player> ragequitters = new LinkedList<>();
	public static List<Player> afkers = new LinkedList<>();
	public static List<Player> diers = new LinkedList<>();
	
	private HelpTable ht = new HelpTable("/quit <ragequit/afk/die>", "/quit is used for custom quit messages.", "", "quit");
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("quit")){
			if(Utilities.validateSender(sender)){
				if(args.length == 0){
					ht.show(sender);
					return true;
				}
				Player p = (Player) sender;
				switch(args[0]){
				case "ragequit":
					ragequitters.add(p);
					p.kickPlayer("You RAGEQUITted!");
					break;
				case "afk":
					afkers.add(p);
					p.kickPlayer("You have been AFK removed by yourself!");
					break;
				case "die":
					diers.add(p);
					p.kickPlayer("You have died.");
					break;
				default:
					ht.show(sender);
					return true;
				}
				return true;
			}else{
				return true;
			}
			
		}
		return false;
	}

}
