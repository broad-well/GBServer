package com.Gbserver.variables;


import org.bukkit.World;

import java.util.UUID;

/*
    Minigames shall extend from this one.
 */
public class Minigame {
    public boolean isRunning = false;
    private World world;
    private String identifier;
    private String name;

    public Minigame(String name, String ident, World world) {
        this.name = name;
        this.identifier = ident;
        this.world = world;
    }

    public String getIdentifier() { return identifier; }
    public String getName() {return name;}
    public World getWorld() {return world;}
    public java.util.List<UUID> players() { return null; }

}
