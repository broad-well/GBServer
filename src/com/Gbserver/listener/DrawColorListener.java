package com.Gbserver.listener;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.Gbserver.Main;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class DrawColorListener implements Listener{
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent pie){
		if (pie.getPlayer().getItemInHand().getType().equals(Material.WOOD_SWORD)
				&& pie.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Thin Brush")
				&& pie.getPlayer().getWorld().equals(Bukkit.getWorld("Drawing"))) {
			//Do select color.
			Block b;
			if((b = pie.getPlayer().getTargetBlock((Set<Material>) null, 100)).getType().equals(Material.STAINED_CLAY)){
				Main.paintColor = b.getData();
				pie.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You have chosen " + DyeColor.getByData(Main.paintColor).name() + "."));
			}
			
			
		}
	}
}
