package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Tell implements CommandExecutor {
	String[] global;
	String globalOutput;
	@EventHandler
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("tell") || label.equalsIgnoreCase("t") || label.equalsIgnoreCase("msg")
				|| label.equalsIgnoreCase("m")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Needs to be a player to execute this command.");
				return false;
			}
			Player player = (Player) sender;
			if (args.length < 2) {
				sender.sendMessage("Invalid Syntax. /tell(t,msg,m) <Player> <Message>.");
				return false;
			} else {
				global = args;
				globalOutput = "";
				concatenateMultipleSpaces();
				Player tp = Bukkit.getPlayer(args[0]);
				tp.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ": " + globalOutput);
				tp.playNote(tp.getLocation(),Instrument.PIANO,Note.natural(1, Tone.G));
				player.sendMessage("Successfully sent message to " + tp.getName() + ": " + globalOutput);
				return true;
			}

			
		} else {
			return false;
		}
	}
	
	void concatenateMultipleSpaces(){
		for(int i = 1; i < global.length; i++){
			//args[1] + " " + args[2] + " " + args[3] + " " etc.
			globalOutput = globalOutput + global[i] + " ";
		}
	}

}
