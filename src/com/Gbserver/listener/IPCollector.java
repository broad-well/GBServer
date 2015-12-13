package com.Gbserver.listener;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ConfigManager;
import com.Gbserver.variables.Identity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by michael on 12/13/15.
 */
public class IPCollector implements Listener{
    public static Path file = ConfigManager.getPathInsidePluginFolder("ips.dat");
    public static HashMap<UUID, String> addresses = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent pje){
        Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(), new Runnable() {
            public void run() {
               addresses.put(pje.getPlayer().getUniqueId(),
                       pje.getPlayer().getAddress().getAddress().toString());
                outFlush();
            }
        }, 10L);
    }

    public static boolean outFlush() {
        try{
            String output = "";
            for(Map.Entry<UUID, String> entry : addresses.entrySet()){
                output += Identity.serializeIdentity(Bukkit.getOfflinePlayer(entry.getKey())) + "<->" +
                        entry.getValue() + "\n";
            }
            Files.write(file, output.getBytes(), StandardOpenOption.CREATE);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean inTake() {
        try{
            addresses.clear();
            for(String line : Files.readAllLines(file, Charset.defaultCharset())){
                String[] entries = line.split("<->");
                addresses.put(Identity.deserializeIdentity(entries[0]).getUniqueId(), entries[1]);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
