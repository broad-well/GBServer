package com.Gbserver.gui;



import com.embryopvp.arenapvp.utils.WhitelistUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import static org.bukkit.ChatColor.*;

public class WhitelistGUI implements Listener {

    private static WhitelistGUI instance = new WhitelistGUI();

    public WhitelistGUI() {

    }

    public static WhitelistGUI getInstance() {
        return instance;
    }


    public void openGUI(Player player){
        Inventory inv = Bukkit.createInventory(null, 9, DARK_AQUA + "Whitelist Manager");


        ItemStack whitelistAll = new ItemStack(Material.QUARTZ_BLOCK, 1);
        ItemMeta whitelistAllMeta = whitelistAll.getItemMeta();

        whitelistAllMeta.setDisplayName(WHITE + "Whitelist All");
        whitelistAll.setItemMeta(whitelistAllMeta);

        ItemStack unWhitelistAll = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta unWhitelistAllMeta = unWhitelistAll.getItemMeta();

        unWhitelistAllMeta.setDisplayName(WHITE + "Clear Whitelist");
        unWhitelistAll.setItemMeta(unWhitelistAllMeta);

        ItemStack enableWhitelist = new ItemStack(Material.STAINED_CLAY, 1, (short) 13);
        ItemMeta enableWhitelistMeta = enableWhitelist.getItemMeta();

        ItemStack whitelistAddSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta whitelistAddSkullMeta = (SkullMeta) whitelistAddSkull.getItemMeta();

        whitelistAddSkullMeta.setDisplayName(WHITE + "Whitelist Player");
        whitelistAddSkull.setItemMeta(whitelistAddSkullMeta);

        ItemStack whitelistRemoveSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta whitelistRemoveSkullMeta = (SkullMeta) whitelistRemoveSkull.getItemMeta();

        whitelistRemoveSkullMeta.setDisplayName(WHITE + "Remove Whitelisted Players");
        whitelistRemoveSkull.setItemMeta(whitelistRemoveSkullMeta);

        enableWhitelistMeta.setDisplayName(WHITE + "Enable Whitelist");
        enableWhitelist.setItemMeta(enableWhitelistMeta);

        ItemStack disableWhitelist = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
        ItemMeta disableWhitelistMeta = enableWhitelist.getItemMeta();

        disableWhitelistMeta.setDisplayName(WHITE + "Disable Whitelist");
        disableWhitelist.setItemMeta(disableWhitelistMeta);

        inv.setItem(0, whitelistAll);
        inv.setItem(1, unWhitelistAll);
        inv.setItem(3, whitelistAddSkull);
        inv.setItem(4, whitelistRemoveSkull);
        inv.setItem(7, enableWhitelist);
        inv.setItem(8, disableWhitelist);

        player.openInventory(inv);
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

        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Whitelist All")) {
            player.closeInventory();
            WhitelistUtil.getInstance().whitelistAll();
        }

        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Clear Whitelist")) {
            player.closeInventory();
            WhitelistUtil.getInstance().clearWhitelist();
        }

        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Whitelist Player")) {
            player.closeInventory();
            WhitelistPlayerGUI.getInstance().openGUI(player);
        }

        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Remove Whitelisted Players")) {
            player.closeInventory();
            WhitelistRemovePlayersGUI.getInstance().openGUI(player);
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
