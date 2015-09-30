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
	public static long toTicks(int minutes) {
		return minutes * 60 * 20;
	}
	
	public static Main getPlugin() {
		return plugin;
	}
}
