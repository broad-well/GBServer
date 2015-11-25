package com.Gbserver;

import com.Gbserver.variables.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class EventSpecials implements Listener{
    //Currently: Thanksgiving!

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        try {
            pje.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Thanksgiving> " + ChatColor.GOLD +
                    "Thank you for being " + randomDeed() + "! " + ChatColor.ITALIC + "~Happy Thanksgiving!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final Path file = ConfigManager.getPathInsidePluginFolder("tgmsgs.txt");
    private String randomDeed() throws IOException {
        List<String> deeds = Files.readAllLines(file, Charset.defaultCharset());
        return deeds.get(Utilities.getRandom(0, deeds.size()-1));
    }
}
