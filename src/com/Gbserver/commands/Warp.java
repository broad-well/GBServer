package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michael on 10/17/15.
 */
public class Warp implements CommandExecutor {
    public static Path pathToConfig = Paths.get("/home/michael/SpigotCache/plugins/Broadwell_Server_Plugin/warps.txt");
    public static HelpTable ht = new HelpTable("/warp <[set/list/remove] [locationName]>", "Used to contain Location values", "", "warp");
    public static HashMap<String, Location> data = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
                    ChatWriter.writeTo(sender, ChatWriterType.WARP, "Successfully set location entry " + ChatColor.BLUE
                            + args[1] + ChatColor.AQUA + " to your current location.");
                    saveData();
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
        List<String> lines = Files.readAllLines(pathToConfig, Charset.defaultCharset());
        for (String s : lines) {
            //Format: ThisLocation,world,141241.14,1451.1,1736.4
            //a.k.a name,worldname,x,y,z; 1:name, 2:world, 3:x, 4:y, 5:z.
            String[] array = s.split(",");
            data.put(array[0], new Location(
                    Bukkit.getWorld(array[1]),
                    Double.valueOf(array[2]),
                    Double.valueOf(array[3]),
                    Double.valueOf(array[4])));
        }
    }

    static void saveData() throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(pathToConfig.toFile()));
        for (Map.Entry<String, Location> e : data.entrySet()) {
            Location l = e.getValue();
            pw.println(e.getKey() + "," + l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ());
        }
        pw.flush();
        pw.close();
    }
}

