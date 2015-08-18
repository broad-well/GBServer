package com.Gbserver.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginTagListener implements Listener{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent pje){
		pje.getPlayer().setCustomName(ChatFormatter.generateTag(pje.getPlayer()));
		pje.getPlayer().setCustomNameVisible(true);
	}
}
