package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.GameType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by michael on 12/22/15.
 */
public class Game implements CommandExecutor{
    private static final String[] subcommands = {
            ChatColor.GOLD + "Available subcommands:",
            ChatColor.AQUA + "join <Game Type>",
            ChatColor.AQUA + "leave [Game Type]",
            ChatColor.AQUA + "team <Color of team> [Game Type]",
            ChatColor.AQUA + "spectate [Game Type]"
    };
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(Sandbox.check(sender)) return true;
        try{
            if(args.length == 0){
                sender.sendMessage(subcommands);
                return true;
            }
            switch(args[0]){
                case "join":
                    //Syntax: /game join <type>
                    if(args.length == 1){
                        sender.sendMessage(ChatColor.YELLOW + "Join usage: /game join <Game Type>");
                        sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.ITALIC + "Example: " + ChatColor.YELLOW + "/game join tf");
                        break;
                    }
                    if(Games.valueOf(args[1]) == null){
                        ChatWriter.writeTo(sender, ChatWriterType.ERROR, "Game not found for input " + ChatColor.YELLOW + args[1]);
                        break;
                    }
                    //Check if the player is globally in any game. TBI

                    break;
                case "leave":
                    //Syntax: /game leave [type]
                    break;
                case "team":
                    //Syntax: /game team <color> [type]
                    break;
                case "spectate":
                    //Syntax: /game spectate [type]
                    break;
            }
        }catch(Exception e){
            ChatWriter.writeTo(sender, ChatWriterType.ERROR, "An error occured during command execution. Stack trace follows");
            sender.sendMessage(Utilities.getStackTrace(e));
        }
        return true;
    }
}
enum Games {
    tf,bl,dr,ctf,bb,run
}
