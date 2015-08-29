package com.Gbserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;

public class Announce {
	public static void registerEvents() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			public void run() {
				Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Announcement> " + ChatColor.GRAY + "The server's site is at http://mwms.mooo.com/mc/ in bootiful Materialize API.");
			}
		}, 100L, toTicks(5));
	}
	
	private static long toTicks(int minutes){
		return minutes * 60 * 20;
	}
}
