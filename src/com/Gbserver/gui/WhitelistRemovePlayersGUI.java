package com.Gbserver.gui;


import com.Gbserver.utils.WhitelistUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Set;

import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.WHITE;

public class WhitelistRemovePlayersGUI implements Listener {

    private static WhitelistRemovePlayersGUI instance = new WhitelistRemovePlayersGUI();

    public WhitelistRemovePlayersGUI() {

    }

    public static WhitelistRemovePlayersGUI getInstance() {
        return instance;
    }


    public void openGUI(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, DARK_AQUA + "Remove Whitelisted Players");

        int j = Bukkit.getWhitelistedPlayers().size();
        for (int i = 0; i < j; i++)
        {
            for (OfflinePlayer p : Bukkit.getWhitelistedPlayers()){
                ItemStack whitelistedPlayerSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta whitelistedPlayerSkullMeta = (SkullMeta) whitelistedPlayerSkull.getItemMeta();

                whitelistedPlayerSkullMeta.setOwner(p.getName());
                whitelistedPlayerSkullMeta.setDisplayName(WHITE + p.getName());
                whitelistedPlayerSkull.setItemMeta(whitelistedPlayerSkullMeta);

                inv.setItem(i, whitelistedPlayerSkull);
                if (i < inv.getSize()) {
                    i++;
                }
            }

        }


        /*for (Player p : Bukkit.getOnlinePlayers()) {
        }
        ItemStack whitelistedPlayerSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta whitelistedPlayerSkullMeta = (SkullMeta) whitelistedPlayerSkull.getItemMeta();

        whitelistedPlayerSkullMeta.setDisplayName(WHITE + "Whitelist Player");
        whitelistedPlayerSkull.setItemMeta(whitelistedPlayerSkullMeta);*/


        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Remove Whitelisted Players")) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) {
            player.closeInventory();
            return;
        }

        if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
            String playerToRemove = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            player.closeInventory();
            WhitelistUtil.getInstance().removePlayerFromWhitelist(playerToRemove, player);
        }
    }


}
