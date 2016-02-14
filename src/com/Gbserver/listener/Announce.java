package com.Gbserver.listener;

import com.Gbserver.Main;

import java.util.LinkedList;
import java.util.List;

public class Announce {
    public static int schedulerCount = 0;
    public static List<String> announcement = new LinkedList<>();
    public static Main plugin;

    public Announce(Main m) {
        plugin = m;
    }

    public static long toTicks(int minutes) {
        return minutes * 60 * 20;
    }

    public static Main getPlugin() {
        return plugin;
    }
}
