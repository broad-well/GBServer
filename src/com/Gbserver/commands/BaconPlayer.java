package com.Gbserver.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 10/8/15.
 */
public class BaconPlayer{
    private Player handle;
    public List<DamageRecord> records = new LinkedList<>();

    public BaconPlayer(Player handle){
        this.handle = handle;
        Bacon.players.add(this);
    }
    public Player getHandle() {
        return handle;
    }

    public void addInflict(DamageRecord dr){
        records.add(dr);
    }

    public String getLastDamages(int num){
        return ChatColor.DARK_AQUA + "#" + num + ": " + ChatColor.GREEN + records.get(records.size()-num).to.getHandle().getName()
                + ChatColor.RESET + " hit by " + ChatColor.GREEN + records.get(records.size()-num).by.getHandle().getName();
    }
    //STATIC

    public static BaconPlayer getByHandle(Player handle){
        for(BaconPlayer bp : Bacon.players){
            if(bp.getHandle().equals(handle)){
                return bp;
            }
        }
        return null;
    }



}
