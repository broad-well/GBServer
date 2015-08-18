package com.Gbserver;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.Gbserver.commands.Afk;
import com.Gbserver.commands.Attentions;
import com.Gbserver.commands.BL;
import com.Gbserver.commands.Back;
import com.Gbserver.commands.Gamemode;
import com.Gbserver.commands.Heal;
import com.Gbserver.commands.Menu;
import com.Gbserver.commands.Nick;
import com.Gbserver.commands.Protection;
import com.Gbserver.commands.Ride;
import com.Gbserver.commands.Runner;
import com.Gbserver.commands.SaveMoment;
import com.Gbserver.commands.Spawn;
import com.Gbserver.commands.TF;
import com.Gbserver.commands.Tell;
import com.Gbserver.commands.runnerListenerDepricated;
import com.Gbserver.listener.AfkListener;
import com.Gbserver.listener.BLListener;
import com.Gbserver.listener.BackListeners;
import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.listener.ExplosionListener;
import com.Gbserver.listener.HealListener;
import com.Gbserver.listener.LoginTagListener;
import com.Gbserver.listener.MenuListener;
import com.Gbserver.listener.NickListener;
import com.Gbserver.listener.ProtectionListener;
import com.Gbserver.listener.RideListener;
import com.Gbserver.listener.TFListeners;

public class Main extends JavaPlugin {
	Vector vector;
	Sheep sh = null;
	int runCount = 0;
	boolean isWait = false;
	Player p = Runner.pl;
	public static Sheep[] snake = new Sheep[50];
	static int i;
	int TFCount = 0;

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