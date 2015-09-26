package com.Gbserver.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;

public class CTF implements CommandExecutor{
	//VARIABLES ARE LOCATED HERE
	//BEGIN OF VARIABLES
	public static World world = Bukkit.getWorld("CTF");
	public static boolean isRunning = false;
	public static Item redFlag;
	public static Item blueFlag;
	public static Location redFlagLocation = new Location(world,0,0,0);
	public static Location blueFlagLocation = new Location(world, 0,0,0);
	public static List<Player> red = new LinkedList<Player>();
	public static List<Player> blue = new LinkedList<Player>();
	//PRIVATE
	private static int frozenid;
	private static HelpTable ht = new HelpTable("/ctf [stop/stats]", "Capture-the-flag commands", "", "ctf");
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("ctf")){
			//Game stopping & statistics.
			if(args.length == 0){
				ht.show(sender);
				return true;
			}
			switch(args[0]){
			case "stop":
				stopGame();
				ChatWriter.writeTo(sender, ChatWriterType.GAME, "Successfully stopped the CTF game.");
				break;
			case "stats":
				//Statistics here.
				//Collections of statistics...??
				break;
			}
			return true;
		}
		return false;
	}
	
	public static void getVariables() {
		redFlag = world.dropItem(redFlagLocation, new ItemStack(Material.REDSTONE_BLOCK));
		redFlag.setPickupDelay(Integer.MAX_VALUE);
		blueFlag = world.dropItem(blueFlagLocation, new ItemStack(Material.LAPIS_BLOCK));
		blueFlag.setPickupDelay(Integer.MAX_VALUE);
		frozenid = Utilities.setFrozen(redFlag, blueFlag);
		//flags are spawned.
		
	}
	
	public static List<Player> allPlayers() {
		List<Player> l = new LinkedList<>();
		l.addAll(red);
		l.addAll(blue);
		return l;
	}
	
	public static void stopGame() {
		isRunning = false;
		redFlag.remove();
		blueFlag.remove();
		red.clear();
		blue.clear();
		Bukkit.getScheduler().cancelTask(frozenid);
	}
}
