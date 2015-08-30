package com.Gbserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.Gbserver.commands.TF;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class TFListeners implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent ice) {
		if (TF.clickInventory) {
			ice.setCancelled(true);
			switch (ice.getSlot()) {
			case 0:
				// Join Red
				if (ice.getWhoClicked() instanceof Player) {
					Player p = (Player) ice.getWhoClicked();
					if (TF.bluePlayers.contains(p)) {
						TF.bluePlayers.remove(p);
					}
					if (!(TF.redPlayers.contains(p))) {
						TF.redPlayers.add(p);
						p.closeInventory();
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Successfully added you to " + ChatColor.RED + "Red Team"));
					} else {
						p.closeInventory();
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are already on Team Red!"));
					}
				}
				break;
			case 1:
				// Join Blue
				if (ice.getWhoClicked() instanceof Player) {
					Player p = (Player) ice.getWhoClicked();
					if (TF.redPlayers.contains(p)) {
						TF.redPlayers.remove(p);
					}
					if (!(TF.bluePlayers.contains(p))) {
						TF.bluePlayers.add(p);
						p.closeInventory();
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Successfully added you to " + ChatColor.BLUE + "Blue Team"));
					} else {
						p.closeInventory();
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are already on Team Blue!"));
					}
				}
				break;
			case 7:
				// Quit Game
				if (ice.getWhoClicked() instanceof Player) {
					Player p = (Player) ice.getWhoClicked();
					if (TF.bluePlayers.contains(p)) {
						TF.bluePlayers.remove(p);
					}
					if (TF.redPlayers.contains(p)) {
						TF.redPlayers.remove(p);
					}

					p.closeInventory();
					p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Successfully removed you from the game."));
				}
				break;
			case 8:
				if (ice.getWhoClicked() instanceof Player) {
					((Player) ice.getWhoClicked()).closeInventory();
				}
				break;
			}
			TF.clickInventory = false;
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent bpe) {
		if (TF.isRunning && TF.getAllPlayers().contains(bpe.getPlayer())) {
			if (TF.redPlayers.contains(bpe.getPlayer())) {
				// Red

				if (!isValid(bpe.getBlockPlaced().getLocation(), true)) {
					bpe.setCancelled(true);
					bpe.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are only allowed to build on red clay."));
				}
			} else {
				// Blue

				if (!isValid(bpe.getBlockPlaced().getLocation(), false)) {
					bpe.setCancelled(true);
					bpe.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are only allowed to build on blue clay."));
				}
			}
			if(bpe.getBlockPlaced().getY() > 110){
				bpe.setCancelled(true);
				bpe.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, ChatColor.BOLD + "Max build height is 10 blocks."));
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent pme) {
		if (TF.isRunning && TF.getAllPlayers().contains(pme.getPlayer())) {
			boolean isRed;
			if (TF.redPlayers.contains(pme.getPlayer())) {
				isRed = true;
			} else {
				isRed = false;
			}
			if (!isValid(pme.getPlayer().getLocation(), isRed)) {
				Vector v = new Vector(-10, 5, 0);
				if(isRed){
					pme.getPlayer().setVelocity(v.multiply(0.2));
					pme.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are only allowed on red clay."));
				}else{
					Vector blue = new Vector(10, 5, 0);
					pme.getPlayer().setVelocity(blue.multiply(0.2));
					pme.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are only allowed on blue clay."));
				}
				
				
			}
			if(pme.getPlayer().getLocation().getY() < 50){
				if(TF.redPlayers.contains(pme.getPlayer())){
					pme.getPlayer().teleport(new Location(pme.getPlayer().getWorld(),TF.RedSpawn[0],TF.RedSpawn[1],TF.RedSpawn[2]));
				
				}else{
					pme.getPlayer().teleport(new Location(pme.getPlayer().getWorld(),TF.BlueSpawn[0],TF.BlueSpawn[1],TF.BlueSpawn[2]));
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent bpe){
		if (TF.isRunning && TF.getAllPlayers().contains(bpe.getPlayer())) {
			if(bpe.getBlock().getY() == 100){
				bpe.setCancelled(true);
				return;
			}
			if(TF.redPlayers.contains(bpe.getPlayer()) && bpe.getBlock().getData() == TF.BLUE_CLAY){
				bpe.setCancelled(true);
				return;
			}
			if(TF.bluePlayers.contains(bpe.getPlayer()) && bpe.getBlock().getData() == TF.RED_CLAY){
				bpe.setCancelled(true);
				return;
			}
			
		}
	}

	@EventHandler
	public void dmg(EntityDamageEvent ede) {
		if (TF.isRunning) {
			if (ede.getEntity() instanceof Player) {
				if (TF.getAllPlayers().contains(((Player) ede.getEntity()))) {
					if (!(ede.getCause().equals(DamageCause.PROJECTILE))) {
						ede.setCancelled(true);
					} else {
						((Player) ede.getEntity()).damage(((Player) ede.getEntity()).getMaxHealth());
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent pde) {
		if (TF.isRunning && TF.getAllPlayers().contains(pde.getEntity())) {
			pde.setKeepInventory(true);
			pde.getEntity().setHealth(pde.getEntity().getMaxHealth());
			if (TF.redPlayers.contains(pde.getEntity())) {
				TF.advanceBlocks(false);
				pde.getEntity().teleport(
						new Location(Bukkit.getWorld("Turf_Wars1"), TF.RedSpawn[0], TF.RedSpawn[1], TF.RedSpawn[2]));
			} else {
				TF.advanceBlocks(true);
				pde.getEntity().teleport(
						new Location(Bukkit.getWorld("Turf_Wars1"), TF.BlueSpawn[0], TF.BlueSpawn[1], TF.BlueSpawn[2]));
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean isValid(Location b, boolean isRed) {
		double y = b.getY();
		Location l = new Location(Bukkit.getWorld("Turf_Wars1"),b.getX(),100,b.getZ());
		if(isRed && l.getBlock().getData() == TF.BLUE_CLAY && l.getBlock().getType() == Material.STAINED_CLAY){
			return false;
		}
		if(!isRed && l.getBlock().getData() == TF.RED_CLAY && l.getBlock().getType() == Material.STAINED_CLAY){
			return false;
		}
		return true;

	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent phe) {
		if (TF.isRunning && phe.getEntity() instanceof Arrow
				&& TF.getAllPlayers().contains(((Player) phe.getEntity().getShooter()))) {
			Arrow a = (Arrow) phe.getEntity();
			Location l = a.getLocation();
			Vector v = a.getVelocity().normalize();
			BlockIterator bi = new BlockIterator(l.getWorld(), l.toVector(), v, 0, 3);
			Block b = null;
			while (bi.hasNext()) {
				b = bi.next();
				if (b.getType() != Material.AIR) {
					break;
				}
			}
			if (b.getY() > 100 && b.getX() < 30 && b.getX() > -51 && b.getZ() > -25 && b.getZ() < 26) {
				b.breakNaturally();
			}
		}
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent ple){
		Player p = (Player) ple.getEntity().getShooter();
		if(TF.isRunning && TF.isBuildtime && TF.getAllPlayers().contains(p)){
			ple.setCancelled(true);
			
			p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are not allowed to launch arrows during Build Time!"));
		}
	}
	
	public static void fill(int[] down, int[] up, Material m, byte data, World w){
		//[] = {x,y,z}
		for(int x = down[0]; x <= up[0]; x++){
			for(int y = down[1]; x <= up[1]; x++){
				for(int z = down[2]; x <= up[2]; x++){
					Location l = new Location(w,x,y,z);
					l.getBlock().setType(m);
					l.getBlock().setData(data);
				}
			}
		}
	}
	
	
}
