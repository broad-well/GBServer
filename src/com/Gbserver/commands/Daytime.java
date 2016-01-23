package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by michael on 1/22/16.
 */
public class Daytime implements CommandExecutor {
    //Declare FINAL constants
    public static final long DAY = 1000;
    public static final long NIGHT = 18000;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("night")){
            //NightWorld selection =
            ((sender instanceof Player) ?
                    ((Player) sender).getWorld() :
                    Bukkit.getWorld("world")).setTime(NIGHT);
            sender.sendMessage("Time set");

        }else{
            ((sender instanceof Player) ?
                    ((Player) sender).getWorld() :
                    Bukkit.getWorld("world")).setTime(DAY);
            sender.sendMessage("Time set");
        }
        return true;
    }

}
