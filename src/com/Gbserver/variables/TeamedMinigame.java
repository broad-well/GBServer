package com.Gbserver.variables;

import com.Gbserver.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.*;

public class TeamedMinigame extends Minigame{
    private static final List<TeamColor> config = Arrays.asList(TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, TeamColor.PURPLE,
            TeamColor.YELLOW, TeamColor.ORANGE);
    public static class DefaultTeamColorConfiguration {
        public static ArrayList<TeamColor> getDefault(int count){
            return count <= 1 || count > TeamColor.values().length ?
                    null : new ArrayList<>(config.subList(0, count));
        }
    }
    private int teamCount;
    public ArrayList<List<UUID>> teamPlayers;
    private ArrayList<TeamColor> teamColors;

    public TeamedMinigame(String name, String ident, World world, int teamCount) {
        super(name, ident, world);
        if(teamCount <= 1 || teamCount > TeamColor.values().length) return;
        this.teamCount = teamCount;
        teamPlayers = new ArrayList<>();
        teamPlayers.ensureCapacity(teamCount);
        teamColors = DefaultTeamColorConfiguration.getDefault(teamCount);
    }

    public TeamColor colorOfTeamIndex(int index){
        return teamColors.get(index);
    }

    @Override
    public List<UUID> players() {
        List<UUID> build = new LinkedList<>();
        for(List<UUID> team : teamPlayers){
            build.addAll(team);
        }
        return build;
    }

    public TeamColor teamOf(UUID player){
        if(!players().contains(player)) return null;
        for(List<UUID> team : teamPlayers){
            if(team.contains(player)){
                return teamColors.get(teamPlayers.indexOf(team));
            }
        }
        return null; //Program should not reach this point.
    }

    public List<UUID> getTeamFromColor(TeamColor tc){
        return !teamColors.contains(tc) ? null : teamPlayers.get(teamColors.indexOf(tc));
    }

    public TeamColor join(UUID pl){
        try {
            //Not if the player is in the game!
            if (players().contains(pl)) return null;

            //Apply with appropriate / random team. Analyze to see which team has the lowest players. If all teams are equal, choose a random team.
            int cond = teamCondition();
            if (cond != -1) {
                //Sizes not the same. Use the output from teamCondition().
                teamPlayers.get(cond).add(pl); //Team alter
                return teamColors.get(cond);
            } else {
                //Choose a random team to join. Or, if the player has color preference, join the color.
                EnhancedPlayer enpl = EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(pl));
                if (enpl.getColorPref() != null){
                    //If this minigame has the color, join that color. Else choose a random one.
                    if (teamColors.contains(enpl.getColorPref())){
                        teamPlayers.get(teamColors.indexOf(enpl.getColorPref())).add(pl); //Team alter
                        return enpl.getColorPref();
                    } else {
                        //No color
                        int selection = Utilities.getRandom(0, teamPlayers.size());
                        teamPlayers.get(selection).add(pl); //Team alter
                        return teamColors.get(selection);
                    }
                } else {
                    //No color
                    int selection = Utilities.getRandom(0, teamPlayers.size());
                    teamPlayers.get(selection).add(pl); //Team alter
                    return teamColors.get(selection);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean attemptSwitch(UUID pl, TeamColor target){
        //If the target color does not exist here, stop.
        if(!teamColors.contains(target)) return false;

        //If the player is not in the game or is already in the target color, stop.
        if(!players().contains(pl) || teamPlayers.get(teamColors.indexOf(target)).contains(pl)) return false;

        //Inop.
        return true;
    }
    private int teamCondition() { //Returns the index of the team with the least players, or -1 if all teams' size are the same.
        //Initialize the sizes list for computation.
        List<Integer> sizes = new LinkedList<>();
        for(List<UUID> team : teamPlayers) sizes.add(team.size());

        //See if the teams' size are the same. If so, return -1.
        {
            int standard = sizes.get(0);
            boolean same = true;
            for (Integer i : sizes) {
                if (i != standard) {
                    same = false;
                    break;
                }
            }
            if (same) return -1;
        }

        //Find the one with the least players.
        int least = Integer.MAX_VALUE;
        for(Integer i : sizes){
            if(i < least) least = i;
        }

        //Use random to figure out whether to use forward checking or backward checking.
        return Utilities.getRandom(0, 2) == 1 ?
                sizes.indexOf(least) :
                sizes.lastIndexOf(least);
    }

}
