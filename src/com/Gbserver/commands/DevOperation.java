package com.Gbserver.commands;

import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.listener.Rank;
import com.Gbserver.variables.PermissionManager;
import com.Gbserver.variables.PermissionManager.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.util.UUID;

public class DevOperation implements CommandExecutor{


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(isEligible(commandSender)){
            if(strings.length == 0) return true;

            switch(strings[0]){
                case "InsertPermission":
                    if(strings.length < 3) return true;
                    Permissions perm = Permissions.valueOf(strings[2]);
                    if(perm == null) return true;
                    OfflinePlayer play = Bukkit.getOfflinePlayer(strings[1]);
                    if(play == null) return true;
                    PermissionManager.perms.put(play.getUniqueId(), perm);
                    commandSender.sendMessage("Permission inserted: " + play.getName() + " -> " + perm);
                    return true;
                case "InsertRank":
                    if(strings.length < 3) return true;
                    ChatFormatter.rankData.put(Bukkit.getOfflinePlayer(strings[1]).getUniqueId(), ChatFormatter.fromConfig(strings[2]));
                    try {
                        ChatFormatter.$export$();
                        commandSender.sendMessage("Rank inserted: " + Bukkit.getOfflinePlayer(strings[1]).getName()
                                + ", " + ChatFormatter.rankData.get(Bukkit.getOfflinePlayer(strings[1]).getUniqueId()).getPrefix());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "ListRank":
                    commandSender.sendMessage(ChatFormatter.rankData.toString());
                    break;
                case "GetUUID":
                    if(strings.length < 2) return true;
                    commandSender.sendMessage(Bukkit.getOfflinePlayer(strings[1]).getUniqueId().toString());
                    return true;
                case "GetName":
                    if(strings.length < 2) return true;
                    commandSender.sendMessage(Bukkit.getOfflinePlayer(UUID.fromString(strings[1])).getName());
                    return true;
                default:
                    commandSender.sendMessage("Bad option.");
                    return true;
            }
        }else{
            commandSender.sendMessage(ChatColor.RED + "You are not authorized to do this operation. Your permission: " +
                    PermissionManager.getPermission((Player) commandSender) + ", required above: "+
                    PermissionManager.Permissions.PRIVILEGED);
            return true;
        }
        return true;
    }

    public boolean isEligible(CommandSender sender) {
        return sender instanceof ConsoleCommandSender || sender instanceof Player && PermissionManager.getPermission((Player) sender).isAbove(Permissions.PRIVILEGED);
    }
}
