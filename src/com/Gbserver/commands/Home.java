package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Home implements CommandExecutor {

    public static HashMap<UUID, Location> data = new HashMap<>();
    public static HelpTable ht = new HelpTable("/home (set)", "This is used to provide home location storage.", "", "home");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender)) {

            inport();
            Player pl = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("set")) {
                    // Set home.
                    // Determine if it is set.
                    if (data.keySet().contains(pl.getUniqueId())) {
                        ChatWriter.writeTo(sender, ChatWriterType.HOME, "We detected your home. Removing it.");
                        data.remove(pl.getUniqueId());
                    }
                    data.put(pl.getUniqueId(), pl.getLocation());
                    ChatWriter.writeTo(pl, ChatWriterType.HOME, "Set your home location to your current location.");
                    //ChatWriter.writeTo(pl, ChatWriterType.HOME, "x: " + pl.getLocation().getBlockX() + ", y: "
                    //+ pl.getLocation().getBlockY() + ", z: " + pl.getLocation().getBlockZ());
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (EnhancedPlayer.getEnhanced(pl).getPermission().isAbove(PermissionManager.Permissions.PRIVILEGED)) {
                        for (Map.Entry<UUID, Location> entry : data.entrySet()) {
                            sender.sendMessage(Bukkit.getOfflinePlayer(entry.getKey()).getName() + " -> " + entry.getValue());
                        }
                    } else {
                        ChatWriter.writeTo(sender, ChatWriterType.CONDITION, "You are not allowed to list homes.");
                    }
                } else {
                    ht.show(sender);
                }
            } else {

                if (!data.containsKey(pl.getUniqueId())) {
                    ChatWriter.writeTo(pl, ChatWriterType.HOME, "You did not set your home. To set, use /home set.");

                } else {
                    pl.teleport(data.get(pl.getUniqueId()));
                    ChatWriter.writeTo(pl, ChatWriterType.HOME, "Teleported you to your set home.");
                }
            }
            export();
            return true;


        }
        return true;
    }

    public static void export() {
        //Convert to savable format first.
        HashMap<String, String> savable = new HashMap<>();
        for (Map.Entry<UUID, Location> entry : data.entrySet())
            savable.put(Identity.serializeIdentity(Bukkit.getOfflinePlayer(entry.getKey())), Utilities.serializeLocation(entry.getValue()));
        ConfigManager.entries.put("Home", savable);
    }

    //1st element: UUID to String; 2nd element: Utilities serialized location
    //Main volatile storage subsystem ID "Home"
    public static void inport() {
        data.clear();
        for (Map.Entry<String, String> entry : ConfigManager.smartGet("Home").entrySet())
            data.put(Identity.deserializeIdentity(entry.getKey()).getUniqueId(), Utilities.deserializeLocation(entry.getValue()));

    }
}
