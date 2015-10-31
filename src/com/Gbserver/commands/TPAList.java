package com.Gbserver.commands;

import com.Gbserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;

/**
 * Created by michael on 10/31/15.
 */
public class TPAList extends LinkedList<TpaPacket> {
    public TPAList() {
        super();
    }

    public boolean addPacket(final TpaPacket e){
        add(e);
        final TPAList This = this;
        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
            public void run() {
                This.remove(e);
            }
        }, 20L * 60L);
        return true;
    }
}
