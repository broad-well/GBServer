package com.Gbserver.listener;

import com.Gbserver.variables.minigame.MGUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by michael on 2/7/16.
 */
public class MGListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde) {
        for (MGUtils mgu : MGUtils.utilAccess.values()) {
            if (mgu.mg.getPlayers().contains(pde.getEntity()) && mgu.mg.getRunlevel() == 3 /* Is running */) {
                mgu.eliminate(pde.getEntity());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe) {
        for (MGUtils mgu : MGUtils.utilAccess.values()) {
            if (mgu.mg.getPlayers().contains(pqe.getPlayer())) {
                mgu.abandon(pqe.getPlayer());
            }

        }
    }
}
