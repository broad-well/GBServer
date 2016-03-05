package com.Gbserver.variables;

import org.bukkit.ChatColor;

/**
 * Shortcuts for extremely common & redundant code.
 */
public class Sh {
    /**
     * Parses the name of a ChatColor.
     *
     * @param chatColor The name of the ChatColor.
     * @return The string representation of the ChatColor.
     */
    public static String pc(String chatColor) {
        return ChatColor.valueOf(chatColor.toUpperCase()).toString();
    }
}
