package com.Gbserver.listener;

import com.Gbserver.variables.EnhancedPlayer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 1/22/16.
 */
public class Birthday implements Listener{
    private static final int MONTH = 0;
    private static final int DAY = 1;

    public static HashMap<String, List<Integer>> birthData = new HashMap<>();

    public static List<OfflinePlayer> whosBirthday() {
        //Nullable
        List<OfflinePlayer> returning = new LinkedList<>();
        for(EnhancedPlayer ep : EnhancedPlayer.cache){
            if(ep.getBirthday() != null){
                if(ep.getBirthday()[MONTH] == Calendar.getInstance().get(Calendar.MONTH) &&
                        ep.getBirthday()[DAY] == Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                    returning.add(ep.toPlayer());
            }
        }
        return returning;
    }

    //Actual listeners
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        for(OfflinePlayer birthdayers : whosBirthday()){
            if(pje.getPlayer().getUniqueId().equals(birthdayers.getUniqueId())){
                pje.getPlayer().sendMessage(ChatColor.BLUE + "Broadwell: " + ChatColor.YELLOW + "Happy birthday, " + pje.getPlayer().getName() + "!");
            }else {
                pje.getPlayer().sendMessage(ChatColor.BLUE + "Birthday> " + ChatColor.YELLOW + "It's " + birthdayers.getName() + "'s birthday! " +
                        ChatColor.ITALIC + "Wish him/her a happy birthday!");
            }
        }
    }
}
