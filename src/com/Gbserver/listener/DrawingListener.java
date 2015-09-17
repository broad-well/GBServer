package com.Gbserver.listener;

import java.util.Set;

import org.bukkit.Bukkit;

import org.bukkit.DyeColor;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.Gbserver.variables.ChatWriter;

public class DrawingListener implements Listener {

	public static World world = Bukkit.getWorld("Drawing");

	@EventHandler
	public void onPlayerUse(PlayerInteractEvent pie) {
		Block b;
		if (pie.getPlayer().getItemInHand().getType().equals(Material.WOOD_SWORD)
				&& pie.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Thin Brush")
				&& pie.getPlayer().getWorld().equals(world)
				&& (b = pie.getPlayer().getTargetBlock((Set<Material>) null, 100)).getType().equals(Material.WOOL)) {
			//Do draw.
			b.setData(DyeColor.BLACK.getData());
			Bukkit.broadcastMessage("Drawn");
		}
	}
}

