package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.Gbserver.Main;

public class Countdown {
	public static int count = 0;
	public static boolean isFinished = false;
	public static Objective obj = null;
	public static Scoreboard sb = null;
	/*
	 * Use getScoreboard in conjunction with isFinished.
	 */
	public static void getScoreboard(String name) {
		
		sb = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = sb.registerNewObjective("countdown", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(name);
		
	}
	
	public static void reset() {
		count = 0;
		isFinished = false;
		obj = null;
		sb = null;
		id = 0;
		
	}
	
	static int id;
	public static void addScore(String message, int countMax){
		count = countMax;
		final Score s = obj.getScore(message + ": ");
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			public void run() {
				if(count > 0){
					count--;
					s.setScore(count);
					
					for(Player p : Bukkit.getOnlinePlayers()){
						p.setScoreboard(sb);
					}
				}else{
					isFinished = true;
					
					Bukkit.getScheduler().cancelTask(id);
				}
				
			}
		}, 0L, 20L);
	}
	
	
}
