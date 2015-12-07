package com.Gbserver.listener;

import com.Gbserver.variables.EnhancedPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class StatOnlineTime implements Listener{
    public static HashMap<EnhancedPlayer, Long> joinMillis = new HashMap<>();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        try {
            joinMillis.put(EnhancedPlayer.getEnhanced(pje.getPlayer()), System.currentTimeMillis());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe){
        try {
            EnhancedPlayer ep = EnhancedPlayer.getEnhanced(pqe.getPlayer());
            ep.setDuration(
                    ep.getDuration() +
                            (System.currentTimeMillis() - joinMillis.get(ep)) / 1000
            );
            joinMillis.remove(ep);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
