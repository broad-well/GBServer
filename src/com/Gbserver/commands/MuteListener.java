package com.Gbserver.commands;

import com.Gbserver.listener.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class MuteListener implements Listener{
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent apce){
		if(Mute.list.contains(apce.getPlayer())){
			apce.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You are muted."));
			apce.setCancelled(true);
			ChatFormatter.setCancelled.add(apce.getPlayer());
		}
	}
}
