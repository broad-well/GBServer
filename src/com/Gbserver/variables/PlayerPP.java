package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class PlayerPP {
    private static DebugLevel dl = new DebugLevel(2, "Player++");
    private static Yaml yaml = SwiftDumpOptions.BLOCK_STYLE();
    private static File file = ConfigManager.getPathInsidePluginFolder("playerdata.yml").toFile();
    private static HashMap<OfflinePlayer, HashMap<String, Object>> data = new HashMap<>();
    public static final HashMap<String, Object> StandardInitAttributes = new HashMap<String, Object>() {{
        put("permission", "guest");
    }};

    public static PlayerPP get(OfflinePlayer op) {
        if (!data.containsKey(op)) {
            Bukkit.getLogger().info("Initialized new PlayerPP storage. Name: " + op.getName());
            data.put(op, createNew());
        }
        return new PlayerPP(op);
    }

    private static HashMap<String, Object> createNew() {
        HashMap<String, Object> product = new HashMap<>();
        product.putAll(StandardInitAttributes);
        return product;
    }

    public static final ConfigLoader.ConfigUser configUser = new ConfigLoader.ConfigUser() {
        @Override
        public void load() throws Exception {
            FileReader fr = new FileReader(file);
            Object obj = yaml.load(fr);
            fr.close();
            if (obj instanceof HashMap) {
                for (Map.Entry<String, HashMap<String, Object>> entry :
                        ((HashMap<String, HashMap<String, Object>>) obj).entrySet()) {
                    dl.debugWrite(5, "Encountered Player++ Configuration Item " + entry.getKey() + ", importing");
                    data.put(Identity.deserializeIdentity(entry.getKey()), entry.getValue());
                }
            } else {
                Bukkit.getLogger().severe("PlayerPP: ConfigLoad unexpected Object type! PlayerPP may not work as" +
                        " expected.");
            }
        }

        @Override
        public void unload() throws Exception {
            FileWriter fw = new FileWriter(file);
            HashMap<String, HashMap<String, Object>> toExport = new HashMap<>();
            for (Map.Entry<OfflinePlayer, HashMap<String, Object>> entry : data.entrySet()) {
                dl.debugWrite(5, "Encountered Player++ Data Item " + entry.getKey() + ", exporting");
                toExport.put(Identity.serializeIdentity(entry.getKey()), entry.getValue());
            }
            yaml.dump(toExport, fw);
            fw.flush();
            fw.close();
        }
    };

    // End of static content
    // A wrapper with minimal abstraction

    private OfflinePlayer op;

    private PlayerPP(OfflinePlayer op) {
        this.op = op;
    }

    public Object getProperty(String tag) {
        return data.get(op).get(tag);
    }
}
