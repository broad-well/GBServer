package com.Gbserver.variables;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.commands.BL;

public class GameType {
	
		// CLASSES (TYPES)
		public static List<SwapPacket> packets = new LinkedList<>();
		public class SwapPacket {
			public Player origin;
			public Player target;
			public Color targetColor;
			public LT type;
			
			public SwapPacket(Player o, Color tColor, Player t, LT type){
				origin = o;
				target = t;
				targetColor = tColor;
				this.type = type;
				for(SwapPacket sp : packets){
					if(sp.origin == this.origin || sp.target == this.target){
						//Repeating packets.
						return;
					}
				}
				packets.add(this);
			}
			
			public void close() throws Throwable {
				packets.remove(this);
				this.finalize();
			}
			
			public void targetNotify() {
				target.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, ChatColor.YELLOW + origin.getName() + " " + ChatColor.DARK_GRAY + "would like to swap teams with you."));
				target.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, ChatColor.BOLD + "Type \"/lobby swap accept\" to accept, type \"/lobby swap deny\" to deny."));
			}
		}
		
		
		// STATIC VARIABLES
		//-----------------------------TF
		private static final World tfWorld = Bukkit.getWorld("Turf_Wars1");
		private static final Location tfSpawn = new Location(tfWorld, -989.5, 136.5, 1005.5);
		private static final Location tfBlueJoin = new Location(tfWorld, -991.5, 135.5, 996.5);
		private static final Location tfRedJoin = new Location(tfWorld, -986.5, 135.5, 996.5);
		//-----------------------------BL
		private static final World blWorld = Bukkit.getWorld("Bomb_Lobbers1");
		private static final Location blSpawn = new Location(blWorld, -990.5, 130, 985.5);
		private static final Location blBlueJoin = new Location(blWorld, -992.5, 130.5, 976.5);
		private static final Location blRedJoin = new Location(blWorld, -988.5, 130.5, 976.5);
		public static GameType BL;
		public static GameType TF;
		public static GameType DR;
		public static GameType CTF;
		//-----------------------------DR
		private static final World drWorld = Bukkit.getWorld("Dragons");
		private static final Location drMapBottom = new Location(drWorld, 1013, 144, -1008);
		private static final Location drMapTop = new Location(drWorld, 945, 163, -967);
		private static final Location drSpawn = new Location(drWorld, -985.5, 164, 987.5);
		private static final Location drBlueJoin = new Location(drWorld, -987.5, 164.5, 978.5);
		private static final Location drRedJoin = new Location(drWorld, -983.5, 164.5, 978.5);
		//-----------------------------CTF
		private static final World ctfWorld = Bukkit.getWorld("CTF");
		private static final Location ctfSpawn = new Location(ctfWorld, -990.5, 160, 985.5);
		private static final Location ctfBlueJoin = new Location(ctfWorld, -992.5, 160.5, 976.5);
		private static final Location ctfRedJoin = new Location(ctfWorld, -988.5, 160.5, 976.5);
		//STATIC ENDS
		
		private Runnable onStart;
		private Sheep blues;
		private Sheep reds;
		private LT type;
		private ScoreDisplay sd;
		public List<Player> blue;
		public List<Player> red;
		
		private int countdown;
		private boolean mapAlterable;
		private boolean mapSelected = false;
		private int mapType;
		
		public GameType(Runnable Start, LT type){
			onStart = Start;
			this.type = type;
			blue = new LinkedList<>();
			red = new LinkedList<>();
			switch(type){
			case TF:
				blues = (Sheep) tfWorld.spawnEntity(tfBlueJoin, EntityType.SHEEP);
				blues.setColor(DyeColor.BLUE);
				reds = (Sheep) tfWorld.spawnEntity(tfRedJoin, EntityType.SHEEP);
				reds.setColor(DyeColor.RED);
				
				sd = new ScoreDisplay("Turf Wars");
				sd.setLine(ChatColor.BOLD + "PLAYERS", 1);
				sd.setLine("Joined: 0", 2);
				sd.setLine(ChatColor.BLUE + "Blue: 0", 3);
				sd.setLine(ChatColor.RED + "Red: ", 4);
				mapAlterable = false;
				break;
			case BL:
				blues = (Sheep) blWorld.spawnEntity(blBlueJoin, EntityType.SHEEP);
				blues.setColor(DyeColor.BLUE);
				reds = (Sheep) blWorld.spawnEntity(blRedJoin, EntityType.SHEEP);
				reds.setColor(DyeColor.RED);
				sd = new ScoreDisplay("Bomb Lobbers");
				sd.setLine(ChatColor.BOLD + "PLAYERS", 1);
				sd.setLine("Joined: 0", 2);
				sd.setLine(ChatColor.BLUE + "Blue: 0", 3);
				sd.setLine(ChatColor.RED + "Red: 0", 4);
				sd.setLine("MAP: ", 6);
				sd.display();
				mapAlterable = true;
				break;
			case DR:
				blues = (Sheep) drWorld.spawnEntity(drBlueJoin, EntityType.SHEEP);
				blues.setColor(DyeColor.BLUE);
				reds = (Sheep) drWorld.spawnEntity(drRedJoin, EntityType.SHEEP);
				reds.setColor(DyeColor.RED);
				sd = new ScoreDisplay("Dragons");
				sd.setLine(ChatColor.BOLD + "PLAYERS", 1);
				sd.setLine("Joined: 0", 2);
				sd.setLine(ChatColor.BLUE + "Blue: 0", 3);
				sd.setLine(ChatColor.RED + "Red: 0", 4);
				sd.setLine(ChatColor.YELLOW + "Teaming does not count", 6);
				sd.display();
				mapAlterable = true;
				break;
			case CTF:
				blues = (Sheep) ctfWorld.spawnEntity(ctfBlueJoin, EntityType.SHEEP);
				blues.setColor(DyeColor.BLUE);
				reds = (Sheep) ctfWorld.spawnEntity(ctfRedJoin, EntityType.SHEEP);
				reds.setColor(DyeColor.RED);
				sd = new ScoreDisplay("Capture the flag");
				sd.setLine(ChatColor.BOLD + "PLAYERS", 1);
				sd.setLine("Joined: 0", 2);
				sd.setLine(ChatColor.BLUE + "Blue: 0", 3);
				sd.setLine(ChatColor.RED + "Red: 0", 4);
				sd.display();
				mapAlterable = false;
				break;
			}
			
		}
		
		public List<Player> allPlayers() {
			List<Player> l = new LinkedList<>();
			l.addAll(blue);
			l.addAll(red);
			return l;
		}
		
		public void clear() {
			blue.clear();
			red.clear();
			uScore();
		}
		
		public void close() throws Throwable {
			clear();
			blues.remove();
			reds.remove();
			packets.clear();
			this.finalize();
		}
		
		public void join(Color c, Player subject){
			if(c == Color.BLUE){
				//Join Blue.
				if(blue.contains(subject)){
					subject.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are already in team "+ChatColor.BLUE+"BLUE"));
					uScore();
					return;
				}
				if(red.contains(subject)){
					//Good swap.
					if(blue.isEmpty()){
						red.remove(subject);
						rawJoin(Color.BLUE, subject);
						uScore();
						return;
					}
					int luckyPlayerIndex = Utilities.getRandom(0, blue.size());
					Player luckyPlayer = blue.get(luckyPlayerIndex);
					SwapPacket sp = new SwapPacket(subject, Color.BLUE, luckyPlayer, this.type);
					sp.targetNotify();
					ChatWriter.writeTo(subject, ChatWriterType.GAME, "Sent a swap request to "+ChatColor.YELLOW+luckyPlayer.getName());
					uScore();
					return;
				}
				//Did not join the game.
				//Let this person in the game!
				if(red.size() >= blue.size()){
					blue.add(subject);
					ChatWriter.writeTo(subject, ChatWriterType.GAME, "You have joined "+ChatColor.BLUE+"BLUE");
					
					uScore();
					return;
				}else{
					ChatWriter.writeTo(subject, ChatWriterType.GAME, "Blue is not reasonable for current team situations. Please join red first, then click on me.");
					uScore();
					return;
				}
			}
			if(c == Color.RED){
				//Join Red.
				if(red.contains(subject)){
					subject.sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You are already in team "+ChatColor.RED+"RED"));
					uScore();
					return;
				}
				if(blue.contains(subject)){
					//Good swap.
					if(red.isEmpty()){
						blue.remove(subject);
						rawJoin(Color.RED, subject);
						uScore();
						return;
					}
					int luckyPlayerIndex = Utilities.getRandom(0, red.size());
					Player luckyPlayer = red.get(luckyPlayerIndex);
					SwapPacket sp = new SwapPacket(subject, Color.RED, luckyPlayer, this.type);
					sp.targetNotify();
					uScore();
					return;
				}
				//Did not join the game.
				//Let this person in the game!
				if(blue.size() >= red.size()){
					red.add(subject);
					ChatWriter.writeTo(subject, ChatWriterType.GAME, "You have joined "+ChatColor.RED+"RED");
					uScore();
					return;
				}else{
					ChatWriter.writeTo(subject, ChatWriterType.GAME, "Red is not reasonable for current team situations. Please join blue first, then click on me.");
					uScore();
					return;
				}
			}
			
		}
		
		public void rawJoin(Color c, Player p){
			if(c == Color.BLUE){
				red.remove(p);
				blue.remove(p);
				blue.add(p);
				ChatWriter.writeTo(p, ChatWriterType.GAME, "Added you to "+ChatColor.BLUE+"BLUE");
			}else{
				blue.remove(p);
				red.remove(p);
				red.add(p);
				ChatWriter.writeTo(p, ChatWriterType.GAME, "Added you to "+ChatColor.RED+"RED");
			}
			uScore();
		}
		
		
		public void leave(Player sub){
			red.remove(sub);
			blue.remove(sub);
			ChatWriter.writeTo(sub, ChatWriterType.GAME, "You have left the game.");
			uScore();
		}
		
		private int id = 0;
		public void start(int ct) {
			if(mapAlterable && !mapSelected){
				ChatWriter.write(ChatWriterType.ERROR, "The game does not have a specified map.");
				return;
			}
			countdown = ct;
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

				@Override
				public void run() {
					if(countdown == 0){
						onStart.run();
						clear();
						Bukkit.getScheduler().cancelTask(id);
					}else{
						sd.setLine(ChatColor.BOLD + "STARTING", 6);
						sd.setLine("In "+countdown, 7);
						countdown--;
					}
				}
				
			}, 0L, 20L);
			if(type == LT.BL){
				com.Gbserver.commands.BL.removePreviousMap();
				com.Gbserver.commands.BL.getMap(mapType);
			}
			if(type == LT.DR){
				getDragonsMap();
				//To be implemented with multiple maps.
			}
		}
		
		public Sheep getBlue() {
			return blues;
		}
		
		public Sheep getRed() {
			return reds;
		}
		
		public void setMap(int number){
			if(!mapAlterable){
				return;
			}
			mapType = number;
			mapSelected = true;
			uScore();
		}
		//PRIVATE
		private void uScore() {
			sd.setLine("Joined: "+allPlayers().size(), 2);
			sd.setLine(ChatColor.BLUE + "Blue: "+blue.size(), 3);
			sd.setLine(ChatColor.RED + "Red: "+red.size(), 4);
			if(type == LT.BL && mapSelected){
				sd.setLine("MAP: "+mapType, 6);
			}
		}
		
		private Color getColor(Player p){
			if(blue.contains(p)){
				return Color.BLUE;
			}else if(!allPlayers().contains(p)){
				return null;
			}else{
				return Color.RED;
			}
		}
		//PUBLIC
		public static Color randomColor() {
			if(Utilities.getRandom(0, 2) == 0){
				return Color.BLUE;
			}else{
				return Color.RED;
			}
		}
		
		public static Color opposite(Color c){
			if(c == Color.BLUE){
				return Color.RED;
			}else{
				return Color.BLUE;
			}
		}
		
		public static void getDragonsMap() {
			Utilities.copy(drMapBottom, drMapTop, new Location(drWorld, 0, 150, 0));
		}
}
