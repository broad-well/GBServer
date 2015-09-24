package com.Gbserver;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Utilities {
	public static boolean validateSender(CommandSender cs){
		if(!(cs instanceof Player)){
			cs.sendMessage(ChatWriter.getMessage(ChatWriterType.CHAT, "Only players are allowed."));
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean isInRangeOf(Location hi, Location lo, Location test){
		int x1 = hi.getBlockX();
		int y1 = hi.getBlockY();
		int z1 = hi.getBlockZ();
		int x2 = lo.getBlockX();
		int y2 = lo.getBlockY();
		int z2 = lo.getBlockZ();
		boolean is[] = new boolean[3];
		return isInRange(x1, x2, test.getBlockX()) && isInRange(y1, y2, test.getBlockY()) && isInRange(z1, z2, test.getBlockZ());
		
	}
	public static boolean isInRange(int x, int y, int test){
		if(x > y){
			return test<x && test>y;
		}
		if(y > x){
			return test<y && test>x;
		}
		if(y == x){
			return false;
		}
		return false;
	}
	
	public static String concat(String[] arg) {
		String output = "";
		for (int i = 0; i < arg.length; i++) {
			String s = arg[i];
			if (i == arg.length-1) {
				output += s;
			} else {
				output += s + " ";
			}
		}
		return output;
	}
	
	public static int getRandom(int min, int max) {
		Random rand = new Random();

		return rand.nextInt(max) + min;
		// 50 is the maximum and the 1 is our minimum
	}
	
	public static void copy(Location originBottom, Location originTop, Location targetBottom){
		//Say originBottom is 10, 50, 40;
		//    originTop is -20, 70, 70.
		int xrelation = originBottom.getBlockX()-targetBottom.getBlockX();
		int zrelation = originBottom.getBlockZ()-targetBottom.getBlockZ();
		for(int x = originBottom.getBlockX(); x - originTop.getBlockX() != 0; 
				x += (originTop.getBlockX() - originBottom.getBlockX())/Math.abs(originTop.getBlockX() - originBottom.getBlockX())){
			for(int y = originBottom.getBlockY(); y < originTop.getBlockY(); y++){
				for(int z = originBottom.getBlockZ(); z - originTop.getBlockZ() != 0;
						z += (originTop.getBlockZ() - originBottom.getBlockZ())/Math.abs(originTop.getBlockZ() - originBottom.getBlockZ())){
					
					Block origin = originBottom.getWorld().getBlockAt(x,y,z);
					int x1 = x-xrelation;
					int z1 = z-zrelation;
					Block target = originBottom.getWorld().getBlockAt(x1, y, z1);
					target.setType(origin.getType());
					target.setData(origin.getData());
				}
			}
		}
	}
	
	public static void giveLeap(Player target) {
		ItemStack axe = new ItemStack(Material.IRON_AXE);
		ItemMeta im = axe.getItemMeta();
		im.setDisplayName("Leap Axe");
		axe.setItemMeta(im);
		target.getInventory().addItem(axe);
	}
}
