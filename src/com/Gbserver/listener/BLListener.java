package com.Gbserver.listener;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.Gbserver.commands.BL;
import com.Gbserver.commands.Runner;

public class BLListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent pie) {
		if (BL.isRunning && BL.players.contains(pie.getPlayer().getName())) {
			Player p = pie.getPlayer();
			Vector v = p.getEyeLocation().getDirection();
			Entity fb = p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
			fb.setVelocity(v.multiply(2.4));
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent pme) {
		Player p = pme.getPlayer();
		if (BL.isRunning && BL.players.contains(p.getName())) {
			if (p.getLocation().getY() < 100) {
				p.damage(p.getHealth() - 3);
				
			}
			if (p.getLocation().getBlock().getType() == Material.WATER || p.getLocation().getBlock().getType() == Material.STATIONARY_WATER){
				p.damage(3);
				
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent pde){
		if(BL.isRunning && BL.players.contains(pde.getEntity().getName())){
			Player p = pde.getEntity();
			Entity[] items = new Entity[11];
			for(int i = 0; i < 10; i++){
				items[i] = p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.RED_ROSE));
				items[i].setVelocity(new Vector(getRandom(-3,3),getRandom(-3,3),3).multiply(0.1));
			}
			
			p.setHealth(p.getMaxHealth());
			p.teleport(new Location(p.getWorld(), 0, 200, 0));
			p.setGameMode(GameMode.SPECTATOR);
			BL.players.remove(p.getName());
			p.sendMessage(ChatColor.RED + "Good Game, Losers! :D");
			if(BL.players.size() == 1){
				BL.isRunning = false;
				BL.players.clear();
			}
		}
	}
	
	@EventHandler
	public void onProjectileHit(EntityDamageByEntityEvent edbee){
		if(BL.isRunning && BL.players.contains(edbee.getEntity().getName()) && edbee.getDamager() instanceof FallingBlock && edbee.getEntity() instanceof Player){
			Player p = (Player) edbee.getEntity();
			p.setHealth(0);
		}
	}
	
	private int getRandom(int min, int max) {
		Random rand = new Random();

		return rand.nextInt(max) + min;
		// 50 is the maximum and the 1 is our minimum
	}
	
	/*private Vector getVector(double pitch, double yaw, double multiplier){
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);
		return new Vector(x,z,y).multiply(multiplier);
	}*/
}
