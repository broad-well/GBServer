package com.Gbserver.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.listener.BackListeners;
import com.Gbserver.variables.Vaults;

public class Back implements CommandExecutor {
	public static boolean CommandAvailable = false;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("back")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can die and get back.");
				return false;
			}
			Player player = (Player) sender;
			if (Vaults.getVault(player.getUniqueId()).hasPrevious()) {
				Vaults.getVault(player.getUniqueId()).toPrevious();
				player.sendMessage("Successfully teleported " + player.getName() + " to his/her original location.");
				Vaults.getVault(player.getUniqueId()).setPrevious(null);
				return true;
			} else {
				player.sendMessage(
						ChatColor.RED + "This command is not available right now. Please try again after a respawn.");
				return false;
			}

		}
		return false;
	}
}
