package com.Gbserver;

import com.Gbserver.commands.*;
import com.Gbserver.listener.*;
import com.Gbserver.mail.MailMan;
import com.Gbserver.variables.*;
import com.Gbserver.variables.minigame.Games;
import com.Gbserver.variables.minigame.MGUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static byte paintColor = (byte) 15;
    //For Halloween!
    public static boolean onEvent = isOnEvent();
    public static boolean debug = true;
    public static String eventName = "Christmas Event";


    @SuppressWarnings("deprecation")
    public void onEnable() {
        ConfigLoader.load();
        final List<Exception> errorStack = new LinkedList<>();
        PluginDescriptionFile desc = getDescription();
        Logger lg = Logger.getLogger("Minecraft");
        setupConfig();
        Utilities.instance = this;
        try {
            ConfigLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Register commands.
        @SuppressWarnings("unused")
        Protection proc = new Protection(this);
        getCommand("spawn").setExecutor(new Spawn());
        getCommand("tell").setExecutor(new Tell());
        getCommand("reply").setExecutor(new Reply());
        getCommand("runner").setExecutor(new Runner());
        getCommand("protection").setExecutor(new Protection(this));
        getCommand("afk").setExecutor(new Afk());
        getCommand("isafk").setExecutor(new Afk());
        getCommand("broadcast").setExecutor(new Attentions());
        getCommand("back").setExecutor(new Back());
        getCommand("nick").setExecutor(new Nick());
        getCommand("ride").setExecutor(new Ride());
        getCommand("save").setExecutor(new SaveMoment());
        getCommand("rest").setExecutor(new SaveMoment());
        getCommand("rideme").setExecutor(new Ride());
        getCommand("gm").setExecutor(new Gamemode());
        getCommand("bl").setExecutor(new BL());
        getCommand("heal").setExecutor(new Heal());
        getCommand("tf").setExecutor(new TF());
        getCommand("menu").setExecutor(new Menu());
        getCommand("sit").setExecutor(new Sit());
        getCommand("announce").setExecutor(new Attentions());
        getCommand("f").setExecutor(new F());
        getCommand("friend").setExecutor(new F());
        getCommand("vote").setExecutor(new Vote());
        getCommand("mute").setExecutor(new Mute());
        getCommand("protect").setExecutor(new Invince());
        getCommand("quit").setExecutor(new Quit());
        getCommand("nofall").setExecutor(new NoFall());
        getCommand("tpa").setExecutor(new Tpa());
        getCommand("tphere").setExecutor(new Tpa());
        getCommand("tpaccept").setExecutor(new Tpa());
        getCommand("tpdeny").setExecutor(new Tpa());
        getCommand("lobby").setExecutor(new Lobby());
        getCommand("ctf").setExecutor(new CTF());
        getCommand("home").setExecutor(new Home());
        getCommand("say").setExecutor(new Say());
        getCommand("ignore").setExecutor(new Ignore());
        getCommand("bacon").setExecutor(new Bacon());
        getCommand("hat").setExecutor(new Hat());
        getCommand("warp").setExecutor(new Warp());
        getCommand("admin").setExecutor(new Admin());
        getCommand("devops").setExecutor(new DevOperation());
        getCommand("group").setExecutor(new Group());
        getCommand("mail").setExecutor(new Mail());
        getCommand("snake").setExecutor(new Snake());
        getCommand("ping").setExecutor(new F());
        getCommand("twitch").setExecutor(new Twitch());
        getCommand("selscript").setExecutor(new SelScript());
        getCommand("skull").setExecutor(new Skull());
        getCommand("weather").setExecutor(new Weather());
        getCommand("marry").setExecutor(new Marriage());
        getCommand("jail").setExecutor(new Jail());
        getCommand("night").setExecutor(new Daytime());
        getCommand("day").setExecutor(new Daytime());
        getCommand("spec").setExecutor(new Spectate());
        getCommand("survival").setExecutor(new Survival());
        getCommand("contractverify").setExecutor(new ContractVerify());
        getCommand("uptime").setExecutor(new Uptime());
        getCommand("answer").setExecutor(new Survey());

        //Register events.
        if (onEvent)
            getServer().getPluginManager().registerEvents(new EventSpecials(), this);

        getServer().getPluginManager().registerEvents(new JailListener(), this);
        getServer().getPluginManager().registerEvents(new Survey(), this);
        getServer().getPluginManager().registerEvents(new BanDB(), this);
        getServer().getPluginManager().registerEvents(new StatOnlineTime(), this);
        getServer().getPluginManager().registerEvents(new IPCollector(), this);
        getServer().getPluginManager().registerEvents(new CreativeGameMode(), this);
        getServer().getPluginManager().registerEvents(new BaconListener(), this);
        getServer().getPluginManager().registerEvents(new SandboxListener(), this);
        getServer().getPluginManager().registerEvents(new CTFListener(), this);
        getServer().getPluginManager().registerEvents(new LobbyListener(), this);
        getServer().getPluginManager().registerEvents(new DrawColorListener(), this);
        getServer().getPluginManager().registerEvents(new InvinceListener(), this);
        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        getServer().getPluginManager().registerEvents(new SitListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new Birthday(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new TFListeners(), this);
        getServer().getPluginManager().registerEvents(new HealListener(), this);
        getServer().getPluginManager().registerEvents(new BLListener(), this);
        getServer().getPluginManager().registerEvents(new ExplosionListener(), this);
        getServer().getPluginManager().registerEvents(new RideListener(), this);
        getServer().getPluginManager().registerEvents(new LoginTagListener(), this);
        getServer().getPluginManager().registerEvents(new NickListener(), this);
        getServer().getPluginManager().registerEvents(new BackListeners(), this);
        getServer().getPluginManager().registerEvents(new AfkListener(), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatter(), this);
        getServer().getPluginManager().registerEvents(new BlockData(), this);
        getServer().getPluginManager().registerEvents(new MGListener(), this);
        lg.info(desc.getName() + " has been enabled.");
        MailMan.setupMailChecker();
        //Setup all minigames using MGUtils.
        for (Games g : Games.values()) {
            MGUtils.utilAccess.get(g).initialize();
        }
        new Announce(this);
        try {
            Reaction.getRepeatingEvent();

        } catch (Exception e) {
            errorStack.add(e);
        }
        try {
            Bacon.getLobby();
        } catch (Exception e) {
            errorStack.add(e);
        }
        GameType.TF = new GameType(new Runnable() {
            public void run() {
                TF.bluePlayers.addAll(GameType.TF.blue);
                TF.redPlayers.addAll(GameType.TF.red);
                TF.startGame();
            }
        }, LT.TF);
        GameType.BL = new GameType(new Runnable() {
            public void run() {
                List<String> pls = new LinkedList<>();
                for (Player pa : GameType.BL.red) {
                    pls.add(pa.getName());
                    pa.teleport(BL.redTP);
                    pa.setGameMode(GameMode.SURVIVAL);
                }
                for (Player pal : GameType.BL.blue) {
                    pls.add(pal.getName());
                    pal.teleport(BL.blueTP);
                    pal.setGameMode(GameMode.SURVIVAL);
                }
                BL.players.addAll(pls);
                BL.isRunning = true;
            }
        }, LT.BL);
        GameType.DR = new GameType(new Runnable() {
            public void run() {
                //Give everyone armor
                //Also the leap axe, bows/arrows, and food.
                for (Player p : GameType.DR.allPlayers()) {
                    ItemStack[] armor = {
                            new ItemStack(Material.GOLD_BOOTS),
                            new ItemStack(Material.GOLD_CHESTPLATE),
                            new ItemStack(Material.GOLD_LEGGINGS),
                            new ItemStack(Material.GOLD_HELMET)
                    };
                    p.getInventory().setArmorContents(armor);
                    ItemStack axe = new ItemStack(Material.IRON_AXE);
                    ItemMeta im = axe.getItemMeta();
                    im.setDisplayName("Leap Axe");
                    axe.setItemMeta(im);
                    p.getInventory().addItem(axe);
                    p.getInventory().addItem(new ItemStack(Material.BOW));
                    p.getInventory().addItem(new ItemStack(Material.ARROW, 64));
                    p.getInventory().addItem(new ItemStack(Material.APPLE, 30));
                    p.setGameMode(GameMode.SURVIVAL);
                    p.teleport(new Location(Bukkit.getWorld("Dragons"), -0.5, 158, 20.5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
                }
                Bukkit.getWorld("Dragons").spawnEntity(new Location(Bukkit.getWorld("Dragons"), 8.5, 166, -23), EntityType.ENDER_DRAGON);
                //Bukkit.getWorld("Dragons").spawnEntity(new Location(Bukkit.getWorld("Dragons"), 8.5, 166, -23), EntityType.ENDER_DRAGON);
                //Bukkit.getWorld("Dragons").spawnEntity(new Location(Bukkit.getWorld("Dragons"), 8.5, 166, -23), EntityType.ENDER_DRAGON);

            }
        }, LT.DR);
        GameType.CTF = new GameType(new Runnable() {

            public void run() {

                CTF.blue.addAll(GameType.CTF.blue);
                CTF.red.addAll(GameType.CTF.red);
                for (Player a : CTF.blue) {
                    a.teleport(CTF.getSpawn(Team.BLUE));
                }
                for (Player b : CTF.red) {
                    b.teleport(CTF.getSpawn(Team.RED));
                }

                CTF.startGame();
                CTF.getVariables();
            }

        }, LT.CTF);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

            public void run() {
                Vector nullv = new Vector(0, 0, 0);
                GameType.TF.getBlue().setVelocity(nullv);
                GameType.TF.getRed().setVelocity(nullv);
                GameType.BL.getBlue().setVelocity(nullv);
                GameType.BL.getRed().setVelocity(nullv);
                GameType.DR.getBlue().setVelocity(nullv);
                GameType.DR.getRed().setVelocity(nullv);
                GameType.CTF.getBlue().setVelocity(nullv);
                GameType.CTF.getRed().setVelocity(nullv);
            }

        }, 0L, 1L);
        // getServer().getPluginManager().registerEvents(new
        // runnerListenerDepricated(), this);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (onEvent)
            scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.setFoodLevel(20);
                    }
                }
            }, 0L, 1L);
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

            public void run() {
                if (CTF.isRunning) {
                    for (Player a : CTF.allPlayers()) {
                        a.setFoodLevel(20);
                    }
                }
                if (Bacon.isRunning) {
                    for (BaconPlayer bp : Bacon.players) {
                        bp.getHandle().setFoodLevel(20);
                    }
                }
            }


        }, 0L, 1L);
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {

                CreativeGameMode.setAll(onEvent ? GameMode.SURVIVAL : GameMode.CREATIVE);
            }
        }, 10L);
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

            @SuppressWarnings("deprecation")
            public void run() {
                if (BL.isRunning) {
                    for (Object p : BL.players.toArray()) {
                        String str = (String) p;
                        Player pl = getServer().getPlayer(str);
                        pl.setFoodLevel(15);
                        if (!(pl.getInventory().contains(Material.TNT))) {
                            ItemStack is = new ItemStack(Material.TNT, 64);
                            pl.getInventory().addItem(is);
                            pl.sendMessage("Refilled your inventory.");
                        }
                    }
                }
                if (TF.isRunning) {

                    for (Player p : TF.getAllPlayers()) {
                        p.setFoodLevel(20);
                        if (TF.isRunning && TF.getAllPlayers().contains(p)) {
                            boolean isRed;
                            isRed = TF.redPlayers.contains(p);
                            if (!(TFListeners.isValid(p.getLocation(), isRed))) {
                                Vector v = new Vector(-10, 5, 0);
                                if (isRed) {
                                    p.setVelocity(v.multiply(0.2));
                                    p.sendMessage("You are only allowed on red clay.");
                                } else {
                                    Vector blue = new Vector(10, 5, 0);
                                    p.setVelocity(blue.multiply(0.2));
                                    p.sendMessage("You are only allowed on blue clay.");
                                }

                            }
                        }
                    }
                    if (TF.isBuildtime) {
                        for (Object o : TF.getAllPlayers().toArray()) {
                            Player p = (Player) o;
                            if (!(p.getInventory().contains(Material.STAINED_CLAY))) {
                                TF.fillInventory(true);
                            }
                        }
                    } else {
                        for (Object o : TF.getAllPlayers().toArray()) {
                            Player p = (Player) o;
                            if (!(p.getInventory().contains(Material.BOW))
                                    || !(p.getInventory().contains(Material.ARROW))) {
                                TF.fillInventory(false);
                            }
                        }
                    }
                }
            }

        }, 0L, 10L);

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

            public void run() {
                for (Chair c : Chairs.chairs) {
                    c.getBat().teleport(c.getLocation());
                }

            }

        }, 0L, 5L);
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                ConfigLoader.unload();
                ConfigLoader.load();
            }
        }, 0, 20 * 60 * 25);
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

            @SuppressWarnings("deprecation")
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Block b;
                    try {
                        if (p.getItemInHand().getType().equals(Material.WOOD_SWORD)
                                && p.getItemInHand().getItemMeta().getDisplayName().equals("Thin Brush")
                                && p.getWorld().equals(Bukkit.getWorld("Drawing"))
                                && p.isBlocking()) {
                            //Do draw.
                            if ((b = p.getTargetBlock((Set<Material>) null, 100)).getType().equals(Material.WOOL)) {
                                b.setData(paintColor);
                            }


                        }
                    } catch (Exception e) {
                        errorStack.add(e);
                    }
                }
            }
        }, 0L, 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                if (errorStack.isEmpty()) {
                    System.out.println("Error stack empty");
                } else {
                    System.out.println("---ERRORSTACK DUMP---");
                    for (Exception e : errorStack) {
                        e.printStackTrace();
                    }
                    errorStack.clear();
                }
            }
        }, 5L);
    }

    public void onDisable() {
        List<Exception> errorStack = new LinkedList<>();
        System.out.println("New error stack");
        try {
            GameType.TF.close();
            GameType.BL.close();
            GameType.DR.close();
            System.out.println("Took care of some minigames");
        } catch (Exception e) {
            errorStack.add(e);
        }
        saveConfig();
        System.out.println("Saved config");
        try {
            Bacon.unload();

            //Territory is here!

            System.out.println("Took care of Bacon and protection");
        } catch (Exception e) {
            errorStack.add(e);
        }

        //Export all types tied with ConfigManager.
        ConfigLoader.unload();
        System.out.println("Dumping error stack now.");
        if (errorStack.isEmpty()) {
            System.out.println("Error stack empty");
        } else {
            System.out.println("---ERRORSTACK DUMP---");
            for (Exception e : errorStack) {
                e.printStackTrace();
            }
            errorStack.clear();
        }

    }

    public void setupConfig() {
        saveDefaultConfig();
        saveConfig();
    }

    public static boolean isOnEvent() {
        try {
            Scanner s = new Scanner(new FileInputStream(ConfigManager.getPathInsidePluginFolder("eventFlipFlop.txt").toFile()));
            return Boolean.valueOf(s.nextLine());
        } catch (Exception e) {
            return false;
        }
    }

}
