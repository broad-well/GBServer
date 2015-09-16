package com.Gbserver.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;

public class BL implements CommandExecutor {
	public static Collection<String> players = new LinkedList<String>();
	public static boolean isRunning = false;
	public static World world = Bukkit.getWorld("Bomb_Lobbers1");
	public static int[][] Beach = {
			{ 517 , 98 , 557 },
			{ 474 , 125, 485 },
			{  43 , 95 ,  -1 },
			{  -2 , 123,  73 }
	};
	
	private HelpTable ht = new HelpTable("/bl <addPlayer/start/reset> <player (only required for addPlayer)>", "This command is used to control the Bomb Lobbers minigame.", "", "bl");
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("bl")) {
			if (args.length < 1) {
				ht.show(sender);
				return true;
			}
			switch (args[0]) {
			case "addPlayer":
				if (args.length < 2) {
					ht.show(sender);
					return true;
				}
				for (int i = 1; i < args.length; i++) {
					Player p = Bukkit.getServer().getPlayer(args[i]);
					players.add(p.getName());
					sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully added " + p.getName() + " to BL players list."));
				}
				return true;
			case "start":
				isRunning = true;
				for(Object p : players.toArray()){
					Player pl = Bukkit.getServer().getPlayer((String) p);
				}
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully started the game."));
				return true;
			case "reset":
				isRunning = false;
				players.clear();
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully reset the game."));
				return true;
			}
		}
		return false;
	}
	
	public static void removePreviousMap() {
		for(int x = Beach[2][0]; x > Beach[3][0]; x--){
			for(int y = Beach[2][1]; y < Beach[3][1]; y++){
				for(int z = Beach[2][2]; z < Beach[3][2]; z++){
					world.getBlockAt(x,y,z).setType(Material.AIR);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void getMap(int type){
		/*
		 * 1: Beach
		 * 2: Forest
		 * 3: Motherboard
		 */
		
		int[] sample = {0,150,0};
		
		if(type == 1){
			//Tri-for
			for(int x = Beach[0][0]; x > Beach[1][0]; x--){
				for(int y = Beach[0][1]; y < Beach[1][1]; y++){
					for(int z = Beach[0][2]; z > Beach[1][2]; z--){
						Block b = world.getBlockAt(new Location(world, x, y, z));
						int x1 = Beach[0][0] - x;
						int y1 = y;
						int z1 = Beach[0][2] - z;
						world.getBlockAt(new Location(world,x1,y1,z1)).setType(b.getType());
						world.getBlockAt(new Location(world,x1,y1,z1)).setData(b.getData());
					}
				}
			}
		}
		if(type == 2){
			
		}
		if(type == 3){
			
		}
	}
}
