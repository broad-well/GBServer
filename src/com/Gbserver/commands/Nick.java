package com.Gbserver.commands;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class Nick implements CommandExecutor {

    //public static boolean clickPending = false;
    //public static Entity target = null;
    //public static boolean clicked = false;
    public static List<Player> list = new LinkedList<Player>();
    public static Player sender;
    public static String arg;
    public static boolean isNaming;

    private HelpTable ht = new HelpTable("/nick <name/uname> <to name (not required with uname)>", "/nick is used to create a nickname (nametag) for an entity.", "", "nick");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("nick")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Only players are allowed."));
                return false;
            }
            if (args.length < 1) {
                ht.show(sender);
                return true;
            }
            switch (args[0]) {
                case "name":
                    if (args.length < 2) {
                        ht.show(sender);
                        return true;
                    }
                    Nick.isNaming = true;
                    Nick.arg = args[1];
                    Nick.sender = (Player) sender;
                    sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, ChatColor.ITALIC + "Please right click an entity to be named."));
                    list.add((Player) sender);
                    return true;
                case "uname":
                    Nick.isNaming = false;
                    Nick.arg = "";
                    Nick.sender = (Player) sender;
                    sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, ChatColor.ITALIC + "Please right click an entity to be un-named."));
                    list.add((Player) sender);
                    return true;
                default:
                    ht.show(sender);
                    return true;
            }
        }
        return false;
    }

}
