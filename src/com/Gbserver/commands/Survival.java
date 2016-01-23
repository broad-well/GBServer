package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.LinkedList;
import java.util.List;

public class Survival implements CommandExecutor, Listener {
    public static List<Player> survivors = new LinkedList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Utilities.validateSender(sender) && Utilities.validateGamePlay(sender)){
            if(survivors.contains(sender)){
                survivors.remove(sender);
                ((Player) sender).setGameMode(GameMode.CREATIVE);
                ChatWriter.writeTo(sender, ChatWriterType.CONDITION, "You are no longer in " + ChatColor.YELLOW + "Survival Mode");
            }else{
                survivors.add((Player) sender);
                ((Player) sender).setGameMode(GameMode.SURVIVAL);
                ChatWriter.writeTo(sender, ChatWriterType.CONDITION, "You are now in " + ChatColor.YELLOW + "Survival Mode");
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe){
        survivors.remove(pqe.getPlayer());
    }

    public static boolean checkSurvival(CommandSender sender){
        if(survivors.contains(sender)){
            ChatWriter.writeTo(sender, ChatWriterType.CONDITION, "This command is not permitted for anyone in " +
                    ChatColor.YELLOW + "Survival Mode" + ChatColor.GRAY + ".");
            return false;
        }else return true;
    }
}
