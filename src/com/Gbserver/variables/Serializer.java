package com.Gbserver.variables;

import com.Gbserver.Utilities;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 1/30/16.
 */
public class Serializer {
    public static final String NAME = "Serializer Subsystem";
    private static final DebugLevel dl = new DebugLevel(2, NAME);
    private static final Yaml helper = new Yaml();
    private static HashMap<String, HashMap<String, String>> customSerialize = new HashMap<>();
    /*
    customSerialize Format:
    java.lang.String: |
        "serializedField": "typeField",
        "serializedField": "typeField"
     */
    public static String serialize(Object obj){
        HashMap<String, String> build = new HashMap<>();
        dl.debugWrite("Serialization requested, type " + obj.getClass().getName());
        build.put("!type", obj.getClass().getName());
        try {
            if (customSerialize.containsKey(obj.getClass().getName())) { //Full name. E.g. java.lang.String.
                for (Map.Entry<String, String> strstr : customSerialize.get(obj.getClass().getName()).entrySet()) {
                    dl.debugWrite(4, "Encountered specified serialization object");
                    build.put(strstr.getKey(), obj.getClass().getField(strstr.getValue()).get(obj).toString());
                }
            }else{
                //Try to preserve all declared values
                for(Field f : obj.getClass().getDeclaredFields()){
                    f.setAccessible(true);
                    dl.debugWrite(4, "Encountered unspecified serialization object, toString " + f.get(obj).toString());
                    build.put(f.getName(), f.get(obj).toString());
                }
            }
            return helper.dump(build);
        }catch(Exception e){
            dl.debugWrite("SERIALIZATION ERROR! Exception:");
            dl.debugWrite(Utilities.getStackTrace(e));
            return "";
        }
    }

    public static final ConfigLoader.ConfigUser configUser = new ConfigLoader.ConfigUser() {

        public void unload() {
            dl.debugWrite("Extracting custom serializer configuration...");
            for (Map.Entry<String, HashMap<String, String>> entry : customSerialize.entrySet()) {
                dl.debugWrite(4, "Encountered entry in serializer configuration: " + entry.getKey() + " -> " + entry.getValue());
                ConfigManager.entries.get("Serializer").put(entry.getKey(), SwiftDumpOptions.BLOCK_STYLE().dump(entry.getValue()));
            }
            dl.debugWrite("Serializer configuration extraction complete.");
        }

        public void load() {
            dl.debugWrite("Reading custom serializer configuration... Clearing cache.");
            customSerialize.clear();
            for (Map.Entry<String, String> entry : ConfigManager.smartGet("Serializer").entrySet()) {
                dl.debugWrite(4, "Encountered entry in ConfigManager data: " + entry.getKey() + " -> " + entry.getValue());
                customSerialize.put(entry.getKey(), (HashMap<String, String>) SwiftDumpOptions.BLOCK_STYLE().load(entry.getValue()));
            }
            dl.debugWrite("Serializer configuration read complete.");
        }
    };

}
