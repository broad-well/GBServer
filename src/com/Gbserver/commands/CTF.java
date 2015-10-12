package com.Gbserver.commands;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class CTF implements CommandExecutor{
	//VARIABLES ARE LOCATED HERE
	//BEGIN OF VARIABLES
	public static World world = Bukkit.getWorld("CTF");
	public static boolean isRunning = false;
	public static Sheep redFlag;
	public static Sheep blueFlag;
	public static Location redFlagLocation = new Location(world,-36.5,105,89.5);
	public static Location blueFlagLocation = new Location(world, 31.5,105,-88.5);
	public static List<Player> red = new LinkedList<Player>();
	public static List<Player> blue = new LinkedList<Player>();
	public static Collection<Integer> tasks = new LinkedList<>();
	public static int frozenblue;
	public static int frozenred;
	public static Location spectate = new Location(world, 0, 135, -3);
	//PRIVATE
	
	private static HelpTable ht = new HelpTable("/ctf [stop/stats]", "Capture-the-flag commands", "", "ctf");
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("ctf")){
			//Game stopping & statistics.
			if(args.length == 0){
				ht.show(sender);
				return true;
			}
			switch(args[0]){
			case "dk3addblue":
				blue.add(Bukkit.getPlayer(args[1]));
				sender.sendMessage("Player debuggedly added.");
				break;
			case "dk3start":
				startGame();
				sender.sendMessage("Game debuggedly started.");
				break;
			case "stop":
				stopGame();
				ChatWriter.writeTo(sender, ChatWriterType.GAME, "Successfully stopped the CTF game.");
				break;
			case "stats":
				//Statistics here.
				//Collections of statistics...??
				sender.sendMessage("--------------------");
				sender.sendMessage("Red flag freezer running: " + (Bukkit.getScheduler().isCurrentlyRunning(frozenred)));
				sender.sendMessage("Blue flag freezer running: " + (Bukkit.getScheduler().isCurrentlyRunning(frozenblue)));
				sender.sendMessage("--------------------");
				sender.sendMessage("A list of all tasks:");
				for(Integer i : tasks){
					sender.sendMessage(i+"");
				}
				sender.sendMessage("--------------------");
				{
					String output = "Red team players: ";
					for(Player p : red){
						output += p.getName() + ", ";
					}
					sender.sendMessage(output);
				}
				{
					String output = "Blue team players: ";
					for(Player p : blue){
						output += p.getName() + ", ";
					}
					sender.sendMessage(output);
				}
				sender.sendMessage("--------------------");
				sender.sendMessage("Is CTF Running? : "+isRunning);
				break;
			}
			return true;
		}
		return false;
	}
	
	public static void getVariables() {
		world.loadChunk(redFlagLocation.getChunk());
		redFlag = (Sheep) world.spawnEntity(redFlagLocation, EntityType.SHEEP);
		redFlag.setColor(DyeColor.RED);
		world.loadChunk(blueFlagLocation.getChunk());
		blueFlag = (Sheep) world.spawnEntity(blueFlagLocation, EntityType.SHEEP);
		blueFlag.setColor(DyeColor.BLUE);
		frozenred = Utilities.setFrozen(redFlag);
		frozenblue = Utilities.setFrozen(blueFlag);
		//flags are spawned.
	}
	//subvoid
	public static Team getLocationTeam(Player p){
		Location l = p.getLocation();
		//Negative: blue, positive: red
		if(l.getZ() < 0){
			return Team.BLUE;
		}
		if(l.getZ() > 0){
			return Team.RED;
		}
		return Team.undefined;
	}
	
	public static Team getOriginatedTeam(Player p){
		if(red.contains(p)){
			return Team.RED;
		}
		if(blue.contains(p)){
			return Team.BLUE;
		}
		return Team.undefined;
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
		for(Integer i : tasks){
			Bukkit.getScheduler().cancelTask(i);
		}
		Bukkit.getScheduler().cancelTask(frozenred);
		Bukkit.getScheduler().cancelTask(frozenblue);
	}
	
	public static void startGame() {
		isRunning = true;
		ItemStack scissors = new ItemStack(Material.SHEARS);
		{
			ItemMeta sciim;
			(sciim = scissors.getItemMeta()).setDisplayName("Flag Capturer");
			scissors.setItemMeta(sciim);
		}
		ItemStack inksac = new ItemStack(Material.INK_SACK);
		{
			ItemMeta inkim;
			(inkim = inksac.getItemMeta()).setDisplayName("Opponent Capturer");
			inksac.setItemMeta(inkim);
		}


		for(Player p : blue){
			p.getInventory().setArmorContents(getArmor(Color.BLUE));
		}
		for(Player p : red){
			p.getInventory().setArmorContents(getArmor(Color.RED));
		}
		for(Player p : allPlayers()){
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().clear();
			p.getInventory().addItem(scissors, inksac);
		}
		CTF.getVariables();
	}
	
	public static Location getSpawn(Team t){
		if(t == Team.BLUE){
			return new Location(world, -4.5, 110, -12);
					
		}
		if(t == Team.RED){
			return new Location(world, -4.5, 110, 12);
		}
		return null;
	}
	
	public static Location getFlagByTeam(Team t){
		switch(t){
		case BLUE:
			return blueFlagLocation;
		case RED:
			return redFlagLocation;
		default:
			return null;
		}
	}

	public static ItemStack[] getArmor(Color c){
		ItemStack blueboot = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta lambboot = (LeatherArmorMeta) blueboot.getItemMeta();
		lambboot.setColor(c);
		blueboot.setItemMeta(lambboot);

		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta chest = (LeatherArmorMeta) chestplate.getItemMeta();
		chest.setColor(c);
		chestplate.setItemMeta(chest);

		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta lam = (LeatherArmorMeta) leggings.getItemMeta();
		lam.setColor(c);
		leggings.setItemMeta(lam);

		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta helmetm = (LeatherArmorMeta)  helmet.getItemMeta();
		helmetm.setColor(c);
		helmet.setItemMeta(helmetm);

		ItemStack[] returning = {blueboot, chestplate, leggings, helmet};
		return returning;
	}

}
