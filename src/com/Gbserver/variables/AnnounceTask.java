package com.Gbserver.variables;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;
import com.Gbserver.listener.Announce;

public class AnnounceTask {
	public static int announceNumber;
	private static int taskID;

	public static class Tasks {
		public static List<AnnounceTask> tasks = new LinkedList<>();

		public static AnnounceTask getTask(int arg) {
			for (AnnounceTask at : tasks) {
				if (at.announceNumber == arg) {
					return at;
				}
			}
			return new AnnounceTask(arg);
		}
		
		public static void reload() {
			for (AnnounceTask at : tasks){
				at.setDisabled();
				int am = at.announceNumber;
				at = new AnnounceTask(am);
			}
		}
	}

	public AnnounceTask(int arg) {
		announceNumber = arg;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			public void run() {
					try {
						Bukkit.broadcastMessage(ChatColor.BLUE + "Announcement> " + ChatColor.YELLOW
								+ Announce.announcements.toArray()[announceNumber]);

					} catch (Exception e) {
						Bukkit.broadcastMessage(
								ChatColor.BLUE + "Error> " + ChatColor.RED + "ERROR! " + e.getMessage());
					}
			
			}
		}, Announce.toTicks(announceNumber * 5), Announce.toTicks(5 * Announce.announcements.size()));
		Tasks.tasks.add(this);
	}

	public static void setDisabled() {
		Bukkit.getScheduler().cancelTask(taskID);
		
	}

}
