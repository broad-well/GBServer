package com.Gbserver.listener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
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
    public static List<Player> setCancelled = new LinkedList<>();


	public static Map<String, Rank> Rankdata = new HashMap<String, Rank>() {{
            put("_Broadwell", Rank.OWNER);
            put("MarkNutt", Rank.BANANA);
            put("Ehcto", Rank.GHOST);
            put("Anairda", Rank.CAT);
            put("Zenithian4", Rank.POTATO);
            put("SallyGreen", Rank.GATOR);
            put("Elenwen", Rank.BIRD);
            put("Mystal", Rank.DUCK);
            put("AcidWolf", Rank.DOG);
            put("Flystal", Rank.DEEQ);
            put("spacetrain31", Rank.DEV);
    }};
	enum Rank {
		OWNER,BANANA,GHOST,CAT,POTATO,GATOR,BIRD,DUCK,DOG,DEEQ,DEV;
        private static Map<Rank, String> format = new HashMap<Rank, String>() {{
            put(OWNER, ChatColor.RED + "" + ChatColor.BOLD + "Owner ");
            put(DEV, ChatColor.RED + "" + ChatColor.BOLD + "Dev ")
            put(GHOST, ChatColor.GRAY + "" + ChatColor.BOLD + "Ghost ");
            put(BANANA, ChatColor.YELLOW + "" + ChatColor.BOLD + "Banana ");
            put(CAT, ChatColor.BLACK + "" + ChatColor.BOLD + "Cat ");
            put(POTATO, ChatColor.GOLD + "" + ChatColor.BOLD + "Potato ");
            put(GATOR, ChatColor.GREEN + "" + ChatColor.BOLD + "Gator ");
            put(BIRD, ChatColor.BLUE + "" + ChatColor.BOLD + "Bird ");
            put(DUCK, ChatColor.WHITE + "" + ChatColor.BOLD + "Bunny ");
            put(DOG, ChatColor.AQUA + "" + ChatColor.BOLD + "Dog ");
            put(DEEQ, ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "deeq ");
        }};

        public static String getPrefix(Rank r){
            return format.get(r);
        }
	}
	
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
            if(!(setCancelled.contains(pce.getPlayer()))) {
                //pce.setFormat(ChatColor.DARK_GRAY + "%s " + ChatColor.RESET + "%s");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!(IgnoreList.getIgnoreList(p).isIgnored(pce.getPlayer()))) {
                        p.sendMessage(generateTag(pce.getPlayer(), true) + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + pce.getMessage());
                    }
                }
                Bukkit.getConsoleSender().sendMessage(generateTag(pce.getPlayer(), true) + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + pce.getMessage());
            }else{
                setCancelled.remove(pce.getPlayer());
            }
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}
	
	public static String generateTag(Player player, boolean isChat) {
		String format = Rank.getPrefix(Rankdata.get(player.getName()));
		if(format == null) format = "";
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
