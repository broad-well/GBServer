package com.Gbserver.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class runnerListenerDepricated implements Listener{
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent pme) {
		Player player = pme.getPlayer();
		World world = player.getWorld();
		if(Runner.isRunner(player) && Runner.isRunning){
			Location loc = player.getLocation();
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			y--;
			Location newloc = new Location(world,x,y,z);
			Material block;
			byte data;
			if(newloc.getBlock().getType() != Material.AIR){
				try{
					Thread.sleep(130);
					block = newloc.getBlock().getType();
					data = newloc.getBlock().getData();
					newloc.getBlock().setType(Material.AIR);
					world.spawnFallingBlock(newloc,block,data);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
