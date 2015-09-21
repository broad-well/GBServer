package com.Gbserver.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import com.Gbserver.commands.Lobby.LUtils;
import com.Gbserver.commands.Lobby.LVariables;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class LobbyListener implements Listener {
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent piee) {
		if (piee.getRightClicked().equals(LVariables.TFBlue)) {
			// Join Blue Team.
			if (LVariables.TFInBlue.contains(piee.getPlayer())) {
				piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME,
						"You are already in " + ChatColor.BLUE + "Blue Team"));
				return;
			}
			if (LVariables.TFInRed.contains(piee.getPlayer())) {
				if (LVariables.TFInBlue.size() > 0) {
					Player toSwap = LVariables.TFInBlue.get(0);
					LVariables.TFInBlue.remove(toSwap);
					toSwap.sendMessage(
							ChatWriter.getMessage(ChatWriterType.GAME, "Swapped you to " + ChatColor.RED + "Red Team"));
					piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You swapped teams with "
							+ toSwap.getName() + ". You are now in " + ChatColor.BLUE + " Blue Team"));
					LVariables.TFInRed.remove(piee.getPlayer());
					LVariables.TFInRed.add(toSwap);
					LVariables.TFInBlue.add(piee.getPlayer());
					LUtils.updateTFScoreboard();
					return;
				} else {
					piee.getPlayer().sendMessage(
							ChatWriter.getMessage(ChatWriterType.GAME, "You cannot be swapped to red. Please rejoin."));
					return;
				}
			}
			piee.getPlayer().sendMessage("I don't think you are in the game.");
		}
		
		if (piee.getRightClicked().equals(LVariables.TFRed)) {
			// Join Red Team.
			if (LVariables.TFInRed.contains(piee.getPlayer())) {
				piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME,
						"You are already in " + ChatColor.RED + "Red Team"));
				return;
			}
			if (LVariables.TFInBlue.contains(piee.getPlayer())) {
				if (LVariables.TFInRed.size() > 0) {
					Player toSwap = LVariables.TFInRed.get(0);
					LVariables.TFInRed.remove(toSwap);
					toSwap.sendMessage(
							ChatWriter.getMessage(ChatWriterType.GAME, "Swapped you to " + ChatColor.BLUE + "Blue Team"));
					piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You swapped teams with "
							+ toSwap.getName() + ". You are now in " + ChatColor.RED + " Red Team"));
					LVariables.TFInBlue.remove(piee.getPlayer());
					LVariables.TFInBlue.add(toSwap);
					LVariables.TFInRed.add(piee.getPlayer());
					LUtils.updateTFScoreboard();
					return;
				} else {
					piee.getPlayer().sendMessage(
							ChatWriter.getMessage(ChatWriterType.GAME, "You cannot be swapped to blue. Please rejoin."));
					return;
				}
			}
			piee.getPlayer().sendMessage("I don't think you are in the game.");
		}
		
		
		
		
		
		if (piee.getRightClicked().equals(LVariables.BLBlue)) {
			// Join Blue Team.
			if (LVariables.BLInBlue.contains(piee.getPlayer())) {
				piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME,
						"You are already in " + ChatColor.BLUE + "Blue Team"));
				return;
			}
			if (LVariables.BLInRed.contains(piee.getPlayer())) {
				if (LVariables.BLInBlue.size() > 0) {
					Player toSwap = LVariables.BLInBlue.get(0);
					LVariables.BLInBlue.remove(toSwap);
					toSwap.sendMessage(
							ChatWriter.getMessage(ChatWriterType.GAME, "Swapped you to " + ChatColor.RED + "Red Team"));
					piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You swapped teams with "
							+ toSwap.getName() + ". You are now in " + ChatColor.BLUE + " Blue Team"));
					LVariables.BLInRed.remove(piee.getPlayer());
					LVariables.BLInRed.add(toSwap);
					LVariables.BLInBlue.add(piee.getPlayer());
					LUtils.updateBLScoreboard();
					return;
				} else {
					piee.getPlayer().sendMessage(
							ChatWriter.getMessage(ChatWriterType.GAME, "You cannot be swapped to blue. Please rejoin."));
					return;
				}
			}
			piee.getPlayer().sendMessage("I don't think you are in the game.");
		}
		
		if (piee.getRightClicked().equals(LVariables.BLRed)) {
			// Join Red Team.
			if (LVariables.BLInRed.contains(piee.getPlayer())) {
				piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME,
						"You are already in " + ChatColor.RED + "Red Team"));
				return;
			}
			if (LVariables.BLInBlue.contains(piee.getPlayer())) {
				if (LVariables.BLInRed.size() > 0) {
					Player toSwap = LVariables.BLInRed.get(0);
					LVariables.BLInRed.remove(toSwap);
					toSwap.sendMessage(
							ChatWriter.getMessage(ChatWriterType.GAME, "Swapped you to " + ChatColor.BLUE + "Blue Team"));
					piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You swapped teams with "
							+ toSwap.getName() + ". You are now in " + ChatColor.RED + " Red Team"));
					LVariables.BLInBlue.remove(piee.getPlayer());
					LVariables.BLInBlue.add(toSwap);
					LVariables.BLInRed.add(piee.getPlayer());
					LUtils.updateBLScoreboard();
					return;
				} else {
					piee.getPlayer().sendMessage(
							ChatWriter.getMessage(ChatWriterType.GAME, "You cannot be swapped to red. Please rejoin."));
					return;
				}
			}
			piee.getPlayer().sendMessage("I don't think you are in the game.");
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent ede) {
		if (ede.getEntity().equals(LVariables.TFBlue) || ede.getEntity().equals(LVariables.TFRed)
				|| ede.getEntity().equals(LVariables.BLBlue) || ede.getEntity().equals(LVariables.BLRed)) {
			ede.setCancelled(true);
		}
	}
}
