package com.Gbserver.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Gbserver.commands.Nick;
import com.Gbserver.commands.Quit;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class LoginTagListener implements Listener{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent pje){
		pje.setJoinMessage(ChatWriter.getMessage(ChatWriterType.JOIN, pje.getPlayer().getName() + " has joined."));
		pje.getPlayer().setCustomName(ChatFormatter.generateTag(pje.getPlayer(),false));
		pje.getPlayer().setCustomNameVisible(true);
		pje.getPlayer().setPlayerListName(ChatFormatter.generateTag(pje.getPlayer(),false));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent pqe){
		if(Quit.ragequitters.contains(pqe.getPlayer())){
			pqe.setQuitMessage(ChatWriter.getMessage(ChatWriterType.QUIT, pqe.getPlayer().getName() + " has " + ChatColor.RED + "" + ChatColor.BOLD + "RAGEQUITTED!"));
			Quit.ragequitters.remove(pqe.getPlayer());
			return;
		}
		
		if(Quit.afkers.contains(pqe.getPlayer())){
			pqe.setQuitMessage(ChatWriter.getMessage(ChatWriterType.QUIT, pqe.getPlayer().getName() + " has been AFK removed."));
			Quit.afkers.remove(pqe.getPlayer());
			return;
		}
		
		if(Quit.diers.contains(pqe.getPlayer())){
			pqe.setQuitMessage(ChatWriter.getMessage(ChatWriterType.QUIT, pqe.getPlayer().getName() + ChatColor.RED + " has died. RIP."));
			Quit.diers.remove(pqe.getPlayer());
			return;
		}
		pqe.setQuitMessage(ChatWriter.getMessage(ChatWriterType.QUIT, pqe.getPlayer().getName() + " has left."));
	}
}
