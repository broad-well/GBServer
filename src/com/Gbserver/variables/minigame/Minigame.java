package com.Gbserver.variables.minigame;


import com.Gbserver.variables.CubicSelection;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Minigame {
    public World world;
    public CubicSelection lobby;
    public String identifier;
    public Runnable startProcedure;
    public Runnable stopProcedure;
    public List<Runnable> threads;
    public List<Listener> listeners;
    public HashMap<String, CubicSelection> maps;
    public int maxPlayers;
    public ExecutorService executorService = Executors.newCachedThreadPool();
    /**
     * Runlevel Documentation:
     * <code>runlevel</code> is a status representation of a Minigame object.
     *
     * Levels:
     * 0 = Not initialized.
     * 1 = Lobby is maintained by a thread.
     * 2 = Lobby countdown in progress.
     * 3 = Game in progress.
     * 4 = Game finished, cleaning up and executing celebration effects.
     */
    public int runlevel = 0;

    public abstract List<Player> getPlayers();
    public boolean isRunning() {return runlevel == 3;}
}
