package com.Gbserver.listener.chatmodules;

import com.Gbserver.listener.ChatModule;
import com.Gbserver.variables.EnhancedPlayer;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 1/30/16.
 */
public class ChatIgnore implements ChatModule{
    @Override
    public String getName() {
        return "Chat Ignorance Module";
    }

    @Override
    public HashMap<String, String> passThru(HashMap<String, String> hs) {
        List<String> recipients = (List<String>) new Yaml().load(hs.get("recipients"));
        List<String> removal = new LinkedList<>();
        for(String str : recipients){
            if(EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(str)).isIgnoring(Bukkit.getPlayer(hs.get("sender")))){
                removal.add(str);
            }
        }
        recipients.removeAll(removal);
        hs.put("recipients", new Yaml().dump(recipients));
        return hs;
    }
}
