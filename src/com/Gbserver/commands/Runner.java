package com.Gbserver.commands;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.variables.CubicSelection;
import com.Gbserver.variables.Sandbox;
import com.Gbserver.variables.TaskStorage;
import com.Gbserver.variables.minigame.MGUtils;
import com.Gbserver.variables.minigame.StandaloneMG;
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

import java.util.*;

public class Runner implements CommandExecutor, StandaloneMG {
    public World world = Bukkit.getWorld("Runner1");
    public CubicSelection lobby = new CubicSelection(
            new Location(world, 1023, 103, -1031),
            new Location(world, 1003, 91, -999)
    );
    public String identifier = "Runner";
    private List<Player> spectators = new LinkedList<>();
    public int runlevel = 0;
    public List<Player> players = new LinkedList<>();
    public Runnable startProcedure = new Runnable() {
        @Override
        public void run() {

            for (Player p : players) {
                p.getInventory().clear();
                Utilities.giveLeap(p);
                p.setGameMode(GameMode.SURVIVAL);
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
            }
        }
    };
    public Runnable stopProcedure = new Runnable() {
        public void run() {

        }
    };
    public List<Runnable> threads = new ArrayList<Runnable>() {{
        add(new Runnable() {
            @Override
            public void run() {

                for (Player p : players) {
                    Block b = p.getLocation().subtract(0, 1, 0).getBlock();
                    if (b.getData() != (byte) 14 && b.getType() == Material.STAINED_CLAY) {

                        b.setData((byte) 14);
                        final TaskStorage ts = new TaskStorage(b);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
                            public void run() {
                                ((Block) ts.getStorage()).setType(Material.AIR);
                                ((Block) ts.getStorage()).getLocation().getWorld().
                                        spawnFallingBlock(((Block) ts.getStorage()).getLocation(),
                                                Material.STAINED_CLAY, (byte) 14);
                            }
                        }, 15L);
                    }
                    p.setFoodLevel(20);
                }

            }
        });
    }};
    public int portalId = 1;
    public HashMap<String, CubicSelection> maps = new HashMap<String, CubicSelection>() {{
        put("Hopper", new CubicSelection(new Location(world, -999, 99, 1022),
                new Location(world, -1053, 72, 981)));
    }};

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public CubicSelection getLobby() {
        return lobby;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Runnable getStartProcedure() {
        return startProcedure;
    }

    @Override
    public Runnable getStopProcedure() {
        return stopProcedure;
    }

    @Override
    public List<Runnable> getThreads() {
        return threads;
    }

    @Override
    public List<Player> getSpectators() {
        return spectators;
    }

    @Override
    public HashMap<String, CubicSelection> getMaps() {
        return maps;
    }

    @Override
    public int getMaxPlayers() {
        return 20;
    }

    @Override
    public int getPortalId() {
        return portalId;
    }


    @Override
    public int getRunlevel() {
        return runlevel;
    }

    @Override
    public MGUtils getUtils() {
        return utils;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void setRunlevel(int level) {
        runlevel = level;
    }

    @Override
    public void setPlayers(List<Player> s) {
        players = s;
    }

    @Override
    public List<Location> getSpawnpoints() {
        return Arrays.asList(
                new Location(world, 3, 99, 3),
                new Location(world, 7, 99, 3),
                new Location(world, 12, 99, 3),
                new Location(world, 17, 99, 3),
                new Location(world, 22, 99, 3),
                new Location(world, 27, 99, 3),
                new Location(world, 32, 99, 3),
                new Location(world, 37, 99, 3),
                new Location(world, 42, 99, 3),
                new Location(world, 47, 99, 3),
                new Location(world, 3, 99, 38),
                new Location(world, 7, 99, 38),
                new Location(world, 12, 99, 38),
                new Location(world, 17, 99, 38),
                new Location(world, 22, 99, 38),
                new Location(world, 27, 99, 38),
                new Location(world, 32, 99, 38),
                new Location(world, 37, 99, 38),
                new Location(world, 42, 99, 38),
                new Location(world, 47, 99, 38)
        );
    }

    //MINIGAME HEADER FINISHED


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

    /*Each minigames requires its own constructor in addition to the contents in the Abstract class.
     */
    public MGUtils utils;

    public Runner() {
        utils = new MGUtils(this);
    }

    public Location join = new Location(world, 1012.5, 102.5, -1023.5);
    public Sheep joinSheep;

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Sandbox.check(sender)) return true;
        if (args.length >= 1) {
            if (!utils.interpret(args, sender))
                sender.sendMessage("Interpretation failure.");
            else
                sender.sendMessage("Interpretation succeeded.");
        } else {
            sender.sendMessage("Missing required arguments...");
        }
        return true;
    }


    public void getSheep() {
        joinSheep = (Sheep) world.spawnEntity(join, EntityType.SHEEP);
        joinSheep.setColor(DyeColor.PURPLE);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
            public void run() {
                joinSheep.setVelocity(new Vector(0, 0, 0));
            }
        }, 0L, 1L);
    }

}