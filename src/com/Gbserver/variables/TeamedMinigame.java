package com.Gbserver.variables;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 10/31/15.
 */
public class TeamedMinigame extends Minigame{
    //--
    public List<Player> bluePlayers;
    public List<Player> redPlayers;

    public TeamedMinigame(String name, World world) {
        super(name, world);
    }

    public List<Player> allPlayers() {
        List<Player> target = new LinkedList<>();
        target.addAll(bluePlayers);
        target.addAll(redPlayers);
        return target;
    }
    public TeamColor whichTeam(Player p){
        if(!allPlayers().contains(p)) return null;
        if(bluePlayers.contains(p)) return TeamColor.BLUE;
        if(redPlayers.contains(p)) return TeamColor.RED;
        return null;
    }


    public boolean addPlayer(Player p, TeamColor color){
        if(allPlayers().contains(p)) return false;
        switch(color){
            case BLUE: bluePlayers.add(p); break;
            case RED: redPlayers.add(p); break;
        }
        return true;
    }



    public void tellPlayers(String message){
        for(Player p : allPlayers()){
            ChatWriter.writeTo(p, ChatWriterType.GAME, message);
        }
    }
}
