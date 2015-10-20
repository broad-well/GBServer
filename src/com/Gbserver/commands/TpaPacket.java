package com.Gbserver.commands;

import org.bukkit.entity.Player;

public class TpaPacket {
    private Player o;
    private Player t;
    private boolean Tpa;

    public TpaPacket(Player origin, Player target, boolean isTPA) {
        o = origin;
        t = target;
        Tpa = isTPA;
    }

    public Player getOrigin() {
        return o;
    }

    public Player getTarget() {
        return t;
    }

    public boolean isTPA() {
        return Tpa;
    }
}
