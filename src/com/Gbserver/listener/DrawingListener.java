package com.Gbserver.listener;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DrawingListener implements Listener{
	
	
	public static World world = Bukkit.getWorld("Drawing");
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent pie){
		if(pie.getPlayer().getItemInHand().getType().equals(Material.WOOD_SWORD)
				&& pie.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Thin Brush")
				&& pie.getPlayer().getWorld().equals(world)
				&& pie.getPlayer().getTargetBlock((Set<Material>) null, 100).getType().equals(Material.WOOL)){
			Block b = pie.getPlayer().getTargetBlock((Set<Material>) null, 100);
			b.setData((byte) 15);
			Bukkit.broadcastMessage("Drawn");
		}
	}
}
