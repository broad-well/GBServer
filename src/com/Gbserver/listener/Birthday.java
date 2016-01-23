package com.Gbserver.listener;

import com.Gbserver.variables.ConfigManager;
import com.Gbserver.variables.Identity;
import com.Gbserver.variables.SwiftDumpOptions;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by michael on 1/22/16.
 */
public class Birthday implements Listener{
    private static final int MONTH = 0;
    private static final int DAY = 1;

    public static HashMap<String, List<Integer>> birthData = new HashMap<>();

    public static List<OfflinePlayer> whosBirthday() {
        //Nullable
        List<OfflinePlayer> returning = new LinkedList<>();
        for(Map.Entry<String, List<Integer>> me : birthData.entrySet()){
            if(me.getValue().get(MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                    me.getValue().get(DAY) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                returning.add(Identity.deserializeIdentity(me.getKey()));
            }
        }
        return returning;
    }

    public static void input() throws IOException {
        FileReader fr = new FileReader(ConfigManager.getPathInsidePluginFolder("birthData.yml").toFile());
        Object obj = new Yaml().load(fr);
        fr.close();
        if(obj instanceof HashMap){
            birthData = (HashMap<String, List<Integer>>) obj;
        }
    }

    public static void output() throws IOException {
        FileWriter fw = new FileWriter(ConfigManager.getPathInsidePluginFolder("birthData.yml").toFile());
        new Yaml().dump(birthData, fw);
        fw.flush();
        fw.close();
    }

    //Actual listeners
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        for(OfflinePlayer birthdayers : whosBirthday()){
            if(pje.getPlayer().getUniqueId().equals(birthdayers.getUniqueId())){
                pje.getPlayer().sendMessage(ChatColor.BLUE + "Broadwell: " + ChatColor.YELLOW + "Happy birthday, " + pje.getPlayer().getName() + "!");
            }else {
                pje.getPlayer().sendMessage(ChatColor.BLUE + "Birthday> " + ChatColor.YELLOW + "It's " + birthdayers.getName() + "'s birthday! " +
                        ChatColor.ITALIC + "Wish him/her a happy birthday!");
            }
        }
    }
}
