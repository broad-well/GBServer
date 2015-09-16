package com.Gbserver.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Vaults;

public class Vote implements CommandExecutor {
	/*
	 * How?

/vote create * * * players * * *
/vote results
/vote close
/vote (voteFor)
	 */
	public static List<String> options = new LinkedList<String>();
	public static int[] votes = new int[9];
	public static List<Player> voted = new LinkedList<>();
	public static List<Player> players = new LinkedList<>();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("vote")) {
			// If there are args, create a new vote.
			// If there is not, delete the current vote.
			// /vote select
			// /vote results
			if (Utilities.validateSender(sender)) {
				if (args.length == 0) {
					//Help coming.
				} else {
					if (args[0].equalsIgnoreCase("results")){
						for(int i = 0; i < options.size(); i++){
							sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, (i+1) + ": " + votes[i]));
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("create")){
						// Create vote.
						if (args.length == 2) {
							sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE,
									"You cannot create a vote with only 1 option."));
							return true;
						}
						options.clear();
						voted.clear();
						players.clear();
						sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, "You created a vote."));
						boolean playerMode = false;
						for (int i = 1; i < args.length; i++) {
							if(!playerMode){
								if(args[i].equalsIgnoreCase("players")){
									playerMode = true;
								}else{
									options.add(args[i]);
								}
							}else{
								players.add(Bukkit.getPlayer(args[i]));
							}
							
							//sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, args[i]));
						}
						votes = new int[options.size()];
						if (!(players.isEmpty())) {
							//Broadcast.
							for (Player p : players) {
								p.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE,
										"You have been invited into a vote."));
								for (int i = 0; i < options.size(); i++) {
									p.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE,
											(i + 1) + ": " + options.get(i)));
								}
								p.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE,
										ChatColor.BOLD + "Type /vote <your selection in number>"));
								
							}
							return true;
						}else{
							ChatWriter.write(ChatWriterType.VOTE,
									"You have been invited into a vote.");
							for (int i = 0; i < options.size(); i++) {
								ChatWriter.write(ChatWriterType.VOTE,
										(i + 1) + ": " + options.get(i));
							}
							ChatWriter.write(ChatWriterType.VOTE,
									ChatColor.BOLD + "Type /vote <your selection in number>");
							return true;
						}

					}
					if(args[0].equalsIgnoreCase("close")){
						if(!(options.isEmpty())){
							options.clear();
							voted.clear();
							players.clear();
							votes = new int[9];
							sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "The vote has been disabled."));
						}else{
							sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "There is no vote ongoing."));
						}
						return true;
					}
					{
						if (players.isEmpty() || players.contains((Player) sender)) {
							// Vote for.
							if (!(voted.contains((Player) sender))) {
								int selection = 0;
								try {
									selection = Integer.parseInt(args[0]);
								} catch (Exception e) {
									sender.sendMessage(
											ChatWriter.getMessage(ChatWriterType.COMMAND, "This is not a number!"));
									return true;
								}
								if (!(options.isEmpty())) {
									voteFor(selection);
									sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE,
											"You voted for option " + selection + ", " + options.get(selection - 1)));
									voted.add((Player) sender);
								} else {
									sender.sendMessage(
											ChatWriter.getMessage(ChatWriterType.COMMAND, "There is no vote ongoing."));
								}
								return true;
							} else {
								sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, "You already voted!"));
								return true;
							} 
						} else {
							sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, "You are not participating in this vote."));
						}
						return true;
					}
				}

			} else {
				return false;
			}
		}
		return false;
	}

	public static boolean isActive() {
		return options.size() != 0;
	}

	public static void clear() {
		options = new LinkedList<>();
		voted = new LinkedList<>();
		votes = new int[4];
		
	}

	public static void voteFor(int number) {
		votes[number - 1]++;
	}

}
