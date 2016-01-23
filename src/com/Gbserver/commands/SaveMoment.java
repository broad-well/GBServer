package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

// BUGGED!
public class SaveMoment implements CommandExecutor {

    public static File file = ConfigManager.getPathInsidePluginFolder("playerdata.yml").toFile();
    //TYPE
    public static HashMap<String, HashMap<String, Object>> saved = new HashMap<>();


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (label.equalsIgnoreCase("save")) {
            if (args.length < 1) {
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
                return false;
            }
            Player p = Bukkit.getServer().getPlayer(args[0]);
            saved.put(p.getName(), newHashMap(p));
            sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Successfully saved " + p.getName() + "'s Inventory, Location, Health, Armor, and Gamemode."));
            return true;
        }
        if (label.equalsIgnoreCase("rest")) {
            if (args.length < 1) {
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
                return false;
            }
            Player p = Bukkit.getServer().getPlayer(args[0]);
            if(saved.get(p.getName()) == null) {
                p.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You never saved your conditions."));
            }else{
                HashMap<String, Object> cr = saved.get(p.getName());
                Location loc = Utilities.deserializeLocation((String) cr.get("location"));
                ItemStack[] contents = new ItemStack[((HashMap[]) cr.get("inventory")).length];
                for(int i = 0; i < contents.length; i++){
                    contents[i] = Utilities.deserialize(((HashMap[]) cr.get("inventory"))[i]);
                }
                double health = Double.parseDouble((String) cr.get("health"));
                ItemStack[] armorContents = new ItemStack[((HashMap[]) cr.get("armor")).length];
                for(int i = 0; i < contents.length; i++){
                    armorContents[i] = Utilities.deserialize(((HashMap[]) cr.get("armor"))[i]);
                }
                GameMode gm = GameMode.valueOf((String) cr.get("gamemode"));
                p.teleport(loc);
                p.getInventory().setContents(contents);
                p.setHealth(health);
                p.getInventory().setArmorContents(armorContents);
                p.setGameMode(gm);
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Successfully restored" + p.getName() + "'s Inventory, Location, Health, Armor, and Gamemode."));
            }


        }
        return true;
    }

    private static HashMap<String, Object> newHashMap(Player p){
        HashMap<String, Object> build = new HashMap<>();
        build.put("location", Utilities.serializeLocation(p.getLocation()));
        HashMap[] items = new HashMap[p.getInventory().getContents().length];
        for(int i = 0; i < items.length; i++){
            items[i] = Utilities.serialize(p.getInventory().getContents()[i]);
        }
        build.put("inventory", items);
        build.put("health", String.valueOf(p.getHealth()));
        HashMap[] armorItems = new HashMap[p.getInventory().getArmorContents().length];
        for(int i = 0; i < armorItems.length; i++){
            armorItems[i] = Utilities.serialize(p.getInventory().getArmorContents()[i]);
        }
        build.put("armor", armorItems);
        build.put("gamemode", p.getGameMode().toString());
        return build;
    }

    public static void output() throws IOException {
        FileWriter fw = new FileWriter(file);
        SwiftDumpOptions.BLOCK_STYLE().dump(saved, fw);
        fw.flush();
        fw.close();
    }

    public static void input() throws IOException {
        FileReader fr = new FileReader(file);
        Object obj = SwiftDumpOptions.BLOCK_STYLE().load(fr);
        fr.close();
        if(obj instanceof HashMap){
            saved = (HashMap<String, HashMap<String, Object>>) obj;
        }
    }


}
