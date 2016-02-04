package com.Gbserver.variables.minigame;

import com.Gbserver.Utilities;
import com.Gbserver.variables.DebugLevel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.LinkedList;
import java.util.List;

public class MGUtils {
    private static DebugLevel dl = new DebugLevel(3, "MinigameHelper");
    public static final int[] GAMEPLAY_LOCATION = {0,100,0};
    //-----
    private Minigame mg;
    public String selectedMap = "";
    public Objective scoreboard;
    private Scoreboard sb;

    public MGUtils(Minigame m){
        mg = m;
    }

    public void fixDuplicatePlayers() {
        List<Player> encountered = new LinkedList<>();
        List<Integer> toDelete = new LinkedList<>();
        for(Player p : mg.getPlayers()){
            if(encountered.contains(p)){
                dl.debugWrite("While fixing duplicate players, found duplicate name:" + p.getName() +
                        " in minigame:" + mg.identifier);
                toDelete.add(mg.getPlayers().indexOf(p));
            }else {
                encountered.add(p);
            }
        }
        for(Integer i : toDelete){
            encountered.remove(i.intValue());
        }
        //Finished.
        toDelete.clear(); encountered.clear();
    }

    public void startGame() throws IllegalStateException {
        if (mg.runlevel != 2) throw new IllegalStateException("Invalid Run-level for starting game.");
        mg.runlevel = 3;
        //Launch threads.
        mg.startProcedure.run();
        for (Runnable run : mg.threads) {
            dl.debugWrite(mg.identifier + ": New Runnable submitted to ExecutorService: " + run.toString());
            mg.executorService.submit(run);
        }
        //Available maps?
        if (selectedMap == null) throw new IllegalStateException("Map not set for starting game.");

    }

    /*
    Scoreboard format:
    [bold color italic][game name]

    [bold color]Players
    [count]/[max]

    [bold color]Map
    [mapName]

    [bold color]Seconds Till Start
    [countdown]
     */

    public void initialize() {

    }

    public void refreshLobbyScoreboard() { //Call this when someone joins the lobby.
        //Obtain scoreboard for lobby.
        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard = sb.registerNewObjective(ChatColor.DARK_GREEN + ChatColor.ITALIC.toString() + mg.identifier + " Lobby", "dummy");
        scoreboard.getScore("").setScore(0);
        scoreboard.getScore(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Players").setScore(1);
        scoreboard.getScore(mg.getPlayers().size() + "/" + mg.maxPlayers).setScore(2);
        scoreboard.getScore(" ").setScore(3);
        scoreboard.getScore(ChatColor.GOLD.toString() + ChatColor.BOLD + "Map").setScore(4);
        scoreboard.getScore(selectedMap).setScore(5);
        scoreboard.getScore("  ").setScore(6);
        scoreboard.setDisplaySlot(DisplaySlot.SIDEBAR);
        for(Player p : mg.getPlayers()){
            p.setScoreboard(sb);
        }
    }

    public void prepareforStart() {
        if(selectedMap == null) {
            Bukkit.getLogger().warning("In game " + mg.identifier + ", map has not been set. " +
                    "Aborting prepareforStart().");
            return;
        }
        //Initialize countdown.

        Utilities.copy(mg.maps.get(selectedMap).getLow(),
                mg.maps.get(selectedMap).getHigh(),
                new Location(mg.world, GAMEPLAY_LOCATION[0], GAMEPLAY_LOCATION[1], GAMEPLAY_LOCATION[2]));
    }

}
