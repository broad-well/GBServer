package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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
            if(Runner.players.contains(sender) && Runner.isRunning){
                Runner.players.remove(sender);
            }
        }
        return true;
    }
}