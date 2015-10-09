package com.Gbserver.commands;
import org.bukkit.ChatColor;

/**
 * Created by michael on 10/8/15.
 */
public class DamageRecord {
    public BaconPlayer by;
    BaconPlayer to;

    public DamageRecord(BaconPlayer by, BaconPlayer to) {
        this.by = by;
        this.to = to;
        to.addInflict(this);
        BaconLogs.log(false, "[DAMAGE] " + to.getHandle().getName() + " hit by " + by.getHandle().getName());
    }

    public String getChatOutput(){
        return ChatColor.DARK_AQUA + "- " + ChatColor.GREEN + this.to.getHandle().getName()
                + ChatColor.RESET + " hit by " + ChatColor.GREEN + this.by.getHandle().getName();
    }
}
