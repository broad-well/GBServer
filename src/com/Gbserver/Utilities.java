package com.Gbserver;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
}
