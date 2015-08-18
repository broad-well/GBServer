package com.Gbserver.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.Gbserver.commands.Heal;

public class HealListener implements Listener{
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent piee){
		if(Heal.pending && piee.getPlayer() == Heal.sender){
			if(piee.getRightClicked() instanceof LivingEntity){
				LivingEntity le = (LivingEntity) piee.getRightClicked();
				le.setHealth(le.getMaxHealth());
				Heal.sender.sendMessage("Successfully healed this Living Entity.");
				Heal.pending = false;
			}
		}
	}
}
