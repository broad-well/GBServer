package com.Gbserver.listener;

import com.Gbserver.variables.Vault;
import com.Gbserver.variables.Vaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BackListeners implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde) {
        Player target = pde.getEntity();
        Vault v = Vaults.getVault(target.getUniqueId());
        v.previous = target.getLocation();
    }
}
