package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.CPrefix;
import com.Gbserver.variables.EnhancedPlayer;
import com.Gbserver.variables.PermissionManager;
import com.Gbserver.variables.Sandbox;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Attentions implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if(Sandbox.check(arg0)) return true;
        if ((arg0 instanceof Player && EnhancedPlayer.getEnhanced((Player) arg0).getPermission().isAbove(PermissionManager.Permissions.GUEST))
                || arg0 instanceof ConsoleCommandSender)
            Bukkit.broadcastMessage(CPrefix.Prf.ANNOUNCEMENT + Utilities.concat(arg3));
        else
            arg0.sendMessage(CPrefix.Prf.CONDITION + "You are not authorized to perform this action.");

        return true;
    }


}
