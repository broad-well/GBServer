package com.Gbserver.menuLibrary;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Item {
	int index;
	ItemStack content;
	Inventory inventory;
	
	public Item(Inventory i, int index){
		this.inventory = i;
		this.index = index;
		content = inventory.getItem(index);
	}
	
	public ItemStack getItem() {
		return content;
	}
	
	public Inventory getHandle() {
		return inventory;
	}
}
