package com.Gbserver.listener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatter implements Listener{
	public final static int OWNER = 1;
	public final static int BANANA = 2;
	public final static int CAT = 3;
	public final static int GHOST = 4;
	public final static int POTATO = 5;
	public final static int GATOR = 6;
	public final static int BIRD = 7;
	public final static int DUCK = 8;
	
	public final static Object[][] RANKDATA = {
			{ "_Broadwell" , OWNER },
			{ "MarkNutt" , BANANA },
			{ "Ehcto" , GHOST},
			//{ "Deliry" },
			{ "Anairda" , CAT},
			{ "Zenithian4" , POTATO},
			{ "SallyGreen" , GATOR},
			{ "Elenwen" , BIRD},
			{ "GlitterZ" , DUCK}
	};
	
	
	
	public final static String fOWNER = ChatColor.RED + "" + ChatColor.BOLD + "OWNER ";
	public final static String fGHOST = ChatColor.GRAY + "" + ChatColor.BOLD + "GHOST ";
	public final static String fBANANA = ChatColor.YELLOW + "" + ChatColor.BOLD + "BANANA ";
	public final static String fCAT = ChatColor.BLACK + "" + ChatColor.BOLD + "CAT ";
	public final static String fPOTATO = ChatColor.GOLD + "" + ChatColor.BOLD + "POTATO ";
	public final static String fGATOR = ChatColor.GREEN + "" + ChatColor.BOLD + "GATOR ";
	public final static String fBIRD = ChatColor.BLUE + "" + ChatColor.BOLD + "BIRD ";
	public final static String fDUCK = ChatColor.WHITE + "" + ChatColor.BOLD + "DUCK ";
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent pce) {
		
		pce.setFormat(generateTag(pce.getPlayer(),true) + ChatColor.GRAY + "%s " + ChatColor.RESET + "%s");
		//pce.setFormat(ChatColor.DARK_GRAY + "%s " + ChatColor.RESET + "%s");
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
				}

			}
			
		}
		if(isChat){
			return format;
		}else{
			return format + ChatColor.RESET + player.getName();
		}
	}
	
}
