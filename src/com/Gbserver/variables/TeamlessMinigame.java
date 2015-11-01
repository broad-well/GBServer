package com.Gbserver.variables;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

/**
 * Created by michael on 10/31/15.
 */
public class TeamlessMinigame extends Minigame{
    public List<Player> players;

    public TeamlessMinigame(String name, World world1) {
        super(name, world1);
    }

    public boolean addPlayer(Player p){
        if(players.contains(p)) return false;
        players.add(p);
        return true;
    }public void addPlayers(Collection<? extends Player> players){
        for(Player p : players){
            addPlayer(p);
        }
    }

    public void tellPlayers(String message){
        for(Player p : players){
            ChatWriter.writeTo(p, ChatWriterType.GAME, message);
        }
    }
}
