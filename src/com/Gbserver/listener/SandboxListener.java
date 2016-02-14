package com.Gbserver.listener;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by michael on 1/22/16.
 */
public class SandboxListener implements Listener{
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent pcpe){
        if(Sandbox.contents.contains(pcpe.getPlayer().getUniqueId())){
            pcpe.setCancelled(true);
            ChatWriter.writeTo(pcpe.getPlayer(), ChatWriterType.CONDITION, "Players in Sandbox are not allowed to execute commands.");
        }
    }
}
