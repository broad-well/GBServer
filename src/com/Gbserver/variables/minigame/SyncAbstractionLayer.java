package com.Gbserver.variables.minigame;

import com.Gbserver.Utilities;
import com.Gbserver.variables.DebugLevel;
import org.bukkit.Bukkit;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SyncAbstractionLayer {
    private static DebugLevel dl = new DebugLevel(3, "Synchronized Abstraction Layer");

    public static void syncExec(final Runnable run, String identity){
        //Locks until the runnable is finished.
        dl.debugWrite("Received new job from " + identity);
        try {
            final Lock l = new ReentrantLock();
            l.lock();
            Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(), new Runnable() {
                public void run() {
                    run.run();
                    l.unlock();
                }
            });
            l.lock(); l.unlock();
        }catch(Exception e){
            Bukkit.getLogger().warning("SAL Process Interrupted - Stack Trace follows:");
            e.printStackTrace();
        }
    }
}
