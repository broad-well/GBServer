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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mute implements CommandExecutor {
    public static List<OfflinePlayer> list = new LinkedList<>();
    private HelpTable ht = new HelpTable("/mute <player to mute>", "/mute is used for general muting funtionality.", "", "mute");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if(Utilities.validateSender(sender) &&
                EnhancedPlayer.getEnhanced((Player) sender).getPermission().isAbove(PermissionManager.Permissions.DEVELOPER)) {
            if (args.length == 0) {
                ht.show(sender);
                return true;
            }
            if (list.contains(Bukkit.getPlayer(args[0]))) {
                list.remove(Bukkit.getPlayer(args[0]));
                Bukkit.getPlayer(args[0]).sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You have been unmuted."));
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Removed " + Bukkit.getPlayer(args[0]).getName()) + " from muted list.");
            } else {
                list.add(Bukkit.getPlayer(args[0]));
                Bukkit.getPlayer(args[0]).sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You have been muted."));
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Added " + Bukkit.getPlayer(args[0]).getName()) + " from muted list.");
            }
        }else{
            sender.sendMessage(ChatColor.RED + "Mute operation aborted, you are not eligible.");
        }
            return true;
    }

    public static void inport() {
        list.clear();
        for(Map.Entry<String, String> entry : ConfigManager.smartGet("Mute").entrySet()){
            list.add(Identity.deserializeIdentity(entry.getKey()));
        }
    }

    public static void export() {
        HashMap<String, String> build = new HashMap<>();
        for(OfflinePlayer op : list){
            build.put(Identity.serializeIdentity(op), "");
        }
        ConfigManager.entries.put("Mute", build);
    }
}
