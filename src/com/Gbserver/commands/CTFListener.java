package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class CTFListener implements Listener {
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent pie) {
		
		if (CTF.allPlayers().contains(pie.getPlayer()) && CTF.isRunning) {
			Bukkit.broadcastMessage("Interact");
			pie.setCancelled(true);
			// Valid.
			// Choices: INKSACK or SHEARS
			if (pie.getPlayer().getItemInHand().getType() == Material.INK_SACK
					&& pie.getRightClicked() instanceof Player) {
				// Deduct health by 20/3.
				((Player) pie.getRightClicked()).setHealth(((Player) pie.getRightClicked()).getHealth() - (20 / 3));
				return;
			}
			if (pie.getPlayer().getItemInHand().getType() == Material.SHEARS && pie.getRightClicked() instanceof Sheep) {
				// Make the flag ride on this player. On death, it teleports
				// back to the original location.
				// On passing boundary, wins!
				Bukkit.getScheduler().cancelTask(CTF.frozenid);
				pie.getPlayer().setPassenger(pie.getRightClicked());
				pie.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME,
						"You are now carrying the flag. Cross the team boundary to win!"));
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent pde){
		if(CTF.allPlayers().contains(pde.getEntity()) && CTF.isRunning){
			if(pde.getEntity().getPassenger() == CTF.blueFlag){
				// Blue flag needs to be in its original location.
				CTF.blueFlag.eject();
				pde.getEntity().eject();
				CTF.blueFlag.teleport(CTF.blueFlagLocation);
				CTF.frozenid = Utilities.setFrozen(CTF.blueFlag, CTF.redFlag);
				
			}
			pde.setDeathMessage(ChatWriter.getMessage(ChatWriterType.DEATH, 
					ChatColor.YELLOW + pde.getEntity().getName() + ChatColor.DARK_RED + " has been killed while trying to obtain the flag."));
		}
	}
}
