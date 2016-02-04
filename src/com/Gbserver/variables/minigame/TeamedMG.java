package com.Gbserver.variables.minigame;

import com.Gbserver.variables.TeamColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class TeamedMG extends Minigame{
    private HashMap<TeamColor, List<Player>> players = new HashMap<>();

    public List<Player> getPlayers() {
        List<Player> build = new ArrayList<>();
        for(List<Player> team : players.values()){
            build.addAll(team);
        }
        return build;
    }
}
