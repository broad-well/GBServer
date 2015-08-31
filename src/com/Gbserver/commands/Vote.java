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

	public static List<String> options = new LinkedList<String>();
	public static int[] votes = new int[4];
	public static List<Player> voted = new LinkedList<>();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("vote")) {
			// If there are args, create a new vote.
			// If there is not, delete the current vote.
			// /vote select
			// /vote results
			if (Utilities.validateSender(sender)) {
				if (args.length == 0) {
					// Delete vote
					if (isActive()) {
						clear();
						sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, "Disabled the current vote."));
					} else {
						sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, "There is no vote ongoing."));
					}
					return true;
				} else {
					if (args[0].equalsIgnoreCase("select")) {
						// Selecting.
						if(!(voted.contains((Player) sender))){
							int selection = 0;
							try {
								selection = Integer.parseInt(args[1]);
							} catch (Exception e) {
								sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "This is not a number!"));
								return true;
							}
							voteFor(selection);
							sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE,
									"You voted for option " + selection + ", " + options.get(selection - 1)));
							voted.add((Player) sender);
							return true;
						}else{
							sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, "You already voted!"));
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("results")){
						for(int i = 0; i < options.size(); i++){
							sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, (i+1) + ": " + votes[i]));
						}
						return true;
					}else{
						// Create vote.
						if (args.length == 1) {
							sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE,
									"You cannot create a vote with only 1 option."));
							return true;
						}
						sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, "You created a vote."));
						for (int i = 0; i < args.length; i++) {
							options.add(args[i]);
							//sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, args[i]));
						}
						
						//Broadcast.
						ChatWriter.write(ChatWriterType.VOTE, "You have invited into a vote.");
						for (int i = 0; i < options.size(); i++) {
							ChatWriter.write(ChatWriterType.VOTE, (i+1) + ": " + options.get(i));
						}
						ChatWriter.write(ChatWriterType.VOTE, ChatColor.BOLD + "Type /vote select <your selection in number>");
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
		for (int i : votes) {
			i = 0;
		}
		voted = new LinkedList<>();
		
	}

	public static void voteFor(int number) {
		votes[number - 1]++;
	}

}
