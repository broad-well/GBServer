package com.Gbserver.commands;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;

public class Runner implements CommandExecutor {

    public static World world = Bukkit.getWorld("Runner1");

    public class TaskBlock {
        public Block storage;
        private int taskid;

        public TaskBlock(Runnable r, Block s, long wait) {
            taskid = Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), r, wait);
        }

        public void close() {
            Bukkit.getScheduler().cancelTask(taskid);
            storage = null;

        }
    }

	/*
	 * I haven't been getting multi-threaded Runner to work. Here is a logical
	 * simulation:
	 * 
	 * Runner begins. A For loop creates dedicated threads for each player.
	 * (MUST BE SYNCHRONOUS) All threads run. Runner stops. All threads stop.
	 */

    public static Location join = new Location(world, 1012.5, 102.5, -1023.5);
    public static Sheep joinSheep;
    public static boolean isRunning = false;
    public static List<Player> players = new LinkedList<Player>();

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (label.equalsIgnoreCase("runner")) {
            switch (args[0]) {
                case "leave":
                    if (!players.contains(sender)) {
                        ChatWriter.writeTo(sender, ChatWriterType.GAME, "You are not in this game yet.");
                        return true;
                    }
                    players.remove(sender);
                    ChatWriter.writeTo(sender, ChatWriterType.GAME, "Removed you from the game.");
                case "start":
                    com.Gbserver.variables.Countdown a = new com.Gbserver.variables.Countdown(20, new Runnable() {
                        public void run() {
                            isRunning = true;
                            for (Player p : players) {
                                Utilities.giveLeap(p);
                                p.setGameMode(GameMode.SURVIVAL);
                                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2));
                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
                            }
                        }
                    });
                    for (Player p : players) {
                        p.teleport(new Location(world, 0, 202, 0));
                    }
                    ChatWriter.writeTo(sender, ChatWriterType.GAME, "Countdown executed.");
                    return true;
                case "reset":
                    isRunning = false;
                    players.clear();
                    getMap();
                    sender.sendMessage("Runner reset done!");
                    return true;
                default:
                    sender.sendMessage("Invalid Syntax.");
                    return false;
            }
        }
        return false;
    }

    public static void getMap() {
        //1st level
        for (int x = -30; x <= 21; x++) {
            for (int z = 18; z >= -20; z--) {
                Block b = world.getBlockAt(x, 200, z);
                b.setType(Material.STAINED_CLAY);
                b.setData(DyeColor.LIGHT_BLUE.getWoolData());
            }
        }

        //2nd level
        for (int x = -26; x <= 17; x++) {
            for (int z = 14; z >= -16; z--) {
                Block b = world.getBlockAt(x, 193, z);
                b.setType(Material.STAINED_CLAY);
                b.setData(DyeColor.BLUE.getWoolData());
            }
        }

        //3rd level
        for (int x = -22; x <= 13; x++) {
            for (int z = 10; z >= -12; z--) {
                Block b = world.getBlockAt(x, 186, z);
                b.setType(Material.STAINED_CLAY);
                b.setData(DyeColor.YELLOW.getWoolData());
            }
        }

        //4th level
        for (int x = -18; x <= 9; x++) {
            for (int z = 6; z >= -8; z--) {
                Block b = world.getBlockAt(x, 179, z);
                b.setType(Material.STAINED_CLAY);
                b.setData(DyeColor.ORANGE.getWoolData());
            }
        }
    }

    public static void getSheep() {
        joinSheep = (Sheep) world.spawnEntity(join, EntityType.SHEEP);
        joinSheep.setColor(DyeColor.PURPLE);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
            public void run() {
                joinSheep.setVelocity(new Vector(0, 0, 0));
            }
        }, 0L, 1L);
    }

}