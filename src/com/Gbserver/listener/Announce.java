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
	public static Main plugin;
	
	public Announce(Main m){
		plugin = m;
	}
	public static void registerEvents() {
		announcement.addAll(plugin.getConfig().getStringList("announcements"));
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
	
	public static Main getPlugin() {
		return plugin;
	}
}
