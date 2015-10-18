package com.Gbserver.commands;

import java.util.Collection;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

// BUGGED!
public class SaveMoment implements CommandExecutor{
	
	//TYPE
	public static Collection<PlayerData> saved = new LinkedList<>();
	public class PlayerData {
		public Player player;
		public Location location;
		public ItemStack[] inventory;
		public ItemStack[] armor;
		public GameMode gamemode;
		public double health;
		
		
		public PlayerData(Player p, Location l, ItemStack[] i, double h, ItemStack[] a, GameMode gm){
			player = p;
			location = l;
			inventory = i;
			health = h;
			armor = a;
			gamemode = gm;
			saved.add(this);
		}
		
		public void close() throws Throwable {
			saved.remove(this);
			location = null;
			inventory = null;
			health = 0;
			finalize();
		}
	}
	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("save")){
			if(args.length < 1){
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
				return false;
			}
			Player p = Bukkit.getServer().getPlayer(args[0]);
			new PlayerData(p, p.getLocation(), p.getInventory().getContents(), p.getHealth(), 
					p.getInventory().getArmorContents(), p.getGameMode());
			sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Successfully saved "+p.getName()+"'s Inventory, Location, and Active Potion Effects."));
			return true;
		}
		if(label.equalsIgnoreCase("rest")){
			if(args.length < 1){
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Invalid Syntax."));
				return false;
			}
			Player p = Bukkit.getServer().getPlayer(args[0]);
				for(PlayerData pd : saved){
					if(pd.player == p){
						//this matches
						p.teleport(pd.location);
						p.getInventory().setContents(pd.inventory);
						p.setHealth(pd.health);
						p.getInventory().setArmorContents(pd.armor);
						p.setGameMode(pd.gamemode);
						sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Successfully restored "+p.getName()+"'s Inventory, Location, and Active Potion Effects."));
						try {
							pd.close();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						saved.remove(pd);
						return true;
					}
				}
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You never saved your conditions."));

			
		}
		return false;
	}
}
