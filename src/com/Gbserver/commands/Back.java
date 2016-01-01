package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import com.Gbserver.variables.Vaults;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {
    public static boolean CommandAvailable = false;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Utilities.validateSender(sender) && Utilities.validateGamePlay(sender)) {
            if(Sandbox.check(sender)) return true;
            Player player = (Player) sender;
            if (Vaults.getVault(player.getUniqueId()).hasPrevious()) {
                Vaults.getVault(player.getUniqueId()).toPrevious();
                player.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Successfully teleported " + player.getName() + " to his/her original location."));
                Vaults.getVault(player.getUniqueId()).setPrevious(null);
                return true;
            } else {
                player.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND,
                        ChatColor.RED + "This command is not available right now. Please try again after a respawn."));
                return false;
            }

        }
        return false;
    }
}
