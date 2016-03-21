package com.Gbserver.variables;

import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;

public class CPrefix implements Mappable {
    public static class Prf {
        public static final CPrefix ANNOUNCEMENT = new CPrefix("Announcement", 0, ChatColor.YELLOW);
        public static final CPrefix ERROR = new CPrefix("Error", ChatColor.RED);
        public static final CPrefix GAME = new CPrefix("Game");
        public static final CPrefix CONDITION = new CPrefix("Condition");
        public static final CPrefix CHAT = new CPrefix("Command");
        public static final CPrefix SERVER = new CPrefix("Server", 0, ChatColor.YELLOW);
        public static final CPrefix COMMAND = new CPrefix("Command");
        //public static final CPrefix JOIN = new CPrefix("Join", ChatColor.DARK_AQUA);
        public static final CPrefix EVENT = new CPrefix("Event", 0, ChatColor.YELLOW);
    }

    final ChatColor DEFAULT_TAGCOLOR = ChatColor.BLUE;
    final ChatColor DEFAULT_TXTCOLOR = ChatColor.GRAY;

    ChatColor tagColor = DEFAULT_TAGCOLOR;
    ChatColor txtColor = DEFAULT_TXTCOLOR;
    String prefix;

    public CPrefix(String tag) {
        prefix = tag;
    }

    public CPrefix(String tag, ChatColor tagcolor) {
        prefix = tag;
        tagColor = tagcolor;
    }

    public CPrefix(String tag, Integer Null, ChatColor txtcolor) {
        prefix = tag;
        txtColor = txtcolor;
    }

    public CPrefix(String tag, ChatColor color, ChatColor txtcolor) {
        this(tag, color);
        txtColor = txtcolor;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}{1}> {2}", tagColor.toString(), prefix, txtColor.toString());
    }

    public HashMap<String, String> toMap() {
        return new HashMap<String, String>() {{
            put("TagColor", tagColor.name());
            put("Prefix", prefix);
            put("TextColor", txtColor.name());
        }};
    }

    public static CPrefix fromMap(HashMap<String, String> map) {
        for (String elem : Arrays.asList("TagColor", "Prefix", "TextColor")) {
            assert map.containsKey(elem);
        }
        return new CPrefix(map.get("Prefix"),
                ChatColor.valueOf(map.get("TagColor")),
                ChatColor.valueOf(map.get("TextColor")));
    }
}
