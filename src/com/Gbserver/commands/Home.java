package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class Home implements CommandExecutor {


    public static HelpTable ht = new HelpTable("/home (set)", "This is used to provide home location storage.", "", "home");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender)) {
            ConfigManager man;
            try {
                man = new ConfigManager(ConfigManager.getPathInsidePluginFolder("homes.cfg"), 6);
                Player pl = (Player) sender;
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("set")) {
                        // Set home.
                        // Determine if it is set.
                        if (man.data.keySet().contains(pl.getUniqueId().toString())) {
                            ChatWriter.writeTo(sender, ChatWriterType.HOME, "We detected your home. Removing it.");
                            man.data.remove(pl.getUniqueId().toString());
                        }
                        man.add(pl.getUniqueId().toString(), null,
                                pl.getWorld().getName(),
                                String.valueOf(pl.getLocation().getX()),
                                String.valueOf(pl.getLocation().getY()),
                                String.valueOf(pl.getLocation().getZ()),
                                String.valueOf(pl.getLocation().getPitch()),
                                String.valueOf(pl.getLocation().getYaw()));
                        ChatWriter.writeTo(pl, ChatWriterType.HOME, "Set your home location to your current location.");
                        //ChatWriter.writeTo(pl, ChatWriterType.HOME, "x: " + pl.getLocation().getBlockX() + ", y: "
                        //+ pl.getLocation().getBlockY() + ", z: " + pl.getLocation().getBlockZ());
                        return true;
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (pl.isOp()) {
                            sender.sendMessage(man.data.toString());
                        } else {
                            ChatWriter.writeTo(sender, ChatWriterType.CONDITION, "You are not allowed to list homes.");
                        }
                        return true;
                    } else {
                        ht.show(sender);
                        return true;
                    }
                }
                man.readData();

                if (!man.data.containsKey(pl.getUniqueId().toString())) {
                    ChatWriter.writeTo(pl, ChatWriterType.HOME, "You did not set your home. To set, use /home set.");
                    return true;
                }
                List<String> data = man.data.get(pl.getUniqueId().toString());
                pl.teleport(new Location(
                        Bukkit.getWorld(data.get(0)),
                        Double.valueOf(data.get(1)),
                        Double.valueOf(data.get(2)),
                        Double.valueOf(data.get(3)),
                        Float.valueOf(data.get(4)),
                        Float.valueOf(data.get(5))));
                ChatWriter.writeTo(pl, ChatWriterType.HOME, "Teleported you to your set home.");

                return true;
            } catch (IOException e) {
                sender.sendMessage("Error occured.");
                return true;
            }

        } else {
            return true;
        }

    }


}
