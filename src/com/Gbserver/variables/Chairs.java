package com.Gbserver.variables;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;

public class Chairs {
	public static List<Chair> chairs = new LinkedList<Chair>();
	public static List<Item> bats = new LinkedList<Item>();
	public static Chair getChair(Location l, Player p){
		for(Chair c : chairs){
			if(c.getLocation().equals(l) && c.getPlayer().equals(p)){
				return c;
			}
		}
		//Does not exist.
		return new Chair(l, p);
	}
	
	public static Chair getChair(Location l){
		for(Chair c : chairs){
			if(c.getLocation().equals(l)){
				return c;
			}
		}
		return null;
	}
	
	public static Chair getChair(Player p){
		for(Chair c : chairs){
			if(c.getPlayer().equals(p)){
				return c;
			}
		}
		return null;
	}
	
	public static Chair getChair(Bat b){
		for(Chair c : chairs){
			if(c.getBat().equals(b)){
				return c;
			}
		}
		return null;
	}
	
	public static void delChair(Chair c){
		c.getBat().eject();
		chairs.remove(c);
		bats.remove(c.getBat());
		c.getBat().remove();
		c = null;
	}
}
