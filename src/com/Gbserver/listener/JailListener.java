package com.Gbserver.listener;

import com.Gbserver.commands.Jail;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by michael on 1/18/16.
 */
public class JailListener implements Listener{
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent apce){
        if(Jail.prisoners.contains(apce.getPlayer())){
            apce.setCancelled(true);
            apce.getPlayer().sendMessage(ChatColor.GOLD + "Prisoners are muted.");
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent pcpe){
        if(Jail.prisoners.contains(pcpe.getPlayer())){
            pcpe.setCancelled(true);
            pcpe.getPlayer().sendMessage(ChatColor.GOLD + "Prisoners are not allowed to perform commands.");
        }
    }
}
