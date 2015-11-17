package com.Gbserver.listener;

import com.Gbserver.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by michael on 11/9/15.
 */
public class CreativeGameMode implements Listener{
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent pje){
        Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(), new Runnable() {
            @Override
            public void run() {
                pje.getPlayer().setGameMode(GameMode.CREATIVE);
            }
        }, 10L);
    }

    public static void setAll(GameMode gm){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.setGameMode(gm);
        }
    }
}
