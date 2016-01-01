package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ignore implements CommandExecutor {


    public static HelpTable ht = new HelpTable("/ignore <player to ignore>", "To ignore a certain player's chat messages", "", "ignore");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender)) {
            if (args.length == 0) {
                if (IgnoreList.getIgnoreList((Player) sender).getIgnoredPlayers().size() != 0) {
                    ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "A list of players ignored by you:");
                    for (OfflinePlayer op : IgnoreList.getIgnoreList((Player) sender).getIgnoredPlayers()) {
                        ChatWriter.writeTo(sender, ChatWriterType.COMMAND, op.getName());
                    }
                } else {
                    ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You are not ignoring anyone.");
                }
                return true;
            }
            OfflinePlayer p;
            p = Bukkit.getOfflinePlayer(args[0]);
            if (p == null || IgnoreList.getIgnoreList((Player) sender) == null) {
                ChatWriter.writeTo(sender, ChatWriterType.ERROR, "Null error. Either the player does not exist, or please rejoin.");
                return true;
            }
            IgnoreList il = IgnoreList.getIgnoreList((Player) sender);
            if (il.isIgnored(p)) {
                il.removeIgnoredPlayer(p);
                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You have un-ignored " + ChatColor.YELLOW + p.getName() + "'s " + ChatColor.GRAY + "chat messages.");
            } else {
                il.addIgnoredPlayer(p);
                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You have begun to ignore " + ChatColor.YELLOW + p.getName() + "'s " + ChatColor.GRAY + "chat messages.");
            }
            return true;
        } else {
            return true;
        }
    }
}
