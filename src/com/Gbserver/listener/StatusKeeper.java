package com.Gbserver.listener;

import com.Gbserver.variables.ConfigPlayerVault;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

/**
 * Created by michael on 10/24/15.
 */
public class StatusKeeper implements Listener{

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe){
        try{
            ConfigPlayerVault cpv = new ConfigPlayerVault(pqe.getPlayer());
            cpv.savData();
            Bukkit.getLogger().info(cpv.data.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        try{
            ConfigPlayerVault cpv = new ConfigPlayerVault(pje.getPlayer());
            cpv.loadData();
            Bukkit.getLogger().info(cpv.data.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
