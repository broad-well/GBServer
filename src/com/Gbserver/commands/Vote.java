package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import com.Gbserver.variables.Sandbox;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Vote implements CommandExecutor {
    /*
     * How?

/vote create * * * players * * *
/vote results
/vote close
/vote (voteFor)
     */
    public static HashMap<String, Integer> current = new HashMap<>();
    public static List<Player> voted = new LinkedList<>();
    private HelpTable ht = new HelpTable("/vote <create/results/close/(your vote selection)> <option> <option> (options required with \"create\")", "/vote is used for voting functionalities.", "", "vote");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        // If there are args, create a new vote.
        // If there is not, delete the current vote.
        // /vote select
        // /vote results

        if (Utilities.validateSender(sender)) {
            if (args.length == 0) {
                ht.show(sender);
                return true;
            } else {
                if (args[0].equalsIgnoreCase("results")) {
                    for (Map.Entry<String, Integer> entry : current.entrySet())
                        sender.sendMessage(entry.getValue() + " players voted for " + entry.getKey());
                    return true;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    if (!current.isEmpty()) {
                        ChatWriter.writeTo(sender, ChatWriterType.VOTE, "Current vote not cleared, use '/vote close' to close it.");
                        return true;
                    }
                    // Create vote.
                    if (args.length == 2) {
                        sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE,
                                "You cannot create a vote with only 1 option."));
                        return true;
                    }
                    sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, "You created a vote."));
                    for (int i = 1; i < args.length; i++) {
                        current.put(args[i], 0);
                    }
                    //Broadcast.
                    ChatWriter.write(ChatWriterType.VOTE,
                            "You have been invited into a vote.");
                    ChatWriter.write(ChatWriterType.VOTE, ChatColor.BOLD.toString() + ChatColor.GOLD + "Options:");
                    for (String sel : current.keySet()) {
                        ChatWriter.write(ChatWriterType.VOTE, sel);
                    }
                    ChatWriter.write(ChatWriterType.VOTE, ChatColor.BOLD.toString() + ChatColor.AQUA +
                            "Use " + ChatColor.YELLOW + "/vote <your selection>" + ChatColor.AQUA +
                            " to vote.");


                    return true;


                }
                if (args[0].equalsIgnoreCase("close")) {
                    if (!current.isEmpty()) {
                        current.clear();
                        ChatWriter.writeTo(sender, ChatWriterType.VOTE, "The current vote has been closed.");
                    } else {
                        sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "There is no vote ongoing."));
                    }
                    voted.clear();
                    return true;
                }
                {
                    if (current.isEmpty()) {
                        ChatWriter.writeTo(sender, ChatWriterType.VOTE, "There's no vote ongoing.");
                    } else {
                        // Vote for.
                        boolean vote = false;
                        if (!voted.contains(sender)) {
                            for (String key : current.keySet()) {
                                if (args[0].equalsIgnoreCase(key)) {
                                    current.put(key, current.get(key) + 1);
                                    vote = true;
                                    voted.add((Player) sender);
                                    ChatWriter.writeTo(sender, ChatWriterType.VOTE, "You voted for " + key);
                                    break;
                                }
                            }
                            if (!vote) sender.sendMessage(ChatColor.RED + "Couldn't find option: " + args[0]);
                            return true;
                        } else {
                            sender.sendMessage(ChatWriter.getMessage(ChatWriterType.VOTE, "You already voted!"));
                        }

                    }
                    return true;
                }
            }

        } else {
            return false;
        }

    }

}
