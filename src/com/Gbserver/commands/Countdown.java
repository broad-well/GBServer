package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.Gbserver.Main;

public class Countdown {
	private static int count = 0;
	public static void getScoreboard() {
		final Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		final Objective obj = sb.registerNewObjective("countdown", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName("Countdown");
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			public void run() {
				count++;
				obj.getScore("Seconds: ").setScore(count);
				for(Player p : Bukkit.getOnlinePlayers()){
					p.setScoreboard(sb);
				}
			}
		}, 60L, 20L);
	}
	
	
}
