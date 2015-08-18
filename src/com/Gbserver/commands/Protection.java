package com.Gbserver.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Gbserver.Main;

public class Protection implements CommandExecutor {
	public static Main instance;

	public Protection(Main instance) {
		Protection.instance = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (label.equalsIgnoreCase("protection")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Needs to be a player.");
				return false;
			}
			switch (args[0]) {
			case "add":
				if (args.length != 8) {
					sender.sendMessage("Invalid Syntax. /protection add <x1> <y1> <z1> <x2> <y2> <z2> <name>");
					return false;
				}
				if (((Player) sender).isOp()) {
					int x1 = Integer.parseInt(args[1]);
					int y1 = Integer.parseInt(args[2]);
					int z1 = Integer.parseInt(args[3]);
					int x2 = Integer.parseInt(args[4]);
					int y2 = Integer.parseInt(args[5]);
					int z2 = Integer.parseInt(args[6]);
					String name = args[7];
					int protectionCount = instance.getConfig().getInt("protection.number");
					protectionCount++;
					instance.getConfig().set("protection.number", protectionCount);
					instance.getConfig().set("protection.zones." + name + ".1", x1);
					instance.getConfig().set("protection.zones." + name + ".2", y1);
					instance.getConfig().set("protection.zones." + name + ".3", z1);
					instance.getConfig().set("protection.zones." + name + ".4", x2);
					instance.getConfig().set("protection.zones." + name + ".5", y2);
					instance.getConfig().set("protection.zones." + name + ".6", z2);
					return true;
				} else {
					sender.sendMessage("You are not OP, you cannot do this.");
					return false;
				}
			case "exclude":
				if (args.length != 8) {
					sender.sendMessage("Invalid Syntax. /protection exclude <x1> <y1> <z1> <x2> <y2> <z2> <name>");
					return false;
				}
				if (((Player) sender).isOp()) {
					int coor[] = new int[6];
					for (int i = 0; i < 6; i++) {
						coor[i] = Integer.parseInt(args[i + 1]);
						instance.getConfig().set("protection.zones." + args[7] + "." + (i + 7), coor[i]);
					}
					return true;
				} else {
					sender.sendMessage("You are not OP, you cannot do this.");
				}

			}
		}
		return false;
	}

}
