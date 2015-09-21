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
	public static List<TpaPacket> tpaList = new LinkedList<>();
	Player[] pa;
	boolean hasDone = false;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("tpa") || label.equalsIgnoreCase("tphere")) {
			// Request to teleport to another player or another player to 
			if (Utilities.validateSender(sender)) {
				
				if (args.length == 0) {
					ht.show(sender);
					return true;
				}
				Player origin;
				Player target;
				if (label.equalsIgnoreCase("tpa")) {
					origin = (Player) sender;
					target = Bukkit.getPlayer(args[0]);
					if (target == null) {
						sender.sendMessage(ChatWriter.getMessage(ChatWriterType.ERROR, "That player cannot be found."));
						return true;
					} 
				}else{
					target = (Player) sender;
					origin = Bukkit.getPlayer(args[0]);
					if (origin == null) {
						sender.sendMessage(ChatWriter.getMessage(ChatWriterType.ERROR, "That player cannot be found."));
						return true;
					}
				}
				TpaPacket tp = new TpaPacket(origin, target, label.equalsIgnoreCase("tpa") );
				tpaList.add(tp);
				if (label.equalsIgnoreCase("tpa")) {
					target.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
							origin.getName() + " would like to teleport to you."));
					target.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
							"Type " + ChatColor.BOLD + "/tpaccept" + ChatColor.RESET + "" + ChatColor.AQUA
									+ " to accept, " + "Type " + ChatColor.BOLD + "/tpdeny" + ChatColor.RESET + ""
									+ ChatColor.AQUA + " to deny. "));
					origin.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND,
							"Sent a teleport request to " + target.getName() + "."));
				}else{
					origin.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
							target.getName() + " would like you to teleport to him/her."));
					origin.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
							"Type " + ChatColor.BOLD + "/tpaccept" + ChatColor.RESET + "" + ChatColor.AQUA
									+ " to accept, " + "Type " + ChatColor.BOLD + "/tpdeny" + ChatColor.RESET + ""
									+ ChatColor.AQUA + " to deny. "));
					target.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND,
							"Sent a teleport-here request to " + origin.getName() + "."));
				}
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
				for( TpaPacket pa : tpaList){
					if((pa.getTarget().equals(s) && pa.isTPA()) || (pa.getOrigin().equals(s) && !(pa.isTPA()))){
						//Do teleportation.
						pa.getOrigin().sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "Teleporting you to "+pa.getTarget().getName()+" in 3 seconds..."));
						pa.getTarget().sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "Teleportation establishing in 3 seconds..."));
						final TaskStorage ts = new TaskStorage(pa);
						final int index = tpaList.indexOf(pa);
						hasDone = true;
						Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

							@Override
							public void run() {
								((TpaPacket) ts.getStorage()).getOrigin().teleport(((TpaPacket) ts.getStorage()).getTarget());
								((TpaPacket) ts.getStorage()).getTarget().sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "Teleportation executed."));
								tpaList.remove(index);
							}
							
						}, 60L);
						break;
					}
				}
				if(hasDone){
					return true;
				}else{
					s.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "You do not have any teleportation requests pending."));
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
				for(TpaPacket pa : tpaList){
					if((pa.getTarget().equals(s) && pa.isTPA()) || (pa.getOrigin().equals(s) && !(pa.isTPA()))){
						//Do teleportation cancel.
						pa.getOrigin().sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "The teleportation has been cancelled by your target player."));
						pa.getTarget().sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "The teleportation has been cancelled."));
						hasDone = true;
						tpaList.remove(pa);
						break;
					}
				}
				if(hasDone){
					return true;
				}else{
					s.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "You do not have any teleportation requests pending."));
					return true;
				}
			}else{
				return true;
			}
		}

		return false;
	}
}
