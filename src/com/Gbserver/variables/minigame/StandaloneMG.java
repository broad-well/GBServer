package com.Gbserver.variables.minigame;

import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public abstract class StandaloneMG extends Minigame {
    private List<Player> players = new LinkedList<>();

    public List<Player> getPlayers(){return players;}
}
