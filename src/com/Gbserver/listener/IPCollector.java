package com.Gbserver.listener;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ConfigLoader;
import com.Gbserver.variables.ConfigManager;
import com.Gbserver.variables.Identity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 12/13/15.
 */
public class IPCollector implements Listener{
    public static Path file = ConfigManager.getPathInsidePluginFolder("ips.dat");
    public static HashMap<OfflinePlayer, String> addresses = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent pje) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(), new Runnable() {
            public void run() {
                addresses.put(pje.getPlayer(),
                        pje.getPlayer().getAddress().getAddress().toString());
                try {
                    configUser.unload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10L);
    }

    public static final ConfigLoader.ConfigUser configUser = new ConfigLoader.ConfigUser() {


        public void unload() {
            HashMap<String, String> converted = new HashMap<>();
            for (Map.Entry<OfflinePlayer, String> entry : addresses.entrySet())
                converted.put(Identity.serializeIdentity(entry.getKey()), entry.getValue());
            //Converted and ready to use.
            ConfigManager.entries.put("IPs", converted);

        }

        public void load() {
            addresses.clear();
            for (Map.Entry<String, String> entry : ConfigManager.smartGet("IPs").entrySet()) {
                addresses.put(Identity.deserializeIdentity(entry.getKey()), entry.getValue());
            }
        }
    };
}
