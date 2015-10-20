package com.Gbserver.listener;

import com.Gbserver.commands.Bacon;
import com.Gbserver.commands.BaconPlayer;
import com.Gbserver.commands.Quit;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.IgnoreList;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginTagListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje) {
        pje.setJoinMessage(ChatWriter.getMessage(ChatWriterType.JOIN, pje.getPlayer().getName() + " has joined."));
        pje.getPlayer().setCustomName(ChatFormatter.generateTag(pje.getPlayer(), false));
        pje.getPlayer().setCustomNameVisible(true);
        pje.getPlayer().setPlayerListName(ChatFormatter.generateTag(pje.getPlayer(), false));
        new IgnoreList(pje.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe) {
        if (Quit.ragequitters.contains(pqe.getPlayer())) {
            pqe.setQuitMessage(ChatWriter.getMessage(ChatWriterType.QUIT, pqe.getPlayer().getName() + " has " + ChatColor.RED + "" + ChatColor.BOLD + "RAGEQUITTED!"));
            Quit.ragequitters.remove(pqe.getPlayer());
            return;
        }

        if (Quit.afkers.contains(pqe.getPlayer())) {
            pqe.setQuitMessage(ChatWriter.getMessage(ChatWriterType.QUIT, pqe.getPlayer().getName() + " has been AFK removed."));
            Quit.afkers.remove(pqe.getPlayer());
            return;
        }

        if (Quit.diers.contains(pqe.getPlayer())) {
            pqe.setQuitMessage(ChatWriter.getMessage(ChatWriterType.QUIT, pqe.getPlayer().getName() + ChatColor.RED + " has died. RIP."));
            Quit.diers.remove(pqe.getPlayer());
            return;
        }
        pqe.setQuitMessage(ChatWriter.getMessage(ChatWriterType.QUIT, pqe.getPlayer().getName() + " has left."));
        IgnoreList.getIgnoreList(pqe.getPlayer()).close();
        if (Bacon.hasPlayer(pqe.getPlayer())) {
            Bacon.players.remove(BaconPlayer.getByHandle(pqe.getPlayer()));
        }
    }
}
