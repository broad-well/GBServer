package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Couple {
    public static File file = ConfigManager.getPathInsidePluginFolder("marriages.dat").toFile();
    private static Yaml helper = SwiftDumpOptions.BLOCK_STYLE();
    public static List<HashMap<String, String>> data = new LinkedList<>();

    public static boolean newCouple(String keyCharacter, String Reactive){
        //Check for existing couples with this property.
        for(HashMap<String, String> couple : data){
            if(Identity.deserializeIdentity(couple.get("Proactive")).getName().equalsIgnoreCase(keyCharacter) &&
                    Identity.deserializeIdentity(couple.get("Reactive")).getName().equalsIgnoreCase(Reactive)){
                return false;
            }
        }

        HashMap<String, String> build = new HashMap<>();
        build.put("PUID", PUID.randomPUID(8).toString());
        build.put("Proactive", Identity.serializeIdentity(Bukkit.getOfflinePlayer(keyCharacter)));
        build.put("Reactive", Identity.serializeIdentity(Bukkit.getOfflinePlayer(Reactive)));
        build.put("TimeOfMarriage", new Date().toString());
        data.add(build);
        return true;
    }

    public static boolean isCoupled(OfflinePlayer arg1, OfflinePlayer arg2){
        for(HashMap<String, String> couple : data){
            if(couple.values().contains(Identity.serializeIdentity(arg1)) &&
                    couple.values().contains(Identity.serializeIdentity(arg2))){
                return true;
            }
        }
        return false;
    }

    public static boolean purgeCouple(PUID puid){
        HashMap<String, String> selected = null;
        for(HashMap<String, String> couple : data){
            if(couple.get("PUID").equals(puid.toString())){
                selected = couple;
            }
        }
        return selected != null && data.remove(selected);
    }

    public static void output() throws IOException {
        FileWriter fw = new FileWriter(file);
        helper.dump(data, fw);
        fw.flush();
        fw.close();
    }

    public static void input() throws IOException {
        FileReader fr = new FileReader(file);
        Object obj = helper.load(fr);
        fr.close();
        if(obj instanceof List){
            data = (List<HashMap<String, String>>) obj;
        }
    }
}
