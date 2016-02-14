package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {
    public static boolean CommandAvailable = false;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender) && Utilities.validateGamePlay(sender)) {
            Player player = (Player) sender;
            Vault v = Vaults.getVault(player.getUniqueId());
            if (v.hasPrevious()) {
                v.toPrevious();
                player.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Successfully teleported " + player.getName() + " to his/her original location."));
                v.setPrevious(null);
            } else {
                player.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND,
                        ChatColor.RED + "This command is not available right now. Please try again after a respawn."));
            }

        }
        return true;
    }
}
