package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.CPrefix;
import com.Gbserver.variables.Sandbox;
import com.Gbserver.variables.Vault;
import com.Gbserver.variables.Vaults;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender) && Utilities.validateGamePlay(sender)) {
            Player player = (Player) sender;
            Vault v = Vaults.getVault(player.getUniqueId());
            if (v.previous != null) {
                player.teleport(v.previous);
                player.sendMessage(CPrefix.Prf.CONDITION + "Successfully teleported you to your original location.");
                v.previous = null;
            } else {
                player.sendMessage(CPrefix.Prf.COMMAND +
                        ChatColor.RED.toString()
                        + "This command is not available right now. Please try again after a respawn.");
            }

        }
        return true;
    }
}
