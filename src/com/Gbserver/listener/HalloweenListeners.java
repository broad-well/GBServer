package com.Gbserver.listener;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
    public void onPlayerJoin(PlayerJoinEvent pje) {
        if (Main.isHalloween) {
            if (!Utilities.isInRangeOf(
                    HALLO_HIGH, HALLO_LOW, pje.getPlayer().getLocation())) {
                pje.getPlayer().setBedSpawnLocation(HALLO_SPAWN);
                pje.getPlayer().kickPlayer("You are not in the Halloween area. Please rejoin.");
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent pje) {
        if (Main.isHalloween) {
            if (!Utilities.isInRangeOf(
                    HALLO_HIGH, HALLO_LOW, pje.getPlayer().getLocation())) {
                pje.getPlayer().setBedSpawnLocation(HALLO_SPAWN);
                pje.getPlayer().kickPlayer("You cannot be outside of the Halloween area. Please rejoin.");
            }
        }
    }
}
