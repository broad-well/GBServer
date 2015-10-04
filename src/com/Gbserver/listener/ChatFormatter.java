package com.Gbserver.listener;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.Gbserver.commands.Team;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.GameType;
import com.Gbserver.variables.IgnoreList;
import com.Gbserver.variables.LT;

public class ChatFormatter implements Listener{
	public final static int OWNER = 1;
	public final static int BANANA = 2;
	public final static int CAT = 3;
	public final static int GHOST = 4;
	public final static int POTATO = 5;
	public final static int GATOR = 6;
	public final static int BIRD = 7;
	public final static int DUCK = 8;
	public final static int DOG = 9;
	
	public final static Object[][] RANKDATA = {
			{ "_Broadwell" , OWNER },
			{ "MarkNutt" , BANANA },
			{ "Ehcto" , GHOST},
			//{ "Deliry" },
			{ "Anairda" , CAT},
			{ "Zenithian4" , POTATO},
			{ "SallyGreen" , GATOR},
			{ "Elenwen" , BIRD},
			{ "Mystal" , DUCK},
			{ "AcidWolf" , DOG}
	};
	
	
	
	public final static String fOWNER = ChatColor.RED + "" + ChatColor.BOLD + "Owner ";
	public final static String fGHOST = ChatColor.GRAY + "" + ChatColor.BOLD + "Ghost ";
	public final static String fBANANA = ChatColor.YELLOW + "" + ChatColor.BOLD + "Banana ";
	public final static String fCAT = ChatColor.BLACK + "" + ChatColor.BOLD + "Cat ";
	public final static String fPOTATO = ChatColor.GOLD + "" + ChatColor.BOLD + "Potato ";
	public final static String fGATOR = ChatColor.GREEN + "" + ChatColor.BOLD + "Gator ";
	public final static String fBIRD = ChatColor.BLUE + "" + ChatColor.BOLD + "Bird ";
	public final static String fDUCK = ChatColor.WHITE + "" + ChatColor.BOLD + "Bunny ";
	public final static String fDOG = ChatColor.AQUA + "" + ChatColor.BOLD + "Dog ";
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent pce) {
		try {
			//Team chatting first.
			if(pce.getMessage().startsWith("@")){
				String message = pce.getMessage().substring(1);
				Chat c;
				pce.setCancelled(true);
				if((c = getChat(pce.getPlayer())) != null){
					for(Player p : c.getPlayersInChat()){
						p.sendMessage(ChatColor.BOLD + "TEAM " + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + message);
					}
					Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "LIMITED TEAM " + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + message);
					return;
				}else{
					ChatWriter.writeTo(pce.getPlayer(), ChatWriterType.ERROR, "You are not in any team right now.");
					return;
				}
			}
			if(pce.getPlayer().getName().equalsIgnoreCase("jrmann100")){
				pce.setCancelled(true);
				String output = ChatColor.BLUE + "j" + ChatColor.GREEN + "r" + ChatColor.RED + "m" + ChatColor.AQUA + "a" + ChatColor.GOLD + "n" + ChatColor.DARK_PURPLE + "n" + ChatColor.YELLOW + "100" + ChatColor.RESET + " " + pce.getMessage();
				for(Player p : Bukkit.getOnlinePlayers()){
					if(!(IgnoreList.getIgnoreList(p).isIgnored(pce.getPlayer()))){
						p.sendMessage(output);
					}
				}
				Bukkit.getConsoleSender().sendMessage(output);
				return;
			}
			pce.setCancelled(true);
			//pce.setFormat(ChatColor.DARK_GRAY + "%s " + ChatColor.RESET + "%s");
			for(Player p : Bukkit.getOnlinePlayers()){
				if(!(IgnoreList.getIgnoreList(p).isIgnored(pce.getPlayer()))){
					p.sendMessage(generateTag(pce.getPlayer(), true) + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + pce.getMessage());
				}
			}
			Bukkit.getConsoleSender().sendMessage(generateTag(pce.getPlayer(), true) + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + pce.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			pce.getPlayer().sendMessage("An error occured.");
			e.printStackTrace();
		}
		
	}
	
	public static String generateTag(Player player, boolean isChat) {
		String format = "";
		for (int i = 0; i < RANKDATA.length; i++) {
			if (player.getName().equals(RANKDATA[i][0])) {

				// if(RANKDATA[i][1].equals("OWNER")){
				// pce.setFormat(OWNER + "%s" + ChatColor.RESET + " %s");
				// }
				// if(RANKDATA[i][1].equals("RESIDENT")){
				// pce.setFormat(RESIDENT + "%s" + ChatColor.RESET + " %s");
				// }
				switch((int) RANKDATA[i][1]){
				case OWNER:
					format = format + fOWNER;
					break;
				case BANANA:
					format = format + fBANANA;
					break;
				case CAT:
					format = format + fCAT;
					break;
				case GHOST:
					format = format + fGHOST;
					break;
				case POTATO:
					format = format + fPOTATO;
					break;
				case GATOR:
					format = format + fGATOR;
					break;
				case BIRD:
					format = format + fBIRD;
					break;
				case DUCK:
					format = format + fDUCK;
					break;
				case DOG:
					format = format + fDOG;
					break;
				}

			}
			
		}
		if(isChat){
			return format;
		}else{
			return format + ChatColor.RESET + player.getName();
		}
	}
	
	//Games that support Team chatting:
	/* TF / BL / CTF */
	public static Chat getChat(Player p){
		GameType[] types = {GameType.TF, GameType.BL, GameType.CTF};
		
		for(GameType gt : types){
			if(gt.blue.contains(p)){
				return new Chat(gt.type, Team.BLUE);
			}
			if(gt.red.contains(p)){
				return new Chat(gt.type, Team.RED);
			}
		}
		return null;
	}
}

class Chat {
	public LT type;
	public Team team;
	public Chat(LT t, Team e){
		type = t;
		team = e;
	}
	
	public List<Player> getPlayersInChat() {
		GameType gt;
		switch(type){
		case TF:
			gt = GameType.TF;
			break;
		case BL:
			gt = GameType.BL;
			break;
		case CTF:
			gt = GameType.CTF;
			break;
		default:
			gt = null;
			return null;
		}
		switch(team){
		case BLUE:
			return gt.blue;
		case RED:
			return gt.red;
		default:
			return null;
		}
	}
}
