package com.Gbserver.listener;

import com.Gbserver.commands.Runner;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class RunnerListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent ede) {
        if (ede.getEntity().equals(Runner.joinSheep)) {
            ede.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent piee) {
        if (piee.getRightClicked().equals(Runner.joinSheep)) {
            if (Runner.players.contains(piee.getPlayer())) {
                ChatWriter.writeTo(piee.getPlayer(), ChatWriterType.GAME, "You are already in this game.");
                return;
            }
            Runner.players.add(piee.getPlayer());
            ChatWriter.writeTo(piee.getPlayer(), ChatWriterType.GAME, "You have joined this Runner game.");
        }
    }
}
