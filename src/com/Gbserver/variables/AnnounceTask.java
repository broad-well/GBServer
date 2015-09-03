package com.Gbserver.variables;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;
import com.Gbserver.listener.Announce;

public class AnnounceTask {
	public int announceNumber;
	private int taskID;

	public static  class Tasks {
		public static List<AnnounceTask> tasks = new LinkedList<>();

		public AnnounceTask getTask(int arg) {
			for (AnnounceTask at : tasks) {
				if (at.announceNumber == arg) {
					return at;
				}
			}
			return new AnnounceTask(arg);
		}
		
		public void reload() {
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
								+ Announce.announcements[0]);

					} catch (Exception e) {
						Bukkit.broadcastMessage(
								ChatColor.BLUE + "Error> " + ChatColor.RED + "ERROR! " + e.getMessage());
					}
			
			}
		}, Announce.toTicks(announceNumber * 5), Announce.toTicks(5 * Announce.announcements.length));
		Tasks.tasks.add(this);
	}

	public void setDisabled() {
		Bukkit.getScheduler().cancelTask(taskID);
		
	}

}
