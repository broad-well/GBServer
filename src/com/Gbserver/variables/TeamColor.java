package com.Gbserver.variables;

import org.bukkit.ChatColor;

import java.awt.*;

/**
 * Created by michael on 10/31/15.
 */
public enum TeamColor {
    BLUE,RED,YELLOW,GREEN,PURPLE,ORANGE;
    public String toString() {
        switch(this){
            case BLUE: return "Blue";
            case RED: return "Red";
            case YELLOW: return "Yellow";
            case GREEN: return "Green";
            case PURPLE: return "Purple";
            case ORANGE: return "Orange";
        }
        return null;
    }

    public ChatColor toColor() {
        switch(this){
            case BLUE: return ChatColor.BLUE;
            case RED: return ChatColor.RED;
            case YELLOW: return ChatColor.YELLOW;
            case GREEN: return ChatColor.GREEN;
            case PURPLE: return ChatColor.DARK_PURPLE;
            case ORANGE: return ChatColor.GOLD;
        }
        return null;
    }

    //--

    public static TeamColor fromColor(ChatColor c){
        if(c == ChatColor.BLUE) return BLUE;
        if(c == ChatColor.RED) return RED;
        if(c == ChatColor.YELLOW) return YELLOW;
        if(c == ChatColor.GREEN) return GREEN;
        if(c == ChatColor.DARK_PURPLE) return PURPLE;
        if(c == ChatColor.GOLD) return ORANGE;
        return null;
    }

    public static TeamColor fromString(String str){
        switch(str){
            case "Blue": return BLUE;
            case "Red": return RED;
            case "Yellow": return YELLOW;
            case "Green": return GREEN;
            case "Purple": return PURPLE;
            case "Orange": return ORANGE;
        }
        return null;
    }
}
