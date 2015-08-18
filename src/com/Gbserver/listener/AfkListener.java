package com.Gbserver.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.Gbserver.commands.Afk;

public class AfkListener implements Listener{
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent pme) {
		Player pl = pme.getPlayer();
		if(Afk.afkList.contains(pl)){
			Afk.doAFK(pl);
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent pce) {
		Player pl = pce.getPlayer();
		if(Afk.afkList.contains(pl)){
			Afk.doAFK(pl);
		}
	}
}
