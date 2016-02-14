package com.Gbserver.variables;

import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * A ConfigManager for those who needs config.
 */
public class ConfigManager {
    private static Path mainFile = getPathInsidePluginFolder("volatile.dat");
    public static HashMap<String, HashMap<String, String>> entries = new HashMap<>();
    private static Yaml confHelper = SwiftDumpOptions.BLOCK_STYLE();

    public static HashMap<String, String> smartGet(String label){
        HashMap<String, String> output = entries.get(label);
        if(output == null){
            System.out.println("Founding new entry: " + label);
            entries.put(label, new HashMap<String, String>());
            return entries.get(label);
        }else return output;
    }

    public static void output() {
        try {
            FileWriter fos = new FileWriter(mainFile.toFile(), false);
            confHelper.dump(entries, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void input() {
        try{
            FileReader fr = new FileReader(mainFile.toFile());
            Object loaded = confHelper.load(fr);

            entries = loaded instanceof HashMap ? (HashMap<String, HashMap<String, String>>) loaded :
                    new HashMap<String, HashMap<String, String>>();
            fr.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //STATIC
    public static Path getPathInsidePluginFolder(String name){
        return Paths.get("/home/michael/SpigotCache/plugins/Broadwell_Server_Plugin", name);
    }
}
