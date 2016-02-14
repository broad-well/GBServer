package com.Gbserver.listener;

import com.Gbserver.variables.GameType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.awt.*;

public class LobbyListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent piee) {
        if (piee.getRightClicked().equals(GameType.TF.getBlue())) {
            // Join Blue Team.
            GameType.TF.join(Color.BLUE, piee.getPlayer());
            return;
        }

        if (piee.getRightClicked().equals(GameType.TF.getRed())) {
            // Join Red Team.
            GameType.TF.join(Color.RED, piee.getPlayer());
            return;
        }


        if (piee.getRightClicked().equals(GameType.BL.getBlue())) {
            // Join Blue Team.
            GameType.BL.join(Color.BLUE, piee.getPlayer());
        }

        if (piee.getRightClicked().equals(GameType.BL.getRed())) {
            // Join Red Team.
            GameType.BL.join(Color.RED, piee.getPlayer());
        }

        if (piee.getRightClicked().equals(GameType.DR.getBlue())) {
            // Join Blue Team.
            GameType.DR.join(Color.BLUE, piee.getPlayer());
        }

        if (piee.getRightClicked().equals(GameType.DR.getRed())) {
            // Join Red Team.
            GameType.DR.join(Color.RED, piee.getPlayer());
        }

        if (piee.getRightClicked().equals(GameType.CTF.getBlue())) {
            // Join Blue Team.
            GameType.CTF.join(Color.BLUE, piee.getPlayer());
        }

        if (piee.getRightClicked().equals(GameType.CTF.getRed())) {
            // Join Red Team.
            GameType.CTF.join(Color.RED, piee.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent ede) {
        if (ede.getEntity().equals(GameType.TF.getBlue()) || ede.getEntity().equals(GameType.TF.getRed()) ||
                ede.getEntity().equals(GameType.BL.getBlue()) || ede.getEntity().equals(GameType.BL.getRed()) ||
                ede.getEntity().equals(GameType.DR.getBlue()) || ede.getEntity().equals(GameType.DR.getRed()) ||
                ede.getEntity().equals(GameType.CTF.getBlue()) || ede.getEntity().equals(GameType.CTF.getRed())) {
            ede.setCancelled(true);
        }
    }
}
