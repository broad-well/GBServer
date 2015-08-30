package com.Gbserver.commands;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;

import com.Gbserver.variables.Chair;
import com.Gbserver.variables.Chairs;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

public class Sit implements CommandExecutor {
	public static Block target;
	public static Bat chair;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("sit")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
				return false;
			}
			Player p = (Player) sender;
			if (Chairs.getChair(p) == null) {
				target = p.getTargetBlock((Set<Material>) null, 100);
				Chair c = Chairs.getChair(target.getLocation(), p);
				p.sendMessage("*You sit comfortably*");
			} else {
				Chairs.delChair(Chairs.getChair(p));
				p.sendMessage("*You eject painfully*");
			}

		}
		return false;
	}
}
