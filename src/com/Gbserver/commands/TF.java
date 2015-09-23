package com.Gbserver.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.Gbserver.Main;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.ScoreDisplay;

/*
 * On begin:
 * 1. Teleport to position
 * 2. Refill inventory
 * 
 * During game (listeners)
 * 1. Building on own turf
 * 2. Staying on own turf
 * 3. Eliminate all damages except arrows
 * 4. Shift positions on arrow hit
 * 
 * 
 */

public class TF implements CommandExecutor {

	public static final byte RED_CLAY = (byte) 14;
	public static final byte BLUE_CLAY = (byte) 11;
	public static World World = Bukkit.getWorld("Turf_Wars1");
	// public static int shiftRelation = 0;
	// Red: Increase
	// Blue: Decrease
	// public static int thresholdRed = -11;
	// public static int thresholdBlue = -10;
	public static final int[][] COOR = { { -50, 100, 25 }, { -11, 100, -24 }, // ^
			// RED
			// selection
			{ 29, 100, 25 }, { -10, 100, -24 } // ^ BLUE selection

	};
	public static List<Player> clickInventory = new LinkedList<>();
	public static boolean isRunning = false;
	public static boolean isBuildtime = false;
	public static final int[] RedSpawn = { -53, 102, 0 };
	public static final int[] BlueSpawn = { 33, 102, 0 };
	public static Inventory menu = Bukkit.createInventory(null, 9, "Turf Wars Menu");
	public static List<Player> redPlayers = new LinkedList<Player>();
	public static List<Player> bluePlayers = new LinkedList<Player>();
	static ScoreDisplay sd;
	static int countdown;
	static boolean isDone = false;
	static int task1;
	static int task2;
	private static Thread s = new Thread() {
		public void run() {
			boolean isValid = true;
			while (isRunning && isValid) {
				final BukkitScheduler bs = Bukkit.getServer().getScheduler();
				if (isValid) {
//					bs.scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
//						public void run() {
//
//							broadcastToPlayers(ChatWriter.getMessage(ChatWriterType.GAME, "Combat time"));
//							isBuildtime = false;
//						}
//					}, 30 * 20L);
					ChatWriter.write(ChatWriterType.GAME, "Trying to set scoreboard");
					countdown = 30;
					task1 = bs.scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

						@Override
						public void run() {
							sd.setLine("Combat in: "+countdown, 3);
							sd.setLine(ChatColor.BLUE + "" + ChatColor.BOLD + "   -BUILD TIME-   ", 4);
							if(countdown == 0){
								isDone = true;
								bs.cancelTask(task1);
							}else{
							countdown--;
							}
						}
						
					}, 0L, 20L);
				}
				while (!isDone) {
					if (Thread.currentThread().isInterrupted()) {
						isValid = false;
						break;
					}
				}
				isBuildtime = false;
				isDone = false;
				if (isValid) {
					/*bs.scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
						public void run() {
							broadcastToPlayers(ChatWriter.getMessage(ChatWriterType.GAME, "Build time"));
							isBuildtime = true;
						}
					}, 180 * 20L);*/
					ChatWriter.write(ChatWriterType.GAME, "Trying to set scoreboard");
					countdown = 150;
					isDone = false;
					task2 = bs.scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

						@Override
						public void run() {
							sd.setLine("Build in: "+countdown, 3);
							sd.setLine(ChatColor.RED + "" + ChatColor.BOLD + "   -COMBAT TIME-   ", 4);
							if(countdown == 0){
								isDone = true;
								bs.cancelTask(task2);
							}else{
							countdown--;
							}
						}
						
					}, 0L, 20L);
				}
				while (!isDone) {
					if (Thread.currentThread().isInterrupted()) {
						isValid = false;
						break;
					}
				}
				isBuildtime = true;
				isDone = false;
			}
		}
	};
	/*
	 * THread: 1. Register event 2. Wait until event declares a specified
	 * boolean 3. Register another event 4. Wait until event declares a
	 * specified boolean
	 * 
	 */

	@SuppressWarnings("deprecation")
	public static void setupInventory() {
		ItemStack is1 = new ItemStack(Material.INK_SACK, redPlayers.size(), (byte) 1);
		ItemStack is2 = new ItemStack(Material.INK_SACK, bluePlayers.size(), (byte) 4);
		ItemStack is3 = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());
		ItemStack is4 = new ItemStack(Material.TNT, 1);
		is1 = setDisplayName(ChatColor.RED + "Join Red", is1);
		is2 = setDisplayName(ChatColor.BLUE + "Join Blue", is2);
		is3 = setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Quit Game", is3);
		is4 = setDisplayName("Exit Menu", is4);
		menu.setItem(0, is1);
		menu.setItem(1, is2);
		menu.setItem(7, is3);
		menu.setItem(8, is4);

	}

	private static int inventoryCount(boolean isRed) {
		if (isRed) {
			// Red
			if (redPlayers.toArray().length != 0) {
				return redPlayers.size();
			} else {
				return 1;
			}
		} else {
			// Blue
			if (bluePlayers.toArray().length != 0) {
				return redPlayers.size();
			} else {
				return 1;
			}
		}
	}

	public static ItemStack setDisplayName(String name, ItemStack is) {
		ItemMeta m = is.getItemMeta();
		m.setDisplayName(name);
		is.setItemMeta(m);
		return is;
	}
	
	private static void setupScoreboard() {
		sd = new ScoreDisplay("Turf Wars");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("tf")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
				return false;
			}
			Player p = (Player) sender;

			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("begin")) {
					setupScoreboard();
					isRunning = true;
					isBuildtime = true;
					for (Object o : redPlayers) {
						Player pl = (Player) o;
						pl.teleport(new Location(Bukkit.getWorld("Turf_Wars1"), RedSpawn[0], RedSpawn[1], RedSpawn[2]));
					}
					for (Object o : bluePlayers) {
						Player pl = (Player) o;
						pl.teleport(
								new Location(Bukkit.getWorld("Turf_Wars1"), BlueSpawn[0], BlueSpawn[1], BlueSpawn[2]));
					}
					fillInventory(false);
					fillInventory(true);
					s.start();
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Game Started."));
				}
				if (args[0].equalsIgnoreCase("toggle")) {
					if (isBuildtime) {
						isBuildtime = false;
					} else {
						isBuildtime = true;
					}
					sender.sendMessage(isBuildtime + "");
				}
				if (args[0].equalsIgnoreCase("stop")) {
					isRunning = false;
					isBuildtime = false;
					redPlayers.clear();
					bluePlayers.clear();
					resetMap();
					s.interrupt();
					s.stop();
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Game Stopped."));
				}
			} else {
				setupInventory();
				p.openInventory(menu);
				clickInventory.add(p);
			}
			return true;

		}
		return false;
	}

	public static List<Player> getAllPlayers() {
		List<Player> l = new LinkedList<Player>(redPlayers);
		l.addAll(bluePlayers);
		return l;
	}

	@SuppressWarnings("deprecation")
	public static void fillInventory(boolean isBuildTime) {
		if (isRunning) {

			for (Object o : getAllPlayers().toArray()) {
				Player p = (Player) o;
				if (isBuildTime) {
					byte data;
					if (redPlayers.contains(p)) {
						data = (byte) 14;
					} else {
						data = (byte) 11;
					}
					p.getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 64, (short) 0, data));
					p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME,"Filled your inventory."));
				} else {
					p.getInventory().addItem(new ItemStack(Material.BOW, 2));
					p.getInventory().addItem(new ItemStack(Material.ARROW, 64));
					p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME,"Filled your inventory."));

				}
			}
		}
	}

	public static void broadcastToPlayers(String message) {
		for (Player p : getAllPlayers()) {
			p.sendMessage(message);
		}
	}

	private static void fillStrip(int x, byte color) {
		// for: Z axis

		// x: specified
		// y: 100
		boolean ahead = true;
		if(x == COOR[0][0] || x == COOR[2][0]){
			if(x == COOR[0][0]){
				broadcastToPlayers(ChatWriter.getMessage(ChatWriterType.GAME,ChatColor.BLUE + "BLUE WINS!"));
			}else{
				broadcastToPlayers(ChatWriter.getMessage(ChatWriterType.GAME,ChatColor.RED + "RED WINS!"));
			}
			isRunning = false;
			isBuildtime = false;
			redPlayers.clear();
			bluePlayers.clear();
			resetMap();
			s.interrupt();
			ahead = false;
			
		}
		if(ahead){
			for (int z = COOR[1][2]; z <= COOR[0][2]; z++) {
				Location l = new Location(Bukkit.getWorld("Turf_Wars1"), x, 100, z);
				l.getBlock().setType(Material.STAINED_CLAY);
				l.getBlock().setData(color);
			}
	
			// 2nd for: Y & Z
			// x: specified
			// y: 100~110
			for (int y = 101; y <= 110; y++) {
				for (int z = COOR[1][2]; z <= COOR[0][2]; z++) {
					Location l = new Location(Bukkit.getWorld("Turf_Wars1"), x, y, z);
					if (l.getBlock().getType().equals(Material.STAINED_CLAY)) {
						l.getBlock().setType(Material.AIR);
					}
				}
			}
		}
	}

	public static void advanceBlocks(boolean isRed) {
		int targetX;
		if (isRed) {
			final int[] SCAN = { -50, 100, -24 };
			World w = Bukkit.getWorld("Turf_Wars1");
			int scanCount = 0;
			boolean Noquit = true;
			while (Noquit) {
				if ((new Location(w, SCAN[0] + scanCount, SCAN[1], SCAN[2]).getBlock().getData() == BLUE_CLAY)) {

					targetX = SCAN[0] + scanCount;
					fillStrip(targetX, RED_CLAY);
					break;
				} else {
					scanCount++;
				}
			}
		} else {
			final int[] SCAN = { 29, 100, -24 };
			World w = Bukkit.getWorld("Turf_Wars1");
			int scanCount = 0;
			boolean Noquit = true;
			while (Noquit) {
				if ((new Location(w, SCAN[0] - scanCount, SCAN[1], SCAN[2]).getBlock().getData() == RED_CLAY)) {

					targetX = SCAN[0] - scanCount;
					fillStrip(targetX, BLUE_CLAY);
					break;
				} else {
					scanCount++;
				}
			}
		}
	}

	private static void resetMap() {
		for (int x = COOR[0][0]; x <= COOR[1][0]; x++) {
			for (int z = COOR[1][2]; z <= COOR[0][2]; z++) {
				Location l = new Location(Bukkit.getWorld("Turf_Wars1"), x, 100, z);
				if (l.getBlock().getData() != RED_CLAY) {
					l.getBlock().setData(RED_CLAY);
				}
				for (int y = 101; y <= 110; y++) {
					l.setY(y);
					if (l.getBlock().getType() != Material.AIR) {
						l.getBlock().setType(Material.AIR);
					}
				}
			}
		}
		for (int x = COOR[3][0]; x <= COOR[2][0]; x++) {
			for (int z = COOR[3][2]; z <= COOR[2][2]; z++) {
				Location l = new Location(Bukkit.getWorld("Turf_Wars1"), x, 100, z);
				if (l.getBlock().getData() != BLUE_CLAY) {
					l.getBlock().setData(BLUE_CLAY);
				}
				for (int y = 101; y <= 110; y++) {
					l.setY(y);
					if (l.getBlock().getType() != Material.AIR) {
						l.getBlock().setType(Material.AIR);
					}
				}
			}
		}
	}

	public static void startGame() {
		setupScoreboard();
		isRunning = true;
		isBuildtime = true;
		for (Object o : redPlayers) {
			Player pl = (Player) o;
			pl.teleport(new Location(Bukkit.getWorld("Turf_Wars1"), RedSpawn[0], RedSpawn[1], RedSpawn[2]));
		}
		for (Object o : bluePlayers) {
			Player pl = (Player) o;
			pl.teleport(
					new Location(Bukkit.getWorld("Turf_Wars1"), BlueSpawn[0], BlueSpawn[1], BlueSpawn[2]));
		}
		fillInventory(true);
		if(!s.isAlive()){
			s.start();
		}

	}
}
