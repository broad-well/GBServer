package com.Gbserver.variables;


import com.Gbserver.variables.ConfigLoader.ConfigUser;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 1/30/16.
 */
public class CommandProfile {
    private static Yaml helper = new Yaml();
    private static final DebugLevel dl = new DebugLevel(2, "Command Profiler");
    public static HashMap<String, HashMap<String, String>> data = new HashMap<>();

    public static CommandProfile get(String entry){
        if(!data.containsKey(entry)){
            data.put(entry, new HashMap<String, String>());
        }
        CommandProfile cp = new CommandProfile();
        cp.label = entry;
        return cp;
    }

    public static final ConfigUser configUser = new ConfigUser() {
        public void unload() {
            dl.debugWrite("Attempting to output Command Profiler data");
            ConfigManager.smartGet("CommandProfile");
            for (Map.Entry<String, HashMap<String, String>> entry : data.entrySet()) {
                ConfigManager.entries.get("CommandProfile").put(entry.getKey(), helper.dump(entry.getValue()));
            }
            dl.debugWrite("Command Profiler data output finished.");
        }

        public void load() {
            dl.debugWrite("Attempting to input Command Profiler data");
            data.clear();
            for (Map.Entry<String, String> entry : ConfigManager.smartGet("CommandProfile").entrySet()) {
                data.put(entry.getKey(), (HashMap<String, String>) helper.load(entry.getValue()));
            }
            dl.debugWrite("Command Profiler data input finished.");
        }
    };



    private String label;

    public String getProperty(String prop){
        String raw = data.get(label).get(prop);
        return raw == null ? "" : raw;
    }
}
