package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by michael on 10/17/15.
 */
public class WarpLocation extends Location{

    public WarpLocation(String worldName, double X, double Y, double Z){
        super(Bukkit.getWorld(worldName), X, Y, Z);
    }


}
