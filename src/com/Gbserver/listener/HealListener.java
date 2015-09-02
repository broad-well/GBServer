package com.Gbserver.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.Gbserver.commands.Heal;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class HealListener implements Listener{
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent piee){
		if(Heal.players.contains(piee.getPlayer())){
			if(piee.getRightClicked() instanceof LivingEntity){
				LivingEntity le = (LivingEntity) piee.getRightClicked();
				le.setHealth(le.getMaxHealth());
				piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND,"Successfully healed this Living Entity."));
				Heal.players.remove(piee.getPlayer());
			}
		}
	}
}
