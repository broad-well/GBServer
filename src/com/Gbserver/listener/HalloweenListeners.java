package com.Gbserver.listener;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * 427 126 1383
 * 461.5446579925818,64.0,1293.6290218195445
 * 576 30 1201
 */
public class HalloweenListeners implements Listener{
    public static final Location HALLO_HIGH = new Location(Bukkit.getWorld("world"), 427, 126, 1383);
    public static final Location HALLO_LOW = new Location(Bukkit.getWorld("world"), 576, 30, 1201);
    public static final Location HALLO_SPAWN = new Location(Bukkit.getWorld("world"), 461, 64, 1293);

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent pje) {
        if (Main.isHalloween) {
            if (!Utilities.isInRangeOf(
                    HALLO_HIGH, HALLO_LOW, pje.getPlayer().getLocation())) {
                pje.getPlayer().teleport(HALLO_SPAWN);
            }

            if(!pje.getPlayer().isOp()) {
                pje.getPlayer().setGameMode(GameMode.ADVENTURE);
            }

            if(!pje.getPlayer().getInventory().contains(Material.PUMPKIN_PIE)) {
                ChatWriter.writeTo(pje.getPlayer(), ChatWriterType.EVENT, ChatColor.BOLD.toString() + "Welcome to the Halloween party!");
                ChatWriter.writeTo(pje.getPlayer(), ChatWriterType.EVENT, "Have some complimentary Pumpkin Pie!");
                Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                        pje.getPlayer().getInventory().addItem(new ItemStack(Material.PUMPKIN_PIE, 64));
                        pje.getPlayer().getInventory().addItem(new ItemStack(Material.PUMPKIN_PIE, 64));
                        pje.getPlayer().getInventory().addItem(new ItemStack(Material.PUMPKIN_PIE, 64));
                    }
                }, 20L);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent pje) {
        if (Main.isHalloween) {
            if (!Utilities.isInRangeOf(
                    HALLO_HIGH, HALLO_LOW, pje.getPlayer().getLocation())) {
                pje.getPlayer().teleport(HALLO_SPAWN);
                pje.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "You are not supposed to be out of the Halloween area.");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent bbe){
        if (Main.isHalloween) {
            if (!Utilities.isInRangeOf(
                    HALLO_HIGH, HALLO_LOW, bbe.getPlayer().getLocation())
                    || !bbe.getPlayer().isOp()) {
               bbe.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent bbe){
        if (Main.isHalloween) {
            if (!Utilities.isInRangeOf(
                    HALLO_HIGH, HALLO_LOW, bbe.getPlayer().getLocation())
                    || !bbe.getPlayer().isOp()) {
                bbe.setCancelled(true);
            }
        }
    }
}
