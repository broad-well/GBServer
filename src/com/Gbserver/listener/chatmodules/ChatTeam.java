package com.Gbserver.listener.chatmodules;

import com.Gbserver.commands.Team;
import com.Gbserver.listener.ChatModule;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.GameType;
import com.Gbserver.variables.LT;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * Created by michael on 1/30/16.
 */
public class ChatTeam implements ChatModule {
    @Override
    public String getName() {
        return "Team chatting module";
    }

    @Override
    public HashMap<String, String> passThru(HashMap<String, String> hs) {
        if (hs.get("msg").startsWith("@")) {
            hs.put("enabled", "false");
            String message = hs.get("msg").substring(1);
            Chat c;
            if ((c = getChat(Bukkit.getPlayer(hs.get("sender")))) != null) {
                for (Player p : c.getPlayersInChat()) {
                    p.sendMessage(ChatColor.BOLD + "TEAM " + ChatColor.GRAY + Bukkit.getPlayer(hs.get("sender")).getName() + " " + ChatColor.RESET + message);
                }
                Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "TEAM " + ChatColor.GRAY + Bukkit.getPlayer(hs.get("sender")).getName() + " " + ChatColor.RESET + message);
            } else {
                ChatWriter.writeTo(Bukkit.getPlayer(hs.get("sender")), ChatWriterType.ERROR, "You are not in any team right now.");
            }
        }
        return hs;
    }

    public static Chat getChat(Player p) {
        GameType[] types = {GameType.TF, GameType.BL, GameType.CTF};

        for (GameType gt : types) {
            if (gt.blue.contains(p)) {
                return new Chat(gt.type, Team.BLUE);
            }
            if (gt.red.contains(p)) {
                return new Chat(gt.type, Team.RED);
            }
        }
        return null;
    }
}
class Chat {
    public LT type;
    public Team team;

    public Chat(LT t, Team e) {
        type = t;
        team = e;
    }

    public List<Player> getPlayersInChat() {
        GameType gt;
        switch (type) {
            case TF:
                gt = GameType.TF;
                break;
            case BL:
                gt = GameType.BL;
                break;
            case CTF:
                gt = GameType.CTF;
                break;
            default:
                gt = null;
                return null;
        }
        switch (team) {
            case BLUE:
                return gt.blue;
            case RED:
                return gt.red;
            default:
                return null;
        }
    }
}
