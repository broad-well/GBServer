package com.Gbserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;

public class JoinListener implements Listener {
	
	boolean answered = true;
	static PlayerJoinEvent pje;

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent pje) {
		Player p = Bukkit.getServer().getPlayer("_Broadwell");
		JoinListener.pje = pje;
		if (isOnline(p)) {
			p.sendMessage("Someone is online!");
			for (int i = 0; i < 100; i++) {
				p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(1, Tone.G));
			}
		}
	}

	private boolean isOnline(Player p) {
		if (p != null) {
			return p.isOnline();
		} else {
			return false;
		}
	}
}
