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
	
	public final static Object[][] RANKDATA = {
			{ "_Broadwell" , OWNER },
			{ "MarkNutt" , BANANA },
			{ "Ehcto" , GHOST},
			//{ "Deliry" },
			{ "Anairda" , CAT},
			{ "Zenithian4" , POTATO},
			{ "SallyGreen" , GATOR}
	};
	
	
	
	public final static String fOWNER = ChatColor.RED + "" + ChatColor.BOLD + "OWNER ";
	public final static String fGHOST = ChatColor.GRAY + "" + ChatColor.BOLD + "GHOST ";
	public final static String fBANANA = ChatColor.YELLOW + "" + ChatColor.BOLD + "BANANA ";
	public final static String fCAT = ChatColor.BLACK + "" + ChatColor.BOLD + "CAT ";
	public final static String fPOTATO = ChatColor.GOLD + "" + ChatColor.BOLD + "POTATO ";
	public final static String fGATOR = ChatColor.GREEN + "" + ChatColor.BOLD + "GATOR ";
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent pce) {
		String format = ChatColor.DARK_GRAY + "";
		for(int i = 0; i < RANKDATA.length; i++){
			if(pce.getPlayer().getName().equals(RANKDATA[i][0])){
				
				//if(RANKDATA[i][1].equals("OWNER")){
					//pce.setFormat(OWNER + "%s" + ChatColor.RESET + " %s");
				//}
				//if(RANKDATA[i][1].equals("RESIDENT")){
					//pce.setFormat(RESIDENT + "%s" + ChatColor.RESET + " %s");
				//}
				switch((int) RANKDATA[i][1]){
				case OWNER:
					format = format + fOWNER + ChatColor.DARK_GRAY;
					break;
				case BANANA:
					format = format + fBANANA + ChatColor.DARK_GRAY;
					break;
				case CAT:
					format = format + fCAT + ChatColor.DARK_GRAY;
					break;
				case GHOST:
					format = format + fGHOST + ChatColor.DARK_GRAY;
					break;
				case POTATO:
					format = format + fPOTATO + ChatColor.DARK_GRAY;
					break;
				case GATOR:
					format = format + fGATOR + ChatColor.DARK_GRAY;
				}
				if(RANKDATA[i].length > 2){
					switch((int) RANKDATA[i][2]){
					case OWNER:
						format = format + fOWNER + ChatColor.DARK_GRAY;
						break;
					case BANANA:
						format = format + fBANANA + ChatColor.DARK_GRAY;
						break;
					case CAT:
						format = format + fCAT + ChatColor.DARK_GRAY;
						break;
					case GHOST:
						format = format + fGHOST + ChatColor.DARK_GRAY;
						break;
					}
				}
				
				
			}
			
		}
		pce.setFormat(format + "%s " + ChatColor.RESET + "%s");
		//pce.setFormat(ChatColor.DARK_GRAY + "%s " + ChatColor.RESET + "%s");
	}
	
	public static String generateTag(Player player) {
		String format = "";
		for (int i = 0; i < RANKDATA.length; i++) {

			if (player.getName().equals(RANKDATA[i][0])) {

				// if(RANKDATA[i][1].equals("OWNER")){
				// pce.setFormat(OWNER + "%s" + ChatColor.RESET + " %s");
				// }
				// if(RANKDATA[i][1].equals("RESIDENT")){
				// pce.setFormat(RESIDENT + "%s" + ChatColor.RESET + " %s");
				// }
				switch ((int) RANKDATA[i][1]) {
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
				}
				if (RANKDATA[i].length > 2) {
					switch ((int) RANKDATA[i][2]) {
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
					}
				}

			}
			
		}
		return format + ChatColor.RESET + player.getName();
	}
	
}
