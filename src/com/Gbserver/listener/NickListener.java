package com.Gbserver.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.Gbserver.commands.Nick;

public class NickListener implements Listener{
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent edbee){
		if(Nick.clickPending){
			Nick.target = edbee.getRightClicked();
			Nick.clicked = true;
			Nick.clickPending = false;
			edbee.setCancelled(true);
			if (!Nick.isNaming) {
				Nick.target.setCustomNameVisible(false);
				Nick.target.setCustomName("");
				Nick.sender.sendMessage("Successfully unnamed this entity.");
			} else {
				Nick.target.setCustomName(Nick.arg);
				Nick.target.setCustomNameVisible(true);
				Nick.sender.sendMessage("Successfully named this entity as " + Nick.arg);
			}
		}
	}
}
