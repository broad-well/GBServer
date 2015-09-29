package com.Gbserver.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.listener.Announce;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;

public class Home implements CommandExecutor {

	public static Map<String, Object> data = new HashMap();
	
	public static HelpTable ht = new HelpTable("/home (set)", "This is used to provide home location storage.", "", "home");
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("home")) {
			if (Utilities.validateSender(sender)) {
				sync(false);
				Player pl = (Player) sender;
				if (args.length == 1) {
					if(args[0].equalsIgnoreCase("set")){
						// Set home.
						// Determine if it is set.
						if (data.containsKey(pl.getName())) {
							ChatWriter.writeTo(pl, ChatWriterType.HOME,
									"We have detected that you have a previous set home. Removing it.");
							data.remove(pl);
						}
						data.put(pl.getName(), toDouble(pl.getLocation()));
						ChatWriter.writeTo(pl, ChatWriterType.HOME, "Set your home location to your current location.");
						//ChatWriter.writeTo(pl, ChatWriterType.HOME, "x: " + pl.getLocation().getBlockX() + ", y: "
								//+ pl.getLocation().getBlockY() + ", z: " + pl.getLocation().getBlockZ());
						ChatWriter.writeTo(pl, ChatWriterType.HOME, pl.getLocation().toString());
						
						sync(true);
						return true;
					}else{
						ht.show(sender);
						return true;
					}
				}
				if(!data.containsKey(pl.getName())){
					ChatWriter.writeTo(pl, ChatWriterType.HOME, "You did not set your home. To set, use /home set.");
					return true;
				}
				pl.teleport(toLocation((double[]) data.get(pl)));
				ChatWriter.writeTo(pl, ChatWriterType.HOME, "Teleported you to your set home.");
				
				return true;
			} else {
				return true;
			}
		}
		return false;
	}

	public void sync(boolean write) {
		FileConfiguration yc = Announce.getPlugin().getConfig();
		if(write){
			yc.createSection("locations.homes", data);
			return;
		}else{
			if(yc.getConfigurationSection("locations.homes") != null){
				data = yc.getConfigurationSection("locations.homes").getValues(false);
			}
			
		}
	}
	
	public double[] toDouble(Location l){
		double[] obj = new double[3];
		obj[0] = l.getX();
		obj[1] = l.getY();
		obj[2] = l.getZ();
		return obj;
	}
	
	public Location toLocation(double[] l){
		return new Location(Bukkit.getWorld("world"), l[0], l[1], l[2]);
		
	}
}
