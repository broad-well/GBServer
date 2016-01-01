package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by michael on 12/21/15.
 */
public class MinigamesHub {
}
class HubCoords {
    private static final World world = Bukkit.getWorld("world");
    public static final Location[][] coords = { // First element: top left, second element: bottom right
            {new Location(world, -739, 13, 728), new Location(world, -739, 9, 726)},
            {new Location(world, -733, 13, 720), new Location(world, -731, 9, 720)},
            {},
            {},
            {},
            {},
            {},
            {}
    };
}
