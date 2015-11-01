package com.Gbserver.variables;


import org.bukkit.World;
import org.bukkit.entity.Player;

/*
    Minigames shall extend from this one.
 */
public class Minigame {
    public World world;
    public String name;
    public boolean isRunning;

    public Minigame(String name, World world1){
        isRunning = false;
        this.name = name;
        this.world = world1;
    }
}
