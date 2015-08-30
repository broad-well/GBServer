package com.Gbserver.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;

public class Announce {
	public static List<String> announcements = Arrays.asList(
			
			"Contact me about bugs, features, and ranks here: http://mwms.mooo.com/mc/contact.html. (Click on it)", 
			"Explore the GitHub page of this plugin: https://github.com/michaelpeng2002/GBServer. (Click on it)"
			
			);
	public static void registerEvents() {

			Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
				public void run() {
					
					try{
					Bukkit.broadcastMessage(ChatColor.BLUE + "Announcement> " + ChatColor.YELLOW + announcements.toArray()[0]);
					
					}catch(Exception e){
						Bukkit.broadcastMessage(ChatColor.BLUE + "Error> " + ChatColor.RED + "ERROR! " + e.getMessage());
					}
				}
			}, toTicks(0), toTicks(5*announcements.size()));
			Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
				public void run() {
					
					try{
					Bukkit.broadcastMessage(ChatColor.BLUE + "Announcement> " + ChatColor.YELLOW + announcements.toArray()[1]);
					
					}catch(Exception e){
						Bukkit.broadcastMessage(ChatColor.BLUE + "Error> " + ChatColor.RED + "ERROR! " + e.getMessage());
					}
				}
			}, toTicks(1*5), toTicks(5*announcements.size()));

	}
	
	private static long toTicks(int minutes){
		return minutes * 60 * 20;
	}
}
