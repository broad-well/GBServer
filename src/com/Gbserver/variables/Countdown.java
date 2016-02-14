package com.Gbserver.variables;

import com.Gbserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Countdown {
    private Runnable toRun;
    private int taskID;
    private int countdown;
    private String message;

    public Countdown(int from, final Runnable t, String origin) {
        final ScoreDisplay sd = new ScoreDisplay("Countdown");
        countdown = from;
        message = origin;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class),
                new Runnable() {

                    @Override
                    public void run() {
                        if (countdown == 0) {
                            t.run();
                            Bukkit.getScheduler().cancelTask(taskID);
                        } else {
                            countdown--;
                            sd.setLine(message + " in", 1);
                            sd.setLine(String.valueOf(countdown), 2);
                            sd.display();
                        }
                    }


                }, 0L, 20L);
    }

    public Countdown(int from, final Runnable t, String origin, List<Player> target) {
        final ScoreDisplay sd = new ScoreDisplay("Countdown");
        countdown = from;
        message = origin;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class),
                new Runnable() {

                    @Override
                    public void run() {
                        if (countdown == 0) {
                            t.run();
                            Bukkit.getScheduler().cancelTask(taskID);
                        } else {
                            countdown--;
                            sd.setLine(message + " in", 1);
                            sd.setLine(String.valueOf(countdown), 2);
                            sd.display();
                        }
                    }


                }, 0L, 20L);
    }

    public void close() throws Throwable {
        taskID = 0;
        countdown = 0;
        toRun = null;
    }
}
