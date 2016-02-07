package com.Gbserver.variables.minigame;


import com.Gbserver.variables.CubicSelection;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public interface Minigame {
    /**
     * Runlevel Documentation:
     * <code>runlevel</code> is a status representation of a Minigame object.
     *
     * Levels:
     * 0 = Not initialized.
     * 1 = Lobby is maintained by a thread.
     * 2 = Lobby countdown in progress.
     * 3 = Game in progress.
     * 4 = Game finished, cleaning up and executing celebration effects.*/

    World getWorld();

    CubicSelection getLobby();

    String getIdentifier();

    Runnable getStartProcedure();

    Runnable getStopProcedure();

    List<Runnable> getThreads();

    List<Player> getSpectators();

    HashMap<String, CubicSelection> getMaps();

    int getMaxPlayers();

    int getPortalId();

    int getRunlevel();

    MGUtils getUtils();

    List<Player> getPlayers();

    void setRunlevel(int level);

    void setPlayers(List<Player> s);

    List<Location> getSpawnpoints(); //This has to be more or equal to the size of max players.
}
