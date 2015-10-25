package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by michael on 10/24/15.
 */
public class ConfigPlayerVault extends ConfigManager{
    public static Path playerDat = getPathInsidePluginFolder("players.dat");
    private Player target;
    private String uuid;
    public ConfigPlayerVault(Player p) throws IOException {
        super(playerDat, 7);
        uuid = p.getUniqueId().toString();
        target = p;
    }

    
    public void savData() throws IOException {
        //Location, Gamemode
        List<String> data = new LinkedList<>();
        {
            Location loc = target.getLocation();
            data.add(String.valueOf(loc.getWorld().getUID().toString()));
            data.add(String.valueOf(loc.getX()));
            data.add(String.valueOf(loc.getY()));
            data.add(String.valueOf(loc.getZ()));
            data.add(String.valueOf(loc.getPitch()));
            data.add(String.valueOf(loc.getYaw()));
        }
        {
            this.data.remove(uuid);
            data.add(GmToString(target.getGameMode()));
        }
        add(uuid, null, data.toArray(new String[6]));
        flush();
    }

    public void loadData() throws IOException {
        readData();
        List<String> value = data.get(uuid);
        if(value == null) return;
        World w = Bukkit.getWorld(UUID.fromString(value.get(0)));
        Location targetLocation = new Location(w, Double.valueOf(value.get(1)),
                Double.valueOf(value.get(2)),
                Double.valueOf(value.get(3)),
                Float.valueOf(value.get(4)),
                Float.valueOf(value.get(5)));
        target.teleport(targetLocation);
        target.setGameMode(GmFromString(value.get(6)));
    }

    private static final String CREATIVE = "C";
    private static final String SURVIVAL = "S";
    private static final String ADVENTURE = "A";
    private static final String SPECTATOR = "P";
    private String GmToString(GameMode gm){
        switch(gm){
            case CREATIVE:
                return CREATIVE;
            case SURVIVAL:
                return SURVIVAL;
            case ADVENTURE:
                return ADVENTURE;
            case SPECTATOR:
                return SPECTATOR;
            default:
                return null;
        }
    }

    private GameMode GmFromString(String str){
        switch(str){
            case CREATIVE:
                return GameMode.CREATIVE;
            case SURVIVAL:
                return GameMode.SURVIVAL;
            case ADVENTURE:
                return GameMode.ADVENTURE;
            case SPECTATOR:
                return GameMode.SPECTATOR;
            default:
                return GameMode.CREATIVE;
        }
    }
}
