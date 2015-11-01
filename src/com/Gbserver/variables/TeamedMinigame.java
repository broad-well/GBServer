package com.Gbserver.variables;

import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class TeamedMinigame extends Minigame{
    //--
    public static List<Player> bluePlayers;
    public static List<Player> redPlayers;


    public static List<Player> allPlayers() {
        List<Player> target = new LinkedList<>();
        target.addAll(bluePlayers);
        target.addAll(redPlayers);
        return target;
    }
    public static TeamColor whichTeam(Player p){
        if(!allPlayers().contains(p)) return null;
        if(bluePlayers.contains(p)) return TeamColor.BLUE;
        if(redPlayers.contains(p)) return TeamColor.RED;
        return null;
    }


    public static boolean addPlayer(Player p, TeamColor color){
        if(allPlayers().contains(p)) return false;
        switch(color){
            case BLUE: bluePlayers.add(p); break;
            case RED: redPlayers.add(p); break;
        }
        return true;
    }



     public static void tellPlayers(String message){
        for(Player p : allPlayers()){
            ChatWriter.writeTo(p, ChatWriterType.GAME, message);
        }
    }
}
