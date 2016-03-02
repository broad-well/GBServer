package com.Gbserver.listener.hiddencmds;

import com.Gbserver.variables.EnhancedPlayer;
import com.Gbserver.variables.PermissionManager;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class JoinGame implements CmdListener.CmdModule {
    @Override
    public String GetLabel() {
        return "CBlockJoinGame";
    }

    @Override
    public int Execute(String cmd, CommandSender sender) {

        return 0;
    }

    @Override
    public boolean IsEligible(CommandSender cs) {
        if (cs instanceof Player) {
            return EnhancedPlayer.getEnhanced((Player) cs).getPermission().isAbove(PermissionManager.Permissions.DEVELOPER);
        } else return cs instanceof ConsoleCommandSender || cs instanceof CommandBlock;
    }
}
