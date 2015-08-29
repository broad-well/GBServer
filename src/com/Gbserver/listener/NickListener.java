package com.Gbserver.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.Gbserver.commands.Nick;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class NickListener implements Listener {
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent edbee) {
		if (Nick.clickPending) {
			Nick.target = edbee.getRightClicked();
			Nick.clicked = true;
			Nick.clickPending = false;
			edbee.setCancelled(true);

			if (!Nick.isNaming) {
				if (edbee.getRightClicked() instanceof Player) {
					DisguiseAPI.undisguiseToAll(Nick.target);
					DisguiseAPI.setViewDisguiseToggled(Nick.target, true);
					Nick.sender.sendMessage("Sucessfully unnamed this Player.");
				} else {
					Nick.target.setCustomNameVisible(false);
					Nick.target.setCustomName("");
					Nick.sender.sendMessage("Successfully unnamed this entity.");
				}
			} else {
				if (edbee.getRightClicked() instanceof Player) {
					PlayerDisguise pd = new PlayerDisguise(Nick.arg, ((Player) edbee.getRightClicked()).getName());
					
					DisguiseAPI.disguiseEntity(Nick.target, pd);
					DisguiseAPI.setViewDisguiseToggled(Nick.target, false);
					Nick.sender.sendMessage("Sucessfully named this Player as " + Nick.arg);
				} else {
					Nick.target.setCustomName(Nick.arg);
					Nick.target.setCustomNameVisible(true);
					Nick.sender.sendMessage("Successfully named this entity as " + Nick.arg);
				}
			}
		}
	}
}
