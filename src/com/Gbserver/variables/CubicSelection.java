package com.Gbserver.variables;

import com.Gbserver.Utilities;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CubicSelection {
    Location high;
    Location low;

    public CubicSelection(Location high, Location low){
        if(!high.getWorld().equals(low.getWorld()) || high.getY() <= low.getY()) return;
        this.high = high;
        this.low = low;
    }

    public Location getHigh() {
        return high;
    }

    public Location getLow() {
        return low;
    }

    public boolean isInside(Location input) {
        return Utilities.isInRangeOf(high, low, input);
    }

    public String serialize() {
        HashMap<String, HashMap<String, String>> output = new HashMap<>();
        output.put("high", Utilities.mapLocation(high));
        output.put("low", Utilities.mapLocation(low));
        return new Yaml().dump(output);
    }

    public List<Block> allBlocks() {
        List<Block> build = new LinkedList<>();
        //Generate for loop.
        //X: low to high
        //Try low 20 high 30, and low 30 high 20
        for(int x = low.getBlockX(); x != high.getBlockX(); x += (high.getBlockX() - low.getBlockX())
                / Math.abs((high.getBlockX() - low.getBlockX()))){
            //Y: Preconfigured
            for(int y = low.getBlockY(); y <= high.getBlockY(); y++){
                //Z: low to high
                for(int z = low.getBlockZ(); z != high.getBlockZ(); z += (high.getBlockZ() - low.getBlockZ()) /
                        Math.abs(high.getBlockZ() - low.getBlockZ())){
                    build.add(high.getWorld().getBlockAt(x,y,z));
                }
            }
        }
        return build;
    }

    /* Static Content ahead */

    public static CubicSelection deserialize(String serialized){
        Object obj = new Yaml().load(serialized);
        if(obj instanceof HashMap){
            HashMap<String, HashMap<String, String>> map = (HashMap<String, HashMap<String, String>>) obj;
            return new CubicSelection(Utilities.locationMap(map.get("high")),
                    Utilities.locationMap(map.get("low")));
        }else return null;
    }

}
