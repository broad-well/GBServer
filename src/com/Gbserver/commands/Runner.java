package com.Gbserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;

import com.Gbserver.Main;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Runner implements CommandExecutor {
	/*
	 * I haven't been getting multi-threaded Runner to work. Here is a logical
	 * simulation:
	 * 
	 * Runner begins. A For loop creates dedicated threads for each player.
	 * (MUST BE SYNCHRONOUS) All threads run. Runner stops. All threads stop.
	 */
	public static Player[] players = new Player[10];
	public static int runnerPlayers;
	Player currentPlayer;
	public static boolean isRunning = false;
	public static boolean isSnakeRunning = false;
	public static Sheep snake;
	public static Player pl;
	public static String playerName;
	@EventHandler
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("runner")) {
			switch (args[0]) {
			case "addPlayer":
				if (args.length < 2) {
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
					return false;
				}
				for (int i = 1; i < args.length; i++) {
					players[i - 1] = Bukkit.getPlayer(args[i]);
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "Added " + args[i] + " to the Runner players list."));
				}
				return true;
			case "removePlayer":
				if (args.length != 2) {
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
					return false;
				}
				for (int i = 1; i < players.length; i++) {
					if (args[1] == players[i - 1].getName()) {
						players[i - 1] = null;
						for (int a = i - 1; a < players.length; a++) {
							if (a != players.length - 1) {
								players[a] = players[a + 1];
							} else {
								players[a] = null;
							}
							/*
							 * 0: Hlo 1: HWr 2: etiohw 3: soieh 4: ser
							 * 
							 * removeL HWr
							 */
						}
					}
				}
				return true;
			case "begin":
				isRunning = true;
				sender.sendMessage("Runner Started!");
				return true;
			case "stop":
				isRunning = false;
				sender.sendMessage("Runner Stopped!");
				return true;
			case "reset":
				isRunning = false;
				for (int i = 0; i < players.length; i++) {
					players[i] = null;
				}
				currentPlayer = null;
				sender.sendMessage("Runner reset done!");
				return true;
			case "snake":
				if(!isSnakeRunning){
				isSnakeRunning = true;
				playerName = ((Player) sender).getName();
				}else{
					isSnakeRunning = false;
					playerName = null;
					Main.snake = new Sheep[50];
				}
				pl = (Player) sender;
				snake = (Sheep) Bukkit.getWorld("world").spawnEntity(pl.getLocation(),EntityType.SHEEP);
    			snake.setPassenger(pl);
				sender.sendMessage("Snake added and started.");
				break;
			case "getpitch":
				double pitch = ((((Player) sender).getLocation().getPitch() + 90) * Math.PI) / 180;
				sender.sendMessage(String.valueOf(pitch));
			default:
				sender.sendMessage("Invalid Syntax.");
				return false;
			}
		}
		return false;
	}

	public static boolean isRunner(Player p) {
		String name = p.getName();
		for (int i = 0; i < players.length; i++) {
			try {
				if (players[i].getName() == name) {
					return true;
				}
			} catch (Exception e) {

			}
		}
		return false;
	}

	
}
