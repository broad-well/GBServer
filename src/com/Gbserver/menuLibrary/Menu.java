package com.Gbserver.menuLibrary;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Menu {
    private Inventory inventory;
    private String name;

    public Menu(String arg0, int index) {
        inventory = Bukkit.createInventory(null, index, arg0);
        name = arg0;
    }

    public void addItem(Material item, int count) {
        ItemStack is = new ItemStack(item, count);
        inventory.addItem(is);
    }

    public void addItem(Material item, int count, String name) {
        ItemStack is = new ItemStack(item, count);
        ItemMeta m = is.getItemMeta();
        m.setDisplayName(name);
        is.setItemMeta(m);
        inventory.addItem(is);
    }

    public void addItem(Material item, int count, String name, List<String> lore) {
        ItemStack is = new ItemStack(item, count);
        ItemMeta m = is.getItemMeta();
        m.setDisplayName(name);
        m.setLore(lore);
        is.setItemMeta(m);
        inventory.addItem(is);
    }
}
