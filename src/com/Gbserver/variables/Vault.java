package com.Gbserver.variables;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Vault {
    private Location previous = null;
    private Player target = null;
    private UUID uuid = null;
    private Object[] data = null;

    //Goal: Create a class that has UUID ident, extending Player. See http://pastie.org/3080995#.
    public Vault(Player p) {
        this.target = p;
        this.uuid = p.getUniqueId();
    }

    public void setPrevious(Location l) {
        this.previous = l;
    }

    public void toPrevious() {
        target.teleport(previous);
    }

    public boolean hasPrevious() {
        return previous != null;
    }

    public void setRawData(Object[] data) {
        this.data = data;
    }

    public boolean hasRawData() {
        return data != null;
    }

    public Object[] getRawData() {
        return data;
    }

    public UUID getUUID() {
        return uuid;
    }
}
