package com.Gbserver.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;
import com.Gbserver.variables.AnnounceTask;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.AnnounceTask.Tasks;
import com.Gbserver.variables.ChatWriter;

public class Announce {
	public static int schedulerCount = 0;
	public static String[] announcements = {
			"Contact me about bugs, features, and ranks here: " + ChatColor.BOLD + "http://broaderator.com/mc/contact.html NEW!" + ChatColor.RESET + "" + ChatColor.YELLOW + "(Click on it)",
			"Explore the GitHub page of this plugin: https://github.com/michaelpeng2002/GBServer. (Click on it)", 
			"Contact me if you are interested in having a mail address like boon@broaderator.com!"};

	public static void registerEvents() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

			@Override
			public void run() {
				if(schedulerCount == announcements.length){
					//Out of bounds..
					//Reset.
					schedulerCount = 0;
				}
					ChatWriter.write(ChatWriterType.ANNOUNCEMENT, announcements[schedulerCount]);
					schedulerCount++;
			}

		}, 0L, toTicks(5));

	}

	public static long toTicks(int minutes) {
		return minutes * 60 * 20;
	}
}
