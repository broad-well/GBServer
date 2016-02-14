package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 10/17/15.
 */
public class Warp implements CommandExecutor {
    public static Path pathToConfig = Paths.get("/home/michael/SpigotCache/plugins/Broadwell_Server_Plugin/warps.txt");
    public static HelpTable ht = new HelpTable("/warp <[set/list/remove] [locationName]>", "Used to contain Location values", "", "warp");
    public static HashMap<String, Location> data = new HashMap<>();
    private static Yaml yaml = new Yaml();
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender)) {
            //Get data.
            if (args.length == 0) {
                ht.show(sender);
                return true;
            }
            try {
                Player p = (Player) sender;
                reloadData();
                if (args[0].equalsIgnoreCase("set")) {
                    //Put new values into the HashMap.
                    if (args.length < 2) {
                        ht.show(sender);
                        return true;
                    }
                    if (args[1].contains(",")) {
                        ChatWriter.writeTo(sender, ChatWriterType.WARP, "Illegal name: name cannot contain commas!");
                        return true;
                    }
                    data.put(args[1], p.getLocation());
                    saveData();
                    ChatWriter.writeTo(sender, ChatWriterType.WARP, "Successfully set location entry " + ChatColor.BLUE
                            + args[1] + ChatColor.AQUA + " to your current location.");
                } else if (args[0].equalsIgnoreCase("list")) {
                    ChatWriter.writeTo(sender, ChatWriterType.WARP, "A list of all warps:");
                    for (String s : data.keySet()) {
                        ChatWriter.writeTo(sender, ChatWriterType.WARP, s);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length < 2) {
                        ht.show(sender);
                        return true;
                    }
                    data.remove(args[1]);
                    saveData();
                    ChatWriter.writeTo(sender, ChatWriterType.WARP, "Successfully deleted that entry.");
                } else {
                    try {
                        p.teleport(data.get(args[0]));
                    } catch (NullPointerException npe) {
                        ChatWriter.writeTo(sender, ChatWriterType.ERROR, "That waypoint cannot be found");
                        return true;
                    }


                }

            } catch (Exception e) {
                ChatWriter.writeTo(sender, ChatWriterType.ERROR, "Error! Please try again.");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    static void reloadData() throws IOException {
        data.clear();
        for(Map.Entry<String, String> entry : ConfigManager.smartGet("Warps").entrySet())
            data.put(entry.getKey(), Utilities.deserializeLocation(entry.getValue()));
    }

    static void saveData() {
        ConfigManager.entries.put("Warps", toYamlDump());

    }

    static HashMap<String, String> toYamlDump() {
        /*
        Format:
        Name Of Warp: Utilities's serialization
         */
        HashMap<String, String> output = new HashMap<>();
        for(Map.Entry<String, Location> entry : data.entrySet())
            output.put(entry.getKey(), Utilities.serializeLocation(entry.getValue()));
        return output;
    }

    static void importYamlDump(HashMap<String, String> dump){
        data.clear();
        for(Map.Entry<String, String> entry : dump.entrySet())
            data.put(entry.getKey(), Utilities.deserializeLocation(entry.getValue()));
    }
}

