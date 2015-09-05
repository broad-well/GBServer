package com.Gbserver.variables;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class LobbyListener implements Listener{
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent piee){
		if(piee.getRightClicked().equals(Lobby.getLobby(LT.TF).getBlueJoin())){
			//Do join blue.
			Lobby l = Lobby.getLobby(LT.TF);
			
			if(l.blue.contains(piee.getPlayer())){
				piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are already on "+ChatColor.BLUE + "Team Blue"));
			}else{
				if(l.red.contains(piee.getPlayer())){
					l.removePlayer(piee.getPlayer(), false);
				}
				l.addPlayer(piee.getPlayer(), false);
				piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Added you to "+ChatColor.BLUE+
						"Blue Team"));
				
			}
			return;
		}
		if(piee.getRightClicked().equals(Lobby.getLobby(LT.TF).getRedJoin())){
			//Do join red.
			Lobby l = Lobby.getLobby(LT.TF);
			if(l.red.contains(piee.getPlayer())){
				piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are already on "+ChatColor.RED + "Team Red"));
			}else{
				if(l.blue.contains(piee.getPlayer())){
					l.removePlayer(piee.getPlayer(), false);
				}
				l.addPlayer(piee.getPlayer(), true);
				piee.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Added you to "+ChatColor.RED+
						"Red Team"));
			}
		}
		
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent ede){
		boolean isLobbyRelated = ede.getEntity().equals(Lobby.getLobby(LT.TF).getRedJoin()) || ede.getEntity().equals(Lobby.getLobby(LT.TF).getBlueJoin())
				|| ede.getEntity().equals(Lobby.getLobby(LT.BL).getRedJoin()) || ede.getEntity().equals(Lobby.getLobby(LT.BL).getBlueJoin());
		if(isLobbyRelated){
			ede.setCancelled(true);
		}
	}
}
