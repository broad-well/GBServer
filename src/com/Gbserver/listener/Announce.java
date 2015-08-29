package com.Gbserver.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;

public class Announce {
	public static List<String> announcements = Arrays.asList("Contact me about bugs, features, and ranks here: http://mwms.mooo.com/mc/contact.html. (Click on it)", 
			"Explore the GitHub page of this plugin: https://github.com/michaelpeng2002/GBServer. (Click on it)");
	private static int i = 0;
	public static void registerEvents() {

		final String[] ann = (String[]) announcements.toArray();
		for(i = 0; i < ann.length; i++){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
				public void run() {
					Bukkit.broadcastMessage(ChatColor.BLUE + "Announcement> " + ChatColor.YELLOW + ann[i]);
				}
			}, toTicks(5*i), toTicks(5*ann.length));
		}
	}
	
	private static long toTicks(int minutes){
		return minutes * 60 * 20;
	}
}
