package com.Gbserver.listener;

import org.bukkit.entity.Bat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.Gbserver.variables.Chairs;

public class SitListener implements Listener{
	@EventHandler
	public void onEntityDamage(EntityDamageEvent ede){
		if(ede.getEntity() instanceof Bat){
			if(Chairs.bats.contains(ede.getEntity())){
				ede.setCancelled(true);
			}
		}
	}
}
