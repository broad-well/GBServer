package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class Ride implements CommandExecutor {
    public static List<Player> list = new LinkedList<Player>();
    public static boolean ridingOthers = false;
    public static boolean hasPassenger = false;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Utilities.validateSender(sender) && Utilities.validateGamePlay(sender)) {
            if (label.equalsIgnoreCase("ride")) {
                ridingOthers = true;
                list.add((Player) sender);
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, ChatColor.ITALIC + "Please right click an entity to ride on it."));
                return true;
            }
            if (label.equalsIgnoreCase("rideme")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
                    return false;
                }


                if (hasPassenger) {
                    ((Player) sender).eject();
                    sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, ChatColor.ITALIC + "Successfully dismounted your passenger."));
                    hasPassenger = false;
                } else {
                    ridingOthers = false;
                    list.add((Player) sender);
                    sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, ChatColor.ITALIC + "Please right click an entity to make it ride on you."));
                }
                return true;
            }
        }
        return false;
    }
}
