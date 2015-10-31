package com.Gbserver.commands;

import com.Gbserver.variables.PermissionManager;
import com.Gbserver.variables.PermissionManager.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Created by michael on 10/31/15.
 */
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
                    Player play = Bukkit.getPlayer(strings[1]);
                    if(play == null) return true;
                    PermissionManager.perms.put(play.getUniqueId(), perm);
                    commandSender.sendMessage("Permission inserted: " + play.getName() + " -> " + perm);
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
    }

    public boolean isEligible(CommandSender sender) {
        if(sender instanceof ConsoleCommandSender) return true;
        if(!(sender instanceof Player)) return false;
        if(PermissionManager.getPermission((Player) sender).isAbove(PermissionManager.Permissions.PRIVILEGED)){
            return true;
        }else{
            return false;
        }
    }
}
