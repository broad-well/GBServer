package com.Gbserver;

import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
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
	Player p = Runner.pl;
	public static Sheep[] snake = new Sheep[50];
	static int i;
	int TFCount = 0;
	public static byte paintColor = (byte) 15;
	
	public void onEnable() {

		PluginDescriptionFile desc = getDescription();
		Logger lg = Logger.getLogger("Minecraft");
		this.saveDefaultConfig();
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
		getServer().getPluginManager().registerEvents(new DrawColorListener(), this);
		getServer().getPluginManager().registerEvents(new InvinceListener(), this);
		getServer().getPluginManager().registerEvents(new LobbyListener(), this);
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

		Announce.registerEvents();
		try {
			Lobby l = new Lobby(LT.TF);
			Lobby l2 = new Lobby(LT.BL);
			Lobby.setSheeps(LT.TF);
			Lobby.setSheeps(LT.BL);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// getServer().getPluginManager().registerEvents(new
		// runnerListenerDepricated(), this);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				if (Runner.isSnakeRunning) {

					double pitch = Math.PI / 1.5;
					double yaw = ((getServer().getPlayer(Runner.playerName).getLocation().getYaw() + 90) * Math.PI)
							/ 180;
					double x = Math.sin(pitch) * Math.cos(yaw);
					double y = Math.sin(pitch) * Math.sin(yaw);
					double z = Math.cos(pitch);
					vector = new Vector(x, z, y);
					Runner.snake.getLocation().setYaw(getServer().getPlayer(Runner.playerName).getLocation().getYaw());
					Runner.snake.setVelocity(vector);

				}
			}

		}, 0L, 1L);
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {

				if (Runner.isSnakeRunning) {

					if (runCount < 50) {
						snake[runCount] = (Sheep) getServer().getPlayer("GoBroadwell").getWorld()
								.spawnEntity(getServer().getPlayer("GoBroadwell").getLocation(), EntityType.SHEEP);
						runCount++;
					}

				}
			}
		}, 10L, 40L);
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

				if (Runner.isSnakeRunning) {

					for (int i = 0; i < snake.length; i++) {
						Sheep sheep = snake[i];
						if (i == 0) {
							sheep.setVelocity(Runner.snake.getVelocity());
							Location l = sheep.getLocation();
							l.setYaw(getServer().getPlayer(Runner.playerName).getLocation().getYaw());
							sheep.teleport(l);
						} else {
							sheep.setVelocity(snake[i - 1].getVelocity());
							Location l = sheep.getLocation();
							l.setYaw(snake[i - 1].getLocation().getYaw());
							sheep.teleport(l);
						}
					}

				}
			}
		}, 0L, 2L);
		for (i = 0; i < 20; i++) {
			scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

				public void run() {
					if (Runner.isRunning && Runner.players.length > i) {
						Player player = Runner.players[i];
						World world = player.getWorld();
						if (Runner.isRunner(player) && Runner.isRunning) {
							Location loc = player.getLocation();
							int x = loc.getBlockX();
							int y = loc.getBlockY();
							int z = loc.getBlockZ();
							y--;
							Location newloc = new Location(world, x, y, z);
							Material block;
							byte data;
							if (newloc.getBlock().getType() != Material.AIR) {
								try {
									Thread.sleep(130);
									block = newloc.getBlock().getType();
									data = newloc.getBlock().getData();
									newloc.getBlock().setType(Material.AIR);
									world.spawnFallingBlock(newloc, block, data);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}, 0L, 3L);
		}
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
	
	
}