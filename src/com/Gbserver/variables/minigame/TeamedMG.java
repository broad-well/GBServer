package com.Gbserver.variables.minigame;

import com.Gbserver.variables.TeamColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public interface TeamedMG extends Minigame {

    //HashMap<TeamColor, List<Player>>
    HashMap<TeamColor, List<Player>> getTeamConfig();
}