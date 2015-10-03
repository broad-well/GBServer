package com.Gbserver.listener;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class ProtectionListener implements Listener {
	final int[][][] DATA = { 
			{
				{ -156, 78, 228 }, 
				{ -130, 68, 208 }, 
			},
			{
				{  72,  65, 365 }, 
				{ -26, 254, 277 },
			},
			{
				{ 165, 101, 433 }, 
				{ 124, 145, 392 },
			},
			{
				{-162,  71, 185 },
				{-163,  75, 175 }
			}
	};
	
	final String[][] TRUSTED_PLAYERS = {
			{"GoBroadwell"},
			{"_Broadwell","Yin_of_the_Yang","Latios_"},
			{""},
			{"_Broadwell"}
	};
	
	final int RIGHT_UP = 0;
	final int LEFT_DOWN = 1;

	final int AXIS_X = 0;
	final int AXIS_Y = 1;
	final int AXIS_Z = 2;

	@EventHandler
	public void onBlockBreak(BlockBreakEvent bbe){
		int x = bbe.getBlock().getX();
		int y = bbe.getBlock().getY();
		int z = bbe.getBlock().getZ();
		
			//bbe.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "This area is protected. Sorry!");
			//bbe.setCancelled(true);
		for(int i = 0; i < DATA.length; i++){
			if(isInSelection(i, x, y, z) && !isTrusted(i, bbe.getPlayer())){
				if(i == 0 && bbe.getBlock().getType() == Material.SNOW_BLOCK){
					
				}else{
					bbe.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, ChatColor.RED + "" + ChatColor.BOLD + "This area is protected. Sorry!"));
					bbe.setCancelled(true);
				}
			}
			if(bbe.getBlock().getWorld().equals(Bukkit.getServer().getWorld("Bomb_Lobbers1")) && bbe.getBlock().getType() == Material.BEDROCK){
				bbe.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, ChatColor.RED + "" + ChatColor.BOLD + "This area is protected. Sorry!"));
				bbe.setCancelled(true);
			}
		}
		
		
	}

	private boolean isInRangeOf(int testant, int min, int max) {
		if (min < max) {
			if (testant >= min && testant <= max) {
				return true;
			} else {
				return false;
			}
		} else if (min != max) {
			if (testant <= min && testant >= max) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private boolean isInSelection(int selectionNumber, int x, int y, int z){
		
		if(isInRangeOf(x,DATA[selectionNumber][RIGHT_UP][AXIS_X],DATA[selectionNumber][LEFT_DOWN][AXIS_X])
				&& isInRangeOf(y,DATA[selectionNumber][RIGHT_UP][AXIS_Y],DATA[selectionNumber][LEFT_DOWN][AXIS_Y])
				&& isInRangeOf(z,DATA[selectionNumber][RIGHT_UP][AXIS_Z],DATA[selectionNumber][LEFT_DOWN][AXIS_Z])
				//&& !isInRangeOf(x,DATA[selectionNumber][EXCLUDE_RIGHT_UP][AXIS_X],DATA[selectionNumber][EXCLUDE_LEFT_DOWN][AXIS_X])
				//&& !isInRangeOf(y,DATA[selectionNumber][EXCLUDE_RIGHT_UP][AXIS_Y],DATA[selectionNumber][EXCLUDE_LEFT_DOWN][AXIS_Y])
				//&& !isInRangeOf(z,DATA[selectionNumber][EXCLUDE_RIGHT_UP][AXIS_Z],DATA[selectionNumber][EXCLUDE_LEFT_DOWN][AXIS_Z])
				){
			
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isTrusted(int selectionNumber, Player contestant) {
		String name = contestant.getName();
		if(Arrays.asList(TRUSTED_PLAYERS[selectionNumber]).contains(name)){
			return true;
		}else{
			return false;
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent bbe){
		int x = bbe.getBlock().getX();
		int y = bbe.getBlock().getY();
		int z = bbe.getBlock().getZ();
		
			//bbe.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "This area is protected. Sorry!");
			//bbe.setCancelled(true);
		for(int i = 0; i < DATA.length; i++){
			if(isInSelection(i, x, y, z) && !isTrusted(i, bbe.getPlayer())){
				if(i == 0 && bbe.getBlock().getType() == Material.SNOW_BLOCK){
					
				}else{
					bbe.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, ChatColor.RED + "" + ChatColor.BOLD + "This area is protected. Sorry!"));
					bbe.setCancelled(true);
				}
			}
		}
	}
}
