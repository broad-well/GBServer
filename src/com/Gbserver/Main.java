package com.Gbserver;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.Gbserver.commands.*;
import com.Gbserver.listener.*;
import com.Gbserver.variables.*;

public class Main extends JavaPlugin {
	Vector vector;
	Sheep sh = null;
	int runCount = 0;
	boolean isWait = false;
	public static Sheep[] snake = new Sheep[50];
	static int i;
	int TFCount = 0;
	public static byte paintColor = (byte) 15;
	
	@SuppressWarnings("deprecation")
	public void onEnable() {

		PluginDescriptionFile desc = getDescription();
		Logger lg = Logger.getLogger("Minecraft");
		//final FileConfiguration fc = getConfig();
		//fc.addDefault("announcements", "An announcement 1");
		//fc.addDefault("announcements", "An announcemento 2");
		//fc.options().copyDefaults(true);
		setupConfig();
		saveConfig();
		@SuppressWarnings("unused")
		Protection proc = new Protection(this);
		getCommand("spawn").setExecutor(new Spawn());
		getCommand("tell").setExecutor(new Tell());
		getCommand("t").setExecutor(new Tell());
		getCommand("msg").setExecutor(new Tell());
		getCommand("m").setExecutor(new Tell());
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
		getServer().getPluginManager().registerEvents(new Reaction(), this);
		getServer().getPluginManager().registerEvents(new CTFListener(), this);
		getServer().getPluginManager().registerEvents(new RunnerListener(), this);
		getServer().getPluginManager().registerEvents(new LobbyListener(), this);
		getServer().getPluginManager().registerEvents(new DrawColorListener(), this);
		getServer().getPluginManager().registerEvents(new InvinceListener(), this);
		getServer().getPluginManager().registerEvents(new MuteListener(), this);
		getServer().getPluginManager().registerEvents(new SitListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
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
		lg.info(desc.getName() + " has been enabled. DDDDDDDDDDDDDDDDDDD");
		new Announce(this);
		Reaction.getRepeatingEvent();
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
				for(Player pa : GameType.BL.red){
					pls.add(pa.getName());
					pa.teleport(BL.redTP);
					pa.setGameMode(GameMode.SURVIVAL);
				}
				for(Player pal : GameType.BL.blue){
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
				for(Player p : GameType.DR.allPlayers()){
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

			@Override
			public void run() {
				
				CTF.blue.addAll(GameType.CTF.blue);
				CTF.red.addAll(GameType.CTF.red);
				for(Player a : CTF.blue){
					a.teleport(CTF.getSpawn(Team.BLUE));
				}
				for(Player b : CTF.red){
					b.teleport(CTF.getSpawn(Team.RED));
				}
				
				CTF.startGame();
				CTF.getVariables();
			}
			
		}, LT.CTF);
		Runner.getSheep();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

			@Override
			public void run() {
				Vector nullv = new Vector(0,0,0);
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
		Announce.registerEvents();
		// getServer().getPluginManager().registerEvents(new
		// runnerListenerDepricated(), this);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				if(CTF.isRunning){
					for(Player a : CTF.allPlayers()){
						a.setFoodLevel(20);
					}
				}
			}
			
		
		}, 0L, 1L);
		
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
							if (TF.redPlayers.contains(p)) {
								isRed = true;
							} else {
								isRed = false;
							}
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
					if (Runner.isRunning) {
						// RED = 14
						for (Player p : Runner.players){
							Block b = p.getLocation().subtract(0,1,0).getBlock();
							if(b.getData() != (byte) 14 && b.getType() == Material.STAINED_CLAY){
								
								b.setData((byte) 14);
								final TaskStorage ts = new TaskStorage(b);
								Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
									public void run() {
										((Block) ts.getStorage()).setType(Material.AIR);
										((Block) ts.getStorage()).getLocation().getWorld().spawnFallingBlock(((Block) ts.getStorage()).getLocation(), Material.STAINED_CLAY, (byte) 14); 
									}
								}, 20L);
							}
							p.setFoodLevel(20);
						}
					}
				}
			}, 0L, 1L);

		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

			public void run() {
				for(Chair c : Chairs.chairs){
					c.getBat().teleport(c.getLocation());
				}
				
			}
			
		}, 0L, 5L);
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()){
					Block b;
					if (p.getItemInHand().getType().equals(Material.WOOD_SWORD)
							&& p.getItemInHand().getItemMeta().getDisplayName().equals("Thin Brush")
							&& p.getWorld().equals(Bukkit.getWorld("Drawing"))
							&& p.isBlocking()) {
						//Do draw.
						if((b = p.getTargetBlock((Set<Material>) null, 100)).getType().equals(Material.WOOL)){
							b.setData(paintColor);
						}
						
						
					}
				}
			}
		}, 0L, 1L);
		/*pm.addPacketListener(
				  new PacketAdapter(this, ListenerPriority.NORMAL, 
				          PacketType.Play.Server.ENTITY_TELEPORT) {
				    @Override
				    public void onPacketSending(PacketEvent event) {
				        // Item packets (id: 0x29)
				    	// bat's packet id is 42.
				        if (event.getPacketType() == 
				                PacketType.Play.Server.ENTITY_TELEPORT) {
				            if(event.getPacket().getIntegers().read(0) == 42){
				            this.getPlugin().getLogger().info("Just found a minecart packet");
				            event.setCancelled(true);
				            }
				        }
				    }
				});
	    /*
		 * scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
		 * 
		 * public void run() { if(TF.isRunning){ try{ TF.isBuildtime = true;
		 * Thread.sleep(30000); TF.isBuildtime = false; Thread.sleep(160000);
		 * }catch(Exception e){
		 * 
		 * } } }
		 * 
		 * }, 0L, 20L); /*
		 * getServer().getScheduler().scheduleSyncDelayedTask(this, new
		 * Runnable() {
		 * 
		 * @SuppressWarnings("deprecation") public void run() { for (int a = 0;
		 * a < Runner.players.length; a++) { Player player = Runner.players[a];
		 * player.sendMessage("hello"); World world = player.getWorld(); if
		 * (Runner.isRunning) { Location loc = player.getLocation();
		 * player.sendMessage("You are at " + loc); int x = loc.getBlockX(); int
		 * y = loc.getBlockY(); int z = loc.getBlockZ(); y--; Location newloc =
		 * new Location(world, x, y, z); Material block; byte data; if
		 * (newloc.getBlock().getType() != Material.AIR) { try {
		 * Thread.sleep(130); block = newloc.getBlock().getType(); data =
		 * newloc.getBlock().getData(); newloc.getBlock().setType(Material.AIR);
		 * world.spawnFallingBlock(newloc, block, data); } catch (Exception e) {
		 * e.printStackTrace(); } }
		 * 
		 * } } } }, 1L); // This is the delay, in ticks, until the thread is
		 * executed, // since the main threads ticks 20 times per second, 60
		 * ticks is // 3 seconds.
		 */
	}
	
	public void onDisable() {
		try {
			GameType.TF.close();
			GameType.BL.close();
			GameType.DR.close();
			Runner.joinSheep.remove();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveConfig();
	
	}
	
	public void setupConfig() {
		if(!getConfig().contains("announcements")){
			List<String> value = Arrays.asList("Hello defaults", "Hello HELL");
			getConfig().addDefault("announcements", value);
			saveConfig();
		}
	}
}
	