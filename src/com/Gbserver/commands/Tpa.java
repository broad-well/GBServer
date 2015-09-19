package com.Gbserver.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import com.Gbserver.variables.TaskStorage;

public class Tpa implements CommandExecutor {

	public static HelpTable ht = new HelpTable("/tpa <target>", "To request to teleport to a player.", "", "tpa");
	public static List<Player[]> tpaList = new LinkedList<>();
	Player[] pa;
	boolean hasDone = false;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("tpa")) {
			// Request to teleport to another player.
			if (Utilities.validateSender(sender)) {
				
				if (args.length == 0) {
					ht.show(sender);
					return true;
				}
				Player origin = (Player) sender;
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null){
					 sender.sendMessage(ChatWriter.getMessage(ChatWriterType.ERROR, "That player cannot be found."));
					return true;
				}
					
				
				Player[] toAdd = { origin, target };
				tpaList.add(toAdd);
				target.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
						origin.getName() + " would like to teleport to you."));
				target.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
						"Type " + ChatColor.BOLD + "/tpaccept" + ChatColor.RESET + "" + ChatColor.AQUA + " to accept, "
								+ "Type " + ChatColor.BOLD + "/tpdeny" + ChatColor.RESET + "" + ChatColor.AQUA
								+ " to deny. "));
				origin.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Sent a teleport request to "+target.getName()+"."));
				return true;
			} else {
				return true;
			}
		}
		
		if (label.equalsIgnoreCase("tpaccept")){
			if (Utilities.validateSender(sender)){
				//Attempts to accept the request.
				Player s = (Player) sender;
				
				hasDone = false;
				for( Player[] pa : tpaList){
					if(pa[1].equals(s)){
						//Do teleportation.
						pa[0].sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "Teleporting you to "+pa[1].getName()+" in 3 seconds..."));
						pa[1].sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "Teleportation establishing in 3 seconds..."));
						final TaskStorage ts = new TaskStorage(pa);
						final int index = tpaList.indexOf(pa);
						hasDone = true;
						Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

							@Override
							public void run() {
								((Player[]) ts.getStorage())[0].teleport(((Player[]) ts.getStorage())[1]);
								((Player[]) ts.getStorage())[0].sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "Teleportation executed."));
								tpaList.remove(index);
							}
							
						}, 60L);
						break;
					}
				}
				if(hasDone){
					return true;
				}else{
					s.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "You do not have any teleportations requests pending."));
					return true;
				}
			}else{
				return true;
			}
		}
		
		if (label.equalsIgnoreCase("tpdeny")){
			if (Utilities.validateSender(sender)){
				//Attempts to deny the request.
				Player s = (Player) sender;
				
				hasDone = false;
				for(Player[] pa : tpaList){
					if(pa[1].equals(s)){
						//Do teleportation cancel.
						pa[0].sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "The teleportation has been cancelled by your target player."));
						pa[1].sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "The teleportation has been cancelled."));
						hasDone = true;
						tpaList.remove(pa);
						break;
					}
				}
				if(hasDone){
					return true;
				}else{
					s.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "You do not have any teleportations requests pending."));
					return true;
				}
			}else{
				return true;
			}
		}

		return false;
	}
}
