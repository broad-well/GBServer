package com.Gbserver.commands;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class Heal implements CommandExecutor {
    public static List<Player> players = new LinkedList<Player>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("heal")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
                return false;
            }
            players.add((Player) sender);
            sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND,
                    ChatColor.ITALIC + "Please click on a living entity to heal it."));
            return true;

        }
        return false;
    }

}
