package com.Gbserver.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import com.Gbserver.variables.ScoreDisplay;

public class Lobby2 implements CommandExecutor {
	private int beginTaskID;
	private int countdown;
	public static class TFLocations {
		public static final World world = Bukkit.getWorld("Turf_Wars1");
		public static final Location LobbySpawn = new Location(TFLocations.world, -989.5, 136.5, 1005.5);
		public static final Location BlueSheep = new Location(TFLocations.world, -991.5, 135.5, 996.5);
		public static final Location RedSheep = new Location(TFLocations.world, -986.5, 135.5, 996.5);
	}
	
	public static class BLLocations {
		public static final World world = Bukkit.getWorld("Bomb_Lobbers1");
		public static final Location LobbySpawn = new Location(world, -990.5, 130, 985.5);
		public static final Location BlueSheep = new Location(world, -992.5, 130.5, 976.5);
		public static final Location RedSheep = new Location(world, -988.5, 130.5, 976.5);
	}
	public static class LUtils {
		public static void initTF() {
			LVariables.TFBlue = (Sheep) TFLocations.world.spawnEntity(TFLocations.BlueSheep, EntityType.SHEEP);
			LVariables.TFBlue.setColor(DyeColor.BLUE);
			
			LVariables.TFRed = (Sheep) TFLocations.world.spawnEntity(TFLocations.RedSheep, EntityType.SHEEP);
			LVariables.TFRed.setColor(DyeColor.RED);
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "entitydata "+LVariables.TFRed.getUniqueId()+" {NoAI:1}");
			LVariables.SheepTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

				@Override
				public void run() {
						Vector nul = new Vector(0, 0, 0);
						LVariables.TFBlue.setVelocity(nul);
						LVariables.TFRed.setVelocity(nul);
						LVariables.BLBlue.setVelocity(nul);
						LVariables.BLRed.setVelocity(nul);
					
				}
				
			}, 0L, 1L);
			LVariables.TFsd = new ScoreDisplay("TF Lobby");
			LVariables.TFsd.setLine("Joined: 0", 1);
			LVariables.TFsd.setLine("Blue: 0", 2);
			LVariables.TFsd.setLine("Red: 0", 3);
		}
		
		public static void initBL() {
			LVariables.BLBlue = (Sheep) BLLocations.world.spawnEntity(BLLocations.BlueSheep, EntityType.SHEEP);
			LVariables.BLBlue.setColor(DyeColor.BLUE);
			
			LVariables.BLRed = (Sheep) BLLocations.world.spawnEntity(BLLocations.RedSheep, EntityType.SHEEP);
			LVariables.BLRed.setColor(DyeColor.RED);
			LVariables.BLsd = new ScoreDisplay("BL Lobby");
			LVariables.BLsd.setLine("Joined: 0", 1);
			LVariables.BLsd.setLine("Blue: 0", 2);
			LVariables.BLsd.setLine("Red: 0", 3);
		}
		
		public static void closeTF() {
			LVariables.TFBlue.remove();
			LVariables.TFRed.remove();
			LVariables.TFInBlue.clear();
			LVariables.TFInRed.clear();
			//Bukkit.getScheduler().cancelTask(LVariables.SheepTaskID);
		}
		
		public static void closeBL() {
			LVariables.BLBlue.remove();
			LVariables.BLRed.remove();
			LVariables.BLInBlue.clear();
			LVariables.BLInRed.clear();
		}
		
		public static void updateTFScoreboard() {
			LVariables.TFsd.setLine("Joined: "+LVariables.getAllPlayersTF().size(), 1);
			LVariables.TFsd.setLine("Blue: "+LVariables.TFInBlue.size(), 2);
			LVariables.TFsd.setLine("Red: "+LVariables.TFInRed.size(), 3);
		}
		
		public static void updateBLScoreboard() {
			LVariables.BLsd.setLine("Joined: "+LVariables.getAllPlayersBL().size(), 1);
			LVariables.BLsd.setLine("Blue: "+LVariables.BLInBlue.size(), 2);
			LVariables.BLsd.setLine("Red: "+LVariables.BLInRed.size(), 3);
		}
	}

	public static class LVariables {
		// ----TF----
		public static Sheep TFBlue;
		public static Sheep TFRed;
		public static List<Player> TFInBlue = new LinkedList<>();
		public static List<Player> TFInRed = new LinkedList<>();
		public static List<Player> getAllPlayersTF() {List<Player> returning = new LinkedList<>(); returning.addAll(TFInBlue); returning.addAll(TFInRed); return returning;}
		public static ScoreDisplay TFsd;
		public static int SheepTaskID;
		// ----BL----
		public static Sheep BLBlue;
		public static Sheep BLRed;
		public static List<Player> BLInBlue = new LinkedList<>();
		public static List<Player> BLInRed = new LinkedList<>();
		public static List<Player> getAllPlayersBL() {List<Player> returning = new LinkedList<>(); returning.addAll(BLInBlue); returning.addAll(BLInRed); return returning;}
		public static ScoreDisplay BLsd;
	}

	private HelpTable ht = new HelpTable("/lobby [bl/tf] [join/leave]", "To join or quit a lobby.", "", "lobby");

	public boolean onCommand(CommandSender sender, Command comm, String label, String[] args) {
		if (label.equalsIgnoreCase("lobby") && Utilities.validateSender(sender)) {
			if (args.length < 2) {
				ht.show(sender);
				return true;
			}
			Player p = (Player) sender;
			switch (args[0]) {
			case "bl":
				switch (args[1]) {
				case "join":
					// Join BL.
					if(LVariables.getAllPlayersBL().contains(p)){
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You already joined BL! To quit, use /lobby bl leave."));
						return true;
					}
					if(LVariables.BLInBlue.size() > LVariables.BLInRed.size()){
						//Join red.
						LVariables.BLInRed.add(p);
						LUtils.updateBLScoreboard();
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Added you to "+ChatColor.RED+"Red Team"));
						return true;
					}else{
						//Join blue.
						LVariables.BLInBlue.add(p);
						LUtils.updateBLScoreboard();
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Added you to "+ChatColor.BLUE+"Blue Team"));
						return true;
					}
				case "leave":
					// Leave BL.
					if(LVariables.BLInBlue.contains(p)){
						LVariables.BLInBlue.remove(p);
					}else if(LVariables.BLInRed.contains(p)){
						LVariables.BLInRed.remove(p);
					}else{
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are not in the BL game."));
						return true;
					}
					p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Removed you from the BL game."));
					LUtils.updateBLScoreboard();
					break;
				case "start":
					countdown = 15;
					beginTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
						public void run() {
							if(countdown == 0){
								List<String> pls = new LinkedList<>();
								for(Player pa : LVariables.BLInRed){
									pls.add(pa.getName());
									pa.teleport(new Location(BL.world,23.5,102.67,58.1));
								}
								for(Player pal : LVariables.BLInBlue){
									pls.add(pal.getName());
									pal.teleport(new Location(BL.world,21.8,103,14.61));
								}
								BL.players.addAll(pls);
								
								
								LUtils.closeBL();
								Bukkit.getScheduler().cancelTask(beginTaskID);
							}else{
								countdown--;
								LVariables.BLsd.setLine(ChatColor.BOLD + "GAME STARTING IN", 5);
								LVariables.BLsd.setLine(countdown + " seconds", 6);
								LVariables.BLsd.setLine(ChatColor.ITALIC + "Lag is normal.", 8);
							}
						}
					}, 0L, 20L);
					BL.removePreviousMap();
					BL.getMap(1);
				default:
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Options."));
					ht.show(sender);
					return true;
				}
				break;
			case "tf":
				switch (args[1]) {
				case "join":
					// Join TF.
					if(LVariables.getAllPlayersTF().contains(p)){
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You already joined TF! To quit, use /lobby tf leave."));
						return true;
					}
					if(LVariables.TFInBlue.size() > LVariables.TFInRed.size()){
						//Join red.
						LVariables.TFInRed.add(p);
						LUtils.updateTFScoreboard();
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Added you to "+ChatColor.RED+"Red Team"));
						return true;
					}else{
						//Join blue.
						LVariables.TFInBlue.add(p);
						LUtils.updateTFScoreboard();
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Added you to "+ChatColor.BLUE+"Blue Team"));
						return true;
					}
				case "leave":
					// Leave TF.
					if(LVariables.TFInBlue.contains(p)){
						LVariables.TFInBlue.remove(p);
					}else if(LVariables.TFInRed.contains(p)){
						LVariables.TFInRed.remove(p);
					}else{
						p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are not in the TF game."));
						return true;
					}
					p.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Removed you from the TF game."));
					LUtils.updateTFScoreboard();
					break;
				case "start":
					
					
					
					
					countdown = 15;
					beginTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
						public void run() {
							if(countdown == 0){
								TF.bluePlayers.addAll(LVariables.TFInBlue);
								TF.redPlayers.addAll(LVariables.TFInRed);
								TF.startGame();
								LUtils.closeTF();
								Bukkit.getScheduler().cancelTask(beginTaskID);
							}else{
								countdown--;
								LVariables.BLsd.setLine("GAME STARTING IN", 5);
								LVariables.BLsd.setLine(countdown + " seconds", 6);
							}
						}
					}, 0L, 20L);
				default:
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Options."));
					ht.show(sender);
					return true;
				}
				break;
			default:
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Options."));
				ht.show(sender);
				return true;
			}
		}else{
			return false;
		}
		return false;
	}
}
