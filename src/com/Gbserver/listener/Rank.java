package com.Gbserver.listener;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class Rank {
    public String text;
    public ChatColor color;

    public Rank(String text, ChatColor color){
        this.text = text;
        this.color = color;
    }

    public String getPrefix() {
        return color.toString() + ChatColor.BOLD + text + " ";
    }

    public String configOutput() {
        return text + "," + get(color);
    }



    //------------------




    public static Rank fromConfig(String configEntry) {
        String[] data = configEntry.split(",");
        return new Rank(data[0], getColor(data[1]));
    }
    private static String get(ChatColor value){
        for(Map.Entry<String, ChatColor> entry : fromString.entrySet()){
            if(entry.getValue() == value){
                return entry.getKey();
            }
        }
        return "";
    }

    private static ChatColor getColor(String str){
        return fromString.get(str);
    }
    //16  values
    public static final HashMap<String, ChatColor> fromString = new HashMap<String, ChatColor>() {{
        put("black", ChatColor.BLACK);
        put("white", ChatColor.WHITE);
        put("red", ChatColor.RED);
        put("dark_red", ChatColor.DARK_RED);
        put("aqua", ChatColor.AQUA);
        put("blue", ChatColor.BLUE);
        put("dark_blue", ChatColor.DARK_BLUE);
        put("green", ChatColor.GREEN);
        put("dark_green", ChatColor.DARK_GREEN);
        put("light_purple", ChatColor.LIGHT_PURPLE);
        put("dark_purple", ChatColor.DARK_PURPLE);
        put("gray", ChatColor.GRAY);
        put("dark_gray", ChatColor.DARK_GRAY);
        put("dark_aqua", ChatColor.DARK_AQUA);
        put("gold", ChatColor.GOLD);
        put("yellow", ChatColor.YELLOW);
    }};
}
