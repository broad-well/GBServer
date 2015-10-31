package com.Gbserver.variables;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by michael on 10/31/15.
 */
public class PermissionManager {
    public enum Permissions {
        GUEST,PRIVILEGED,DEVELOPER,OWNER;
        public int getLevel() {
            switch(this){
                case GUEST:
                    return 0;
                case PRIVILEGED:
                    return 1;
                case DEVELOPER:
                    return 2;
                case OWNER:
                    return 3;
                default:
                    return 0xfffff;
            }
        }

        public boolean isAbove(Permissions p){
            return getLevel() > p.getLevel();
        }
    }
    public static HashMap<UUID, Permissions> perms = new HashMap<>();

    private static Path file = ConfigManager.getPathInsidePluginFolder("permissions.dat");
    public static void export() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new FileOutputStream(file.toFile()), true);
        for(Map.Entry<UUID, Permissions> entry : perms.entrySet()){
            writer.println(entry.getKey().toString() + "," + entry.getValue().toString());
        }
        writer.flush();
        writer.close();
    }

    public static void import_() throws IOException {
        for(String line : Files.readAllLines(file, Charset.defaultCharset())){
            String[] data = line.split(",");
            perms.put(UUID.fromString(data[0]), Permissions.valueOf(data[1]));
        }
    }

    public static Permissions getPermission(OfflinePlayer p){
        Permissions perm = perms.get(p.getUniqueId());
        if(perm == null) return Permissions.GUEST;
        return perm;
    }

    public static List<UUID> playersWith(Permissions perm){
        List<UUID> uids = new LinkedList<>();
        for(UUID uid : perms.keySet()){
            if(perms.get(uid) == perm) uids.add(uid);
        }
        return uids;
    }
}
