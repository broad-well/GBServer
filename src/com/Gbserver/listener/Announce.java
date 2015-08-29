package com.Gbserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;

public class Announce {
	public static void registerEvents() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			public void run() {
				Bukkit.broadcastMessage(ChatColor.BLUE + "Announcement> " + ChatColor.WHITE + "Check out my GitHub page containing the source code of my plugin! https://github.com/michaelpeng2002/GBServer/");
			}
		}, 0L, toTicks(10));

	

		Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			public void run() {
				Bukkit.broadcastMessage(ChatColor.BLUE + "Announcement> " + ChatColor.WHITE + "You can submit bugged/wanted features of my plugin on the GitHub page. https://github.com/michaelpeng2002/GBServer/issues/3");
			}
		}, toTicks(5), toTicks(10));
	}
	
	private static long toTicks(int minutes){
		return minutes * 60 * 20;
	}
}
