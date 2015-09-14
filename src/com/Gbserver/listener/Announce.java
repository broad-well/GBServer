package com.Gbserver.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.ChatWriter;

public class Announce {
	public static int schedulerCount = 0;
	public static List<String> announcement = new LinkedList<>();
	
	public static void registerEvents() {
		announcement.add("Contact me about bugs, features, and ranks here: " + ChatColor.BOLD + "http://broaderator.com/mc/contact.html NEW!" + ChatColor.RESET + "" + ChatColor.YELLOW + " (Click on it)");
		announcement.add("NEW: The server now has a Twitter! Stay up to date with scheduled downtime, upcoming features and more! "+ChatColor.UNDERLINE+"http://twitter.com/broadwellServer");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

			@Override
			public void run() {
				if(schedulerCount == announcement.size()){
					//Out of bounds..
					//Reset.
					schedulerCount = 0;
				}
					ChatWriter.write(ChatWriterType.ANNOUNCEMENT, announcement.get(schedulerCount));
					schedulerCount++;
			}

		}, 0L, toTicks(10));

	}

	public static long toTicks(int minutes) {
		return minutes * 60 * 20;
	}
}
