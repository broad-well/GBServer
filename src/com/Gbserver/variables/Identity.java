package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * Created by michael on 12/7/15.
 */
public class Identity {
    // Player{uuid:XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXX,name:XXXXXXXX}
    public static String serializeIdentity(OfflinePlayer p){
        return "Player{uuid:" + p.getUniqueId() + ",name:" + p.getName() + "}";
    }

    public static OfflinePlayer deserializeIdentity(String serialized){
        if(!serialized.startsWith("Player{uuid:")) return null; // Player name length unsure
        return Bukkit.getOfflinePlayer(UUID.fromString(
                serialized.substring(7, serialized.indexOf("}"))
                .split(",")[0]
                .split(":")[1]
        ));
    }

    public static String fixSerialized(String bad){
        if(!serializedValid(bad))
            return serializeIdentity(deserializeIdentity(bad));
        else
            return bad;
    }

    public static boolean serializedValid(String check){
        if(!check.startsWith("Player{uuid:")) return false;
        String[] entries = check
                .substring(7,
                        check.indexOf("}"))
                .split(",");
        OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(entries[0].split(":")[1]));
        return target.getName().equals(entries[1].split(":")[1]);
    }
}
