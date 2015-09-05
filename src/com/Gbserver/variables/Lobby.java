package com.Gbserver.variables;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.Gbserver.Main;
import com.Gbserver.commands.BL;
import com.Gbserver.commands.TF;

public class Lobby implements Listener {
	private LT type;
	public List<Player> red = new LinkedList<>();
	public List<Player> blue = new LinkedList<>();
	private ScoreDisplay sd;
	private Sheep blueJoin = null;
	private Sheep redJoin = null;
	private Sheep Quit = null;

	public static List<Lobby> lobbies = new LinkedList<>();
	public static boolean hasTF = false;
	public static boolean hasBL = false;
	private static Location TFBlue = new Location(Bukkit.getWorld("Turf_Wars1"), 804.429, 103.5, 796.562);
	private static Location TFRed = new Location(Bukkit.getWorld("Turf_Wars1"), 800.429, 103.5, 796.562);
	private static Location BLBlue = new Location(BL.world, 810.5, 109.5, 795.5);
	private static Location BLRed = new Location(BL.world, 806.5, 109.5, 795.5);

	public Lobby(LT type) throws Exception {
		this.type = type;
		switch (type) {
		case BL:
			if (hasBL) {
				throw new Exception("Bomb Lobbers lobby is already created!");
			}
			sd = new ScoreDisplay("BL Lobby");
			hasBL = true;
			break;
		case TF:
			if (hasTF) {
				throw new Exception("Turf Wars lobby is already created!");
			}
			sd = new ScoreDisplay("TF Lobby");
			hasTF = true;
			break;
		case RUN:
			break;
		default:
			break;
		}

		sd.setLine("Joined: 0", 1);
		sd.setLine(ChatColor.BLUE + "Blue: 0", 2);
		sd.setLine(ChatColor.RED + "Red: 0", 3);
		lobbies.add(this);

	}

	// public void addPlayer(Player p){
	// players.add(p);
	// PlayerChangeEvent
	// sd.setLine("Joined: "+players.size(), 1);
	// }

	public void addPlayer(Player p, boolean isRed) {
		if (isRed) {
			red.add(p);
			sd.setLine(ChatColor.RED + "Red: " + red.size(), 3);
		} else {
			blue.add(p);
			sd.setLine(ChatColor.BLUE + "Blue: " + blue.size(), 2);
		}
		sd.setLine("Joined: " + (blue.size() + red.size()), 1);
		if ((blue.size() + red.size()) > 1) {
			startGame();
		}
	}

	public void removePlayer(Player p, boolean quitLobby) {
		red.remove(p);
		sd.setLine(ChatColor.RED + "Red: " + red.size(), 3);
		blue.remove(p);
		sd.setLine(ChatColor.BLUE + "Blue: " + blue.size(), 2);
		sd.setLine("Joined: " + (blue.size() + red.size()), 1);
	}
	/*
	 * <type> LOBBY Joined: 3 Blue: Red:
	 */

	public LT getType() {
		return type;
	}

	public Sheep getBlueJoin() {
		return blueJoin;
	}

	public Sheep getRedJoin() {
		return redJoin;
	}

	public void setRedJoin(Sheep redJoin) {
		this.redJoin = redJoin;
	}

	public void setBlueJoin(Sheep blueJoin) {
		this.blueJoin = blueJoin;
	}

	public Sheep getQuit() {
		return Quit;
	}

	public void setQuit(Sheep quit) {
		Quit = quit;
	}

	public static Lobby getLobby(LT lt) {
		for (Lobby l : lobbies) {
			if (l.getType() == lt) {
				return l;
			}
		}
		return null;
	}

	public static void setSheeps(LT type) {
		if (type == LT.TF) {
			final Sheep s = (Sheep) TF.World.spawnEntity(TFBlue, EntityType.SHEEP);
			s.setColor(DyeColor.BLUE);
			s.teleport(TFBlue);
			getLobby(type).setBlueJoin(s);

			final Sheep s2 = (Sheep) TF.World.spawnEntity(TFRed, EntityType.SHEEP);
			s2.setColor(DyeColor.RED);
			s2.teleport(TFRed);
			getLobby(type).setRedJoin(s2);

			Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

				@Override
				public void run() {

					s.setVelocity(new Vector(0, 0, 0));

					s2.setVelocity(new Vector(0, 0, 0));
				}

			}, 0L, 1L);
		}

		if (type == LT.BL) {
			final Sheep s = (Sheep) TF.World.spawnEntity(BLBlue, EntityType.SHEEP);
			s.setColor(DyeColor.BLUE);
			s.teleport(BLBlue);
			getLobby(type).setBlueJoin(s);

			final Sheep s2 = (Sheep) TF.World.spawnEntity(BLRed, EntityType.SHEEP);
			s2.setColor(DyeColor.RED);
			s2.teleport(BLRed);
			getLobby(type).setRedJoin(s2);

			Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

				@Override
				public void run() {

					s.setVelocity(new Vector(0, 0, 0));

					s2.setVelocity(new Vector(0, 0, 0));
				}

			}, 0L, 1L);
		}
	}

	private int countdown = 30;
	private int task;

	private void startGame() {
		if(type == LT.TF){
			countdown = 30;
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

			@Override
			public void run() {
				sd.setLine(ChatColor.BOLD + "Game starting in", 5);
				sd.setLine(countdown + "", 6);
				if (countdown != 1) {
					countdown--;
				} else {
					TF.bluePlayers.addAll(blue);
					TF.redPlayers.addAll(red);
					TF.startGame();
					Bukkit.getScheduler().cancelTask(task);
				}
			}

		}, 0L, 20L);
		} else{
			
			sd.setLine("Lag is normal.", 8);
			countdown = 30;
			task = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

				@Override
				public void run() {
					sd.setLine(ChatColor.BOLD + "Game starting in", 5);
					sd.setLine(countdown + "", 6);
					if (countdown != 1) {
						countdown--;
					} else {
						
						List<String> blueNames = new LinkedList<>();
						List<String> redNames = new LinkedList<>();
						for(Player p : red){
							redNames.add(p.getName());
							p.teleport(new Location(BL.world,23.5,102.67,58.1));
						}
						for(Player p : blue){
							blueNames.add(p.getName());
							p.teleport(new Location(BL.world,21.8,103,14.61));
						}
						
						BL.players.addAll(redNames);
						BL.players.addAll(blueNames);
						sd.reset();
						BL.isRunning = true;
						red.clear();
						blue.clear();
						Bukkit.getScheduler().cancelTask(task);
					}
				}

			}, 0L, 20L);
			BL.removePreviousMap();
			BL.getMap(1);
		}
	}
}
