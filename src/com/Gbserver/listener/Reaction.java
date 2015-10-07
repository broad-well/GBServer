package com.Gbserver.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

import net.md_5.bungee.api.ChatColor;

public class Reaction implements Listener{
	
	//Subclass "Equation"
	public static List<Equation> equations = new LinkedList<>();
	
	
	//Settings
	private static int firstMax = 5000;
	private static int firstMin = -5000;
	private static int secondMax = 10000;
	private static int secondMin = -10000;
	
	public static void getRepeatingEvent() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			public void run() {
				if (Bukkit.getOnlinePlayers().size() > 0) {
					Equation e = getEquation();
					e.getChatMessage();
					currentEquation = e;
					pending = true;
					Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
						public void run() {
							if (pending) {
								ChatWriter.write(ChatWriterType.CHAT,
										ChatColor.YELLOW + "REACTION: Time's up! Nobody got the answer.");
								ChatWriter.write(ChatWriterType.CHAT,
										ChatColor.YELLOW + "The answer is: "+currentEquation.calculate());
								currentEquation.close();
								currentEquation = null;
								pending = false;
							}
						}
					}, 1200L);
				}else{
					Bukkit.getLogger().log(Level.INFO, "Tried to get reaction, no players");
					Equation e = getEquation();
					Bukkit.getLogger().log(Level.INFO, "" + e.left + " " + e.operator + " " + e.right);
					Bukkit.getLogger().log(Level.INFO, String.valueOf(e.calculate()));
					e.close();
					e = null;
				}
			}
		}, 0L, 18000L);
	}
	
	public static Equation getEquation(){
		//+-x are easy
		//On ÷, need to check if remainder is 0.
		int op=Utilities.getRandom(Equation.PLUS, Equation.POWER);
		int first = Utilities.getRandom(firstMin, firstMax);
		int second = Utilities.getRandom(secondMin, secondMax);
		while(op == Equation.DIVIDE && first%second != 0){
			first = Utilities.getRandom(firstMin, firstMax);
			second = Utilities.getRandom(secondMin, secondMax);
		}
		if(op == Equation.POWER){
			second = Utilities.getRandom(3, 10);
		}
		Equation e = new Equation(first, op, second);
		return e;
		//long r = e.calculate();
		//e.close();
		//return r;
	}
	
	//LISTENER-------------------------------------
	private static Equation currentEquation;
	private static boolean pending = false;
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent apce){
		if(pending){
			long ans = 0;
			try{
				ans = Long.parseLong(apce.getMessage());
			}catch(Exception e){
				return;
			}
			if(ans == currentEquation.calculate()){
				//Got it correct!
				apce.setCancelled(true);
				apce.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.CHAT, "Congratulations! You got it right!"));
				ChatWriter.write(ChatWriterType.CHAT, ChatColor.YELLOW + "REACTION: " + apce.getPlayer().getName() + " got the answer! The answer is " + ans);
				currentEquation.close();
				currentEquation = null;
				pending = false;
				ChatFormatter.setCancelled.add(apce.getPlayer());
				return;
			
			}else{
				apce.setCancelled(true);
				apce.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.CHAT, "I don't think that is the right answer. Try again!"));
				ChatFormatter.setCancelled.add(apce.getPlayer());
				return;
			}
		}
	}
}
