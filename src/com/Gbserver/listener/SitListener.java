package com.Gbserver.listener;

import com.Gbserver.variables.Chairs;
import org.bukkit.entity.Bat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class SitListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent ede) {
        if (ede.getEntity() instanceof Bat) {
            if (Chairs.bats.contains(ede.getEntity())) {
                ede.setCancelled(true);
            }
        }
    }
}
