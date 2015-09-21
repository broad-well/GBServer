package com.Gbserver.commands;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class InvinceListener implements Listener{
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent piee){
		if(Invince.inLine.contains(piee.getPlayer())){
			if(piee.getRightClicked() instanceof LivingEntity){
				if(Invince.saved.contains(piee.getRightClicked())){
					Invince.saved.remove(piee.getRightClicked());
					piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You have made this LivingEntity un-invinsible."));
				}else{
					Invince.saved.add((LivingEntity) piee.getRightClicked());
					piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You have made this LivingEntity invinsible."));
				}
				
				Invince.inLine.remove(piee.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent ede){
		if(Invince.saved.contains(ede.getEntity()) || (ede.getCause() == DamageCause.FALL && NoFall.noFall)){
			ede.setCancelled(true);
		}
	}
}
