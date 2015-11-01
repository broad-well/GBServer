package com.Gbserver.variables;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class TeamlessMinigame extends Minigame{
    public static List<Player> players;

    public static boolean addPlayer(Player p){
        if(players.contains(p)) return false;
        players.add(p);
        return true;
    }

    public static void addPlayers(Collection<? extends Player> players){
        for(Player p : players){
            addPlayer(p);
        }
    }

    public static void tellPlayers(String message){
        for(Player p : players){
            ChatWriter.writeTo(p, ChatWriterType.GAME, message);
        }
    }
}
