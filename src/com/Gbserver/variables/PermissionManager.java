package com.Gbserver.variables;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;


public class PermissionManager {
    @Deprecated
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
    public static Permissions getPermission(OfflinePlayer p){
        Permissions perm = perms.get(p.getUniqueId());
        if(perm == null) return Permissions.GUEST;
        return perm;
    }

    // New routines below

    public static LinkedList<String> permLevels = new LinkedList<>();

    public static final ConfigLoader.ConfigUser configUser = new ConfigLoader.ConfigUser() {
        @Override
        public void load() throws Exception {
            permLevels.clear();
            permLevels.addAll(ConfigManager.smartGet("PermissionLevels").keySet());
        }

        @Override
        public void unload() throws Exception {
            ConfigManager.smartGet("PermissionLevels").clear();
            for (String item : permLevels) {
                ConfigManager.entries.get("PermissionLevels").put(item, "");
            }
        }
    };

    public static boolean isAbove(String perm, String permlevel) {
        assert permLevels.contains(permlevel) && permLevels.contains(perm);
        return permLevels.indexOf(perm) > permLevels.indexOf(permlevel);
    }

}
