package com.Gbserver.variables;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Vault {
    public Location previous = null;
    public UUID uuid;
    public ArrayList<String> condition = new ArrayList<>();
    public HashMap<String, Object> properties = new HashMap<>();

    //Equivalent of a struct.
    public Vault(OfflinePlayer p) {
        this.uuid = p.getUniqueId();
    }
}
