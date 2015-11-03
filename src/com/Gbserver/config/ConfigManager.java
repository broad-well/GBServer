package com.Gbserver.config;

import com.embryopvp.EmbryoPVP;
import com.embryopvp.utils.LogUtil;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;


public class ConfigManager {

    private static ConfigManager instance = new ConfigManager();
    private static Plugin p = EmbryoPVP.getPlugin();

    private FileConfiguration messages;

    private File messagesFile;


    public ConfigManager() {

    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public void setup() {

        //load the default config
        p.getConfig().options().copyDefaults(true);
        p.saveDefaultConfig();

        //load our config files
        messagesFile = new File(p.getDataFolder(), "messages.yml");

        try {
            if(!messagesFile.exists()) {
                loadFile("messages.yml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //load our config files
        messages = YamlConfiguration.loadConfiguration(messagesFile);

    }


    public FileConfiguration getConfig() {
        return p.getConfig();
    }

    public FileConfiguration getMessagesConfig() {
        return messages;
    }

    public boolean isDebugEnabled() {
        return p.getConfig().getBoolean("debug");
    }

    public void loadFile(String file)
    {
        File t = new File(p.getDataFolder(), file);
        LogUtil.LogToConsole("Writing new file: " + t.getAbsolutePath());

        try {
            t.createNewFile();
            FileWriter out = new FileWriter(t);
            if (!isDebugEnabled()) {
                //should we print the file we are writing for debug purposes?
                System.out.println(file);
            }
            //InputStream is = getClass().getResourceAsStream("/"+file);
            InputStream is = p.getResource(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                out.write(line + "\n");
                if (!isDebugEnabled()) {
                    System.out.print(line);
                }
            }
            out.flush();
            is.close();
            isr.close();
            br.close();
            out.close();

            if (!isDebugEnabled()) {
                LogUtil.LogToConsole("Loaded Config " + file + " successfully!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveConfig() {
        p.saveConfig();
    }


    public void saveMessagesConfig() {
        try {

            //save our messages config file
            messages.save(messagesFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
