package com.Gbserver.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.Gbserver.commands.Nick;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class NickListener implements Listener {
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent edbee) {
		if (Nick.list.contains(edbee.getPlayer())) {
			Entity target = edbee.getRightClicked();
			//Nick.clicked = true;
			//Nick.clickPending = false;
			edbee.setCancelled(true);

			if (!Nick.isNaming) {
				if (edbee.getRightClicked() instanceof Player) {
					DisguiseAPI.undisguiseToAll(target);
					DisguiseAPI.setViewDisguiseToggled(target, true);
					Nick.sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Sucessfully unnamed this Player."));
				} else {
					target.setCustomNameVisible(false);
					target.setCustomName("");
					Nick.sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully unnamed this entity."));
				}
			} else {
				if (edbee.getRightClicked() instanceof Player) {
					PlayerDisguise pd = new PlayerDisguise(Nick.arg, ((Player) edbee.getRightClicked()).getName());
					
					DisguiseAPI.disguiseEntity(target, pd);
					DisguiseAPI.setViewDisguiseToggled(target, false);
					Nick.sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Sucessfully named this Player as " + Nick.arg));
				} else {
					target.setCustomName(Nick.arg);
					target.setCustomNameVisible(true);
					Nick.sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully named this entity as " + Nick.arg));
				}
			}
			Nick.list.remove(edbee.getPlayer());
		}
	}
}
