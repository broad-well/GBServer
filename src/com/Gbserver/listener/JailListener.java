package com.Gbserver.listener;

import com.Gbserver.commands.Jail;
import com.Gbserver.variables.CommandProfile;
import com.Gbserver.variables.ConfigManager;
import com.Gbserver.variables.DebugLevel;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 1/18/16.
 */
public class JailListener implements Listener{
    private static DebugLevel dl = new DebugLevel(3, "CommandBlocker");

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
        dl.debugWrite("ProposedCommand: " + pcpe.getMessage() + ", algorithm-extracted label " + pcpe.getMessage().split(" ")[0].substring(1));
        if(CommandProfile.get(pcpe.getMessage().split(" ")[0].substring(1)).getProperty("enabled").equals("false")){
            pcpe.setCancelled(true);
            pcpe.getPlayer().sendMessage(ChatColor.RED + "This command has been disabled by an administrator.");
        }
    }

}
