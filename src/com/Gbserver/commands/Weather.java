package com.Gbserver.commands;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.listener.ChatFormatter;
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
    public static HashMap<String, boolean[]> interpreter = new HashMap<String, boolean[]>(){{
        //hasStorm and isThundering
        put("c", new boolean[]{false, false});
        put("clear", new boolean[]{false, false});
        put("r", new boolean[]{true, false});
        put("rain", new boolean[]{true, false});
        put("t", new boolean[]{true, true});
        put("thunder", new boolean[]{true, true});
        put("to", new boolean[]{false, true});
        put("thunderonly", new boolean[]{false, true});
    }};
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if(Utilities.validateGamePlay(sender) && (!Main.isOnEvent() || ChatFormatter.staff.contains(sender.getName()))){
            if(args.length == 0){
                sender.sendMessage(ChatColor.GOLD + "Available options:");
                sender.sendMessage(ChatColor.YELLOW + "c" + ChatColor.RESET + "   :   " + ChatColor.GREEN + "clear");
                sender.sendMessage(ChatColor.YELLOW + "r" + ChatColor.RESET + "   :   " + ChatColor.GREEN + "rain");
                sender.sendMessage(ChatColor.YELLOW + "t" + ChatColor.RESET + "   :   " + ChatColor.GREEN + "thunder");
                sender.sendMessage(ChatColor.YELLOW + "to" + ChatColor.RESET + "  :   " + ChatColor.GREEN + "thunder only");
                return true;
            }
            World selection = sender instanceof Player ?
                    ((Player) sender).getWorld() :
                    Bukkit.getWorld("world");
            selection.      setStorm(interpreter.get(args[0])[0]);
            selection. setThundering(interpreter.get(args[0])[1]);
            sender.sendMessage("Weather updated");
        }
        return true;
    }
}
