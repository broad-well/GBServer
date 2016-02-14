package com.Gbserver.variables;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IgnoreList {
    public static HashMap<OfflinePlayer, List<OfflinePlayer>> list = new HashMap<>();

    public static List<OfflinePlayer> smartGet(OfflinePlayer p){
        return list.get(p) == null ?
                new LinkedList<OfflinePlayer>() :
                list.get(p);
    }

    public static void ignore(OfflinePlayer p, OfflinePlayer origin){
        List<OfflinePlayer> li = list.get(origin);
        if(li == null) li = new LinkedList<>();
        li.add(p);
        list.put(origin, li);
    }

    public static void unignore(OfflinePlayer p, OfflinePlayer origin){
        List<OfflinePlayer> li = list.get(origin);
        if(li == null) li = new LinkedList<>();
        li.remove(p);
        list.put(origin, li);
    }

    public static boolean isIgnored(OfflinePlayer p, OfflinePlayer origin){
        List<OfflinePlayer> li = list.get(origin);
        if(li == null) li = new LinkedList<>();
        return li.contains(p);
    }
    //--------------------
    public static void output() {
        for(Map.Entry<OfflinePlayer, List<OfflinePlayer>> entry : list.entrySet()){
            EnhancedPlayer.getEnhanced(entry.getKey()).setIgnoreList(entry.getValue());
        }
    }

    public static void input() {
        list.clear();
        for(EnhancedPlayer ep : EnhancedPlayer.cache){
            list.put(ep.toPlayer(), ep.getIgnoreList());
        }
    }
}
