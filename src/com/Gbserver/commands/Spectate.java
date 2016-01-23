package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by michael on 1/22/16.
 */
public class Spectate implements CommandExecutor{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(Utilities.validateSender(sender) && Utilities.validateGamePlay(sender)){
            if(args.length == 0){
                sender.sendMessage(ChatColor.DARK_AQUA + "/spec Usage: /spec <player>");
                sender.sendMessage(ChatColor.DARK_AQUA + "The <player> is the name of the person you wish to spectate.");
            }else{
                Player p = (Player) sender;
                if(Bukkit.getPlayer(args[0]) == null){
                    ChatWriter.writeTo(sender, ChatWriterType.ERROR, "I cannot find " + ChatColor.YELLOW + args[0] + ChatColor.GRAY + " online.");
                }else{
                    p.setGameMode(GameMode.SPECTATOR);
                    p.teleport(Bukkit.getPlayer(args[0]));
                }
            }
        }
        return true;
    }
}
