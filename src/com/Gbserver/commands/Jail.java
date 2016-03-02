package com.Gbserver.commands;

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

/**
 * Created by michael on 1/18/16.
 */
public class Jail implements CommandExecutor {
    public static List<OfflinePlayer> prisoners = new LinkedList<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length == 0){
            sender.sendMessage(ChatColor.DARK_BLUE + "Jail Usage");
            sender.sendMessage(ChatColor.BLUE + "/jail list");
            sender.sendMessage(ChatColor.BLUE + "/jail add " + ChatColor.RED.toString() + ChatColor.ITALIC + "Requires privileges");
            sender.sendMessage(ChatColor.BLUE + "/jail remove " + ChatColor.RED.toString() + ChatColor.ITALIC + "Requires privileges");
            return true;
        }
        switch(args[0]){
            case "list":
                sender.sendMessage(ChatColor.GOLD + "A list of active prisoners:");
                for(OfflinePlayer op : prisoners){
                    sender.sendMessage(ChatColor.YELLOW + op.getName());
                }
                break;
            case "add":
                if(isEligible(sender)){
                    if(args.length == 1){
                        sender.sendMessage(ChatColor.DARK_AQUA + "Add a name");
                    }else{
                        if(prisoners.contains(Bukkit.getOfflinePlayer(args[1]))){
                            sender.sendMessage("This person is already a prisoner");
                        }else{
                            prisoners.add(Bukkit.getOfflinePlayer(args[1]));
                            sender.sendMessage("Added, executing restriction operations...");
                            OfflinePlayer sel = Bukkit.getOfflinePlayer(args[1]);
                            EnhancedPlayer.getEnhanced(sel).setPermission(PermissionManager.Permissions.GUEST);
                            sel.setOp(false);
                            if(sel.isOnline()){
                                Bukkit.getPlayer(sel.getUniqueId()).sendMessage(ChatColor.BLUE + "Jail> " + ChatColor.GRAY + "You have been jailed.");
                            }
                            //mute / sandbox
                            sender.sendMessage("Set permission to GUEST and deopped");
                        }
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Insufficient privileges");
                }
                break;
            case "remove":
                if(isEligible(sender)){
                    if(args.length == 1){
                        sender.sendMessage(ChatColor.DARK_AQUA + "Add a name");
                    }else{
                        if(!prisoners.contains(Bukkit.getOfflinePlayer(args[1]))){
                            sender.sendMessage("This person is not a prisoner");
                        }else{
                            prisoners.remove(Bukkit.getOfflinePlayer(args[1]));
                            sender.sendMessage("Removed");
                        }
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Insufficient privileges");
                }
                break;
        }
        return true;
    }

    boolean isEligible(CommandSender sender) {
        return !(sender instanceof Player) ||
                EnhancedPlayer.getEnhanced((Player) sender).getPermission().isAbove(PermissionManager.Permissions.DEVELOPER);
    }

    public static final ConfigLoader.ConfigUser configUser = new ConfigLoader.ConfigUser() {
        public void unload() {
            HashMap<String, String> build = new HashMap<>();
            for (OfflinePlayer op : prisoners) {
                build.put(Identity.serializeIdentity(op), "");
            }
            ConfigManager.entries.put("Prisoners", build);
        }

        public void load() {
            prisoners.clear();
            HashMap<String, String> data = ConfigManager.smartGet("Prisoners");
            for (String str : data.keySet()) {
                prisoners.add(Identity.deserializeIdentity(str));
            }
        }
    };


}
