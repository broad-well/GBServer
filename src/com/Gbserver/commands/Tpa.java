package com.Gbserver.commands;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tpa implements CommandExecutor {

    public static HelpTable ht = new HelpTable("/tpa <target>", "To request to teleport to a player.", "", "tpa");
    static HashMap<Player, Player> tpaList = new HashMap<>(); // Key is origin, value is origin's target.
    boolean hasDone = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (label.equalsIgnoreCase("tpa")) {
            // Request to teleport to another player or another player to
            if (Utilities.validateSender(sender) && Utilities.validateGamePlay(sender)) {

                if (args.length == 0) {
                    ht.show(sender);
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    ChatWriter.writeTo(sender, ChatWriterType.ERROR, "This player is not online.");
                    return true;
                }
                tpaList.put((Player) sender, target);
                target.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
                        sender.getName() + " would like to teleport to you."));
                target.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
                        "Type " + ChatColor.BOLD + "/tpaccept" + ChatColor.RESET + "" + ChatColor.AQUA
                                + " to accept, Type " + ChatColor.BOLD + "/tpdeny" + ChatColor.RESET + ""
                                + ChatColor.AQUA + " to deny. "));
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND,
                        "Sent a teleport request to " + target.getName() + "."));

            }

        } else if (label.equalsIgnoreCase("tphere")) {
            if (!Utilities.validateSender(sender)) {
                return true;
            }
            if (args.length == 0) {
                ht.show(sender);
                return true;
            }
            List<Player> manipulations = parseInput(sender, args[0]);
            if (manipulations == null) {
                ChatWriter.writeTo(sender, ChatWriterType.ERROR, "Invalid Selscript statement -or- Unknown player.");
                return true;
            }
            for (Player p : manipulations) {
                tpaList.put(p, (Player) sender);
                p.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
                        sender.getName() + " would like you to teleport to him/her."));
                p.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
                        "Type " + ChatColor.BOLD + "/tpaccept" + ChatColor.RESET + "" + ChatColor.AQUA
                                + " to accept, " + "Type " + ChatColor.BOLD + "/tpdeny" + ChatColor.RESET + ""
                                + ChatColor.AQUA + " to deny. "));
                sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND,
                        "Sent a teleport-here request to " + p.getName() + "."));
            }

        }

        if (label.equalsIgnoreCase("tpaccept") || label.equalsIgnoreCase("tpdeny")) {
            if (Utilities.validateSender(sender)) {
                //Attempts to accept the request.
                Player s = (Player) sender;

                for (final Map.Entry<Player, Player> pa : tpaList.entrySet()) {
                    if (pa.getValue().equals(s)) {
                        if (label.equalsIgnoreCase("tpaccept")) {
                            //Do teleportation.
                            pa.getKey().sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "Teleporting you to " +
                                    pa.getValue().getName() + " in 3 seconds..."));
                            pa.getValue().sendMessage(ChatWriter.getMessage(ChatWriterType.TPA,
                                    "Teleportation establishing in 3 seconds..."));
                            Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

                                @Override
                                public void run() {
                                    pa.getKey().teleport(pa.getValue());
                                    pa.getValue().sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "Teleportation executed."));
                                    tpaList.remove(pa.getKey());
                                }

                            }, 60L);
                        } else if (label.equalsIgnoreCase("tpdeny")) {
                            ChatWriter.writeTo(pa.getKey(), ChatWriterType.TPA, "Your teleportation to " +
                                    ChatColor.YELLOW + pa.getValue().getName() + ChatColor.AQUA + " has been cancelled.");
                            ChatWriter.writeTo(pa.getValue(), ChatWriterType.TPA, "You have denied the teleportation " +
                                    "request.");
                            tpaList.remove(pa.getKey());
                        }
                        return true;
                    }
                }
                s.sendMessage(ChatWriter.getMessage(ChatWriterType.TPA, "You do not have any teleportation requests pending."));

            }
            return true;
        }
        return true;
    }

    static List<Player> parseInput(CommandSender sender, String arg) {
        if (arg.startsWith("%")) {
            return SelectorScriptParser.instance.parse(sender, arg);
        } else {
            Player p = Bukkit.getPlayer(arg);
            return p == null ? null : Collections.singletonList(p);
        }
    }
}
