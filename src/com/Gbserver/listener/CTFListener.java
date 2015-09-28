package com.Gbserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.commands.CTF;
import com.Gbserver.commands.Team;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class CTFListener implements Listener {
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent pie) {

		if (CTF.allPlayers().contains(pie.getPlayer()) && CTF.isRunning) {
			pie.setCancelled(true);
			// Valid.
			// Choices: INKSACK or SHEARS
			if (pie.getPlayer().getItemInHand().getType() == Material.INK_SACK
					&& pie.getRightClicked() instanceof Player) {
				// Deduct health by 20/3.
				Player p = (Player) pie.getRightClicked();
				if (CTF.getOriginatedTeam(p) != CTF.getLocationTeam(p)) {
					p.damage(20 / 3);
				}
				return;
			}
			if (pie.getPlayer().getItemInHand().getType() == Material.SHEARS
					&& pie.getRightClicked() instanceof Sheep) {
				// Make the flag ride on this player. On death, it teleports
				// back to the original location.
				// On passing boundary, wins!
				Sheep s = (Sheep) pie.getRightClicked();
				if (s.getColor() == DyeColor.BLUE && CTF.getOriginatedTeam(pie.getPlayer()) == Team.RED) {
					Bukkit.getScheduler().cancelTask(CTF.frozenblue);
					pie.getPlayer().setPassenger(pie.getRightClicked());
					pie.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME,
							"You are now carrying the flag. Cross the team boundary to win!"));
					return;
				}

				if (s.getColor() == DyeColor.RED && CTF.getOriginatedTeam(pie.getPlayer()) == Team.BLUE) {
					Bukkit.getScheduler().cancelTask(CTF.frozenred);
					pie.getPlayer().setPassenger(pie.getRightClicked());
					pie.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME,
							"You are now carrying the flag. Cross the team boundary to win!"));
					return;
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent pde) {
		if (CTF.allPlayers().contains(pde.getEntity()) && CTF.isRunning) {
			pde.setKeepInventory(true);
			if (pde.getEntity().getPassenger() == CTF.blueFlag) {
				// Blue flag needs to be in its original location.
				pde.getEntity().eject();
				CTF.blueFlag.teleport(CTF.blueFlagLocation);
				CTF.frozenblue = Utilities.setFrozen(CTF.blueFlag);

			}

			if (pde.getEntity().getPassenger() == CTF.redFlag) {
				// Red flag needs to be in its original location.
				pde.getEntity().eject();
				CTF.redFlag.teleport(CTF.redFlagLocation);
				CTF.frozenred = Utilities.setFrozen(CTF.redFlag);
			}
			// Respawn after 10 seconds
			pde.setDeathMessage(ChatWriter.getMessage(ChatWriterType.DEATH, ChatColor.YELLOW + pde.getEntity().getName()
					+ ChatColor.DARK_RED + " has been killed while trying to obtain the flag."));
			ChatWriter.writeTo(pde.getEntity(), ChatWriterType.GAME, "Respawning in 10 seconds...");
			pde.getEntity().setHealth(20);
			pde.getEntity().setGameMode(GameMode.SPECTATOR);
			pde.getEntity().teleport(CTF.spectate);
			Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
				public void run() {
					pde.getEntity().teleport(CTF.getSpawn(CTF.getOriginatedTeam(pde.getEntity())));
					pde.getEntity().setGameMode(GameMode.SURVIVAL);
					ChatWriter.writeTo(pde.getEntity(), ChatWriterType.GAME, "You have been respawned.");
				}
			}, 200L);
		}

	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent ede) {
		if (ede.getEntity() == CTF.blueFlag || ede.getEntity() == CTF.redFlag) {
			ede.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent pme) {
		if (CTF.allPlayers().contains(pme.getPlayer()) && CTF.isRunning) {
			if (pme.getPlayer().getLocation().getY() < 0 && CTF.isRunning
					&& CTF.allPlayers().contains(pme.getPlayer())) {
				pme.getPlayer().teleport(CTF.getSpawn(CTF.getOriginatedTeam(pme.getPlayer())));
			}
			if (pme.getPlayer().getLocation().getY() > 90 && pme.getPlayer().getLocation().getBlockZ() == 0) {
				// This is where the game ends.
				if (pme.getPlayer().getPassenger() == CTF.blueFlag || pme.getPlayer().getPassenger() == CTF.redFlag) {

					ChatWriter.write(ChatWriterType.GAME, Team.toString(CTF.getOriginatedTeam(pme.getPlayer()))
							+ ChatColor.YELLOW + " has won the game!");
					CTF.stopGame();
				} else {
					ChatWriter.writeTo(pme.getPlayer(), ChatWriterType.GAME, "You are crossing the team boundary.");
					return;
				}

			}
		}
	}
}
