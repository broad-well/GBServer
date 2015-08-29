package com.Gbserver.variables;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.Gbserver.Main;

public class Chair {
	private Location loc;
	private Item bat;
	private Player p;
	private UUID id;
	public Chair(Location l, Player toSit){
		loc = l;
		bat = loc.getWorld().dropItem(loc, new ItemStack(Material.GOLD_NUGGET));
		p = toSit;
		Vector v = new Vector(0,0,0);
		bat.setPickupDelay(Integer.MAX_VALUE);
		bat.setVelocity(v);
		bat.setPassenger(p);
		id = UUID.randomUUID();
		Chairs.chairs.add(this);
		Chairs.bats.add(bat);
		Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

			@Override
			public void run() {
				bat.teleport(loc);
				
			}
			
		}, 60L);
		
	}
	
	public UUID getUUID() {
		return id;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public Item getBat() {
		return bat;
	}
	
	private void hideBat() {
		WitherSkull ws = loc.getWorld().spawn(loc, WitherSkull.class);
		Vector v = new Vector(0,0,0);
		ws.setDirection(v);
		ws.setVelocity(v);
		
	}
}
