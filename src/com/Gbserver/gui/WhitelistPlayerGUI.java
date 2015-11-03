package com.Gbserver.gui;


import com.Gbserver.utils.WhitelistUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.WHITE;

public class WhitelistPlayerGUI implements Listener {

    private static WhitelistPlayerGUI instance = new WhitelistPlayerGUI();

    public WhitelistPlayerGUI() {

    }

    public static WhitelistPlayerGUI getInstance() {
        return instance;
    }


    public void openGUI(Player player){
        final Player p = player;

        AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler(){
            @Override
            public void onAnvilClick(AnvilGUI.AnvilClickEvent event){
                if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                    event.setWillClose(true);
                    event.setWillDestroy(true);

                    if (event.getName() != null) {
                        WhitelistUtil.getInstance().addPlayerToWhitelist(event.getName(), p);
                    }
                } else {
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                }
            }
        });

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, new ItemStack(Material.NAME_TAG));

        gui.open();

        //Inventory inv = Bukkit.createInventory(null, InventoryType.ANVIL, DARK_AQUA + "Whitelist Player");

        //player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Whitelist Manager")) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) {
            player.closeInventory();
            return;
        }

        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Whitelist Player")) {
            player.closeInventory();
            WhitelistUtil.getInstance().whitelistAll();
        }

        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Clear Whitelist")) {
            player.closeInventory();
            WhitelistUtil.getInstance().clearWhitelist();
        }

        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Enable Whitelist")) {
            player.closeInventory();
            WhitelistUtil.getInstance().enableWhitelist(player);
        }

        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Disable Whitelist")) {
            player.closeInventory();
            WhitelistUtil.getInstance().disableWhitelist(player);
        }

    }


}
