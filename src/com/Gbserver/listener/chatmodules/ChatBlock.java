package com.Gbserver.listener.chatmodules;

import com.Gbserver.listener.ChatModule;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;

public class ChatBlock implements ChatModule{
    @Override
    public String getName() {
        return "Inappropriate Content Blocker";
    }

    @Override
    public HashMap<String, String> passThru(HashMap<String, String> hs) {
        for(String entry : ConfigManager.smartGet("Vulgar").keySet()){
            if(complexContains(hs.get("msg"), entry)){
                hs.put("enabled", "false");
                ChatWriter.writeTo(Bukkit.getPlayer(hs.get("sender")), ChatWriterType.CHAT,
                        "Vulgar content detected. Keep in mind that excessive vulgarism can lead to mute and/or ban.");
                ChatWriter.writeTo(Bukkit.getPlayer(hs.get("sender")), ChatWriterType.CHAT,
                        "Bad content: " + ChatColor.YELLOW + entry);
                Bukkit.getLogger().info(" .vulgar : " + hs);
            }
        }
        return hs;
    }

    public boolean complexContains(String content, String criteria){
        //lock in COMPLEXLocKcOMPLEX                bie in DWmajBIEke
        //4       length 18, 15 loops, 0 to 14      3      10, 8 loops, 0 to
        if(criteria.length() > content.length()) return false;
        for(int i = 0; i < (content.length() - criteria.length() + 1); i++){
            if(content.substring(i, i+ criteria.length()).equalsIgnoreCase(criteria)) return true;
        }
        return false;
    }
}
