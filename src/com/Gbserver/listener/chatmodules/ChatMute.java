package com.Gbserver.listener.chatmodules;

import com.Gbserver.commands.Mute;
import com.Gbserver.listener.ChatModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;

/**
 * Created by michael on 1/30/16.
 */
public class ChatMute implements ChatModule{
    @Override
    public String getName() {
        return "Chat muting module";
    }

    @Override
    public HashMap<String, String> passThru(HashMap<String, String> hs) {
        if(Mute.list.contains(Bukkit.getPlayer(hs.get("sender")))){
            hs.put("enabled", "false");
            Bukkit.getPlayer(hs.get("sender")).sendMessage(ChatColor.BLUE + "Mute> " + ChatColor.GRAY + "Muted players may not chat.");
        }
        return hs;
    }
}
