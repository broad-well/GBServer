package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import com.Gbserver.variables.minigame.Games;
import com.Gbserver.variables.minigame.MGUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Spawn implements CommandExecutor {
    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender) && Utilities.validateGamePlay(sender) && Survival.checkSurvival(sender)) {
            Player player = (Player) sender;
            player.teleport(new Location(Bukkit.getWorld("world"), 145, 119, 413));
            player.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Successfully teleported " + ChatColor.YELLOW + player.getName() +
                    ChatColor.GRAY + " to the spawn."));
            if (MGUtils.utilAccess.get(Games.RUNNER).mg.getPlayers().contains(sender)) {
                MGUtils.utilAccess.get(Games.RUNNER).eliminate((Player) sender);
            }
        }
        return true;
    }
}