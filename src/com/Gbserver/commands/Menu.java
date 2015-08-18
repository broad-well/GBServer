package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Menu implements CommandExecutor{
	
	private static final Object[] invDisplay = {
			"Go to Spawn", Material.GLOWSTONE,
			"Tag AFK", Material.PAINTING,
			"Back to location before death", Material.REDSTONE,
			"Name an animal", Material.NAME_TAG,
			"Ride an animal", Material.GOLD_BARDING,
			"Make an animal ride you", Material.DIAMOND_BARDING,
			"Switch gamemode", Material.REDSTONE_COMPARATOR,
			"Heal an animal", Material.POTION,
			"Turf Wars", Material.BOW
	};
	
	private Inventory getInventory(){
		Inventory i = Bukkit.createInventory(null, 9, "Commands");
		for(int a = 0; a < invDisplay.length - 1; a+=2){
			ItemStack is = new ItemStack((Material) invDisplay[a+1], 1);
			is = setDisplayName(((String) invDisplay[a]), is);
			i.setItem(a/2, is);
		}
		return i;
	}
	
	public static ItemStack setDisplayName(String name, ItemStack is) {
		ItemMeta m = is.getItemMeta();
		m.setDisplayName(name);
		is.setItemMeta(m);
		return is;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("menu")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Must be a player.");
				return false;
			}
			Player p = (Player) sender;
			p.openInventory(getInventory());
			return true;
		}
		return false;
	}
}
