package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;

public class Countdown {
	private Runnable toRun;
	private int taskID;
	private int countdown;
	
	public Countdown(int from, final Runnable t){
		final ScoreDisplay sd = new ScoreDisplay("Countdown");
		countdown = from;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class),
				new Runnable() {

					@Override
					public void run() {
						if(countdown == 0){
							t.run();
							Bukkit.getScheduler().cancelTask(taskID);
						}else{
							countdown--;
							sd.setLine("In", 1);
							sd.setLine(""+countdown, 2);
							sd.display();
						}
					}
			
			
		}, 0L, 20L);
	}
	
	public void close() throws Throwable {
		taskID = 0;
		countdown = 0;
		toRun = null;
		this.finalize();
	}
}
