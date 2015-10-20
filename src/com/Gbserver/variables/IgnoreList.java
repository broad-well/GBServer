package com.Gbserver.variables;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class IgnoreList {
    public static List<IgnoreList> list = new LinkedList<>();

    public static IgnoreList getIgnoreList(Player p) {
        for (IgnoreList il : list) {
            if (il.target.equals(p)) {
                return il;
            }
        }
        return new IgnoreList(p);
    }
    //--------------------

    protected Player target;
    private Collection<OfflinePlayer> ignored = new LinkedList<>();

    public IgnoreList(Player p) {
        target = p;
        list.add(this);
    }

    public void addIgnoredPlayer(OfflinePlayer p) {
        ignored.add(p);
    }

    public void removeIgnoredPlayer(OfflinePlayer p) {
        ignored.remove(p);
    }

    public Collection<OfflinePlayer> getIgnoredPlayers() {
        return ignored;
    }

    public boolean isIgnored(OfflinePlayer p) {
        return ignored.contains(p);
    }

    public void close() {
        list.remove(this);
        ignored = null;
        target = null;
    }
}
