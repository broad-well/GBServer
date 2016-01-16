package com.Gbserver.commands;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by michael on 1/14/16.
 */
public class Weather implements CommandExecutor {
    public static HashMap<String, Boolean> interpreter = new HashMap<String, Boolean>(){{
        //hasStorm and isThundering
        put("c", false);
        put("clear", false);
        put("r", true);
        put("rain", true);
    }};
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if(Utilities.validateGamePlay(sender) && (!Main.isOnEvent() || ChatFormatter.staff.contains(sender.getName()))){
            if(args.length == 0){
                sender.sendMessage(ChatColor.GOLD + "Available options:");
                sender.sendMessage(ChatColor.YELLOW + "c" + ChatColor.RESET + "   :   " + ChatColor.GREEN + "clear");
                sender.sendMessage(ChatColor.YELLOW + "r" + ChatColor.RESET + "   :   " + ChatColor.GREEN + "rain");
                return true;
            }
            World selection = sender instanceof Player ?
                    ((Player) sender).getWorld() :
                    Bukkit.getWorld("world");
            Boolean flip = interpreter.get(args[0]);
            if(flip == null){
                ChatWriter.writeTo(sender, ChatWriterType.ERROR, "I don't understand " + ChatColor.YELLOW + args[0]);
            }else{
                selection.setStorm(flip);
                sender.sendMessage("Weather updated");
            }
        }else{
            sender.sendMessage(ChatColor.RED + "You are not eligible to change weather because either you are in a game or you are not a staff of an ongoing event.");
        }
        return true;
    }
}
