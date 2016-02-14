package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import com.Gbserver.variables.Sandbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Afk implements CommandExecutor {

    // isAFK Help Topic.

    private HelpTable ht = new HelpTable("/isafk <Player to query>", "isAFK is used to query if a player has flagged AFK.", "", "isafk");

    public static HashMap<Player, Integer> afkList = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender)) {
            if (label.equalsIgnoreCase("afk")) {
                doAFK((Player) sender);
            }else if (label.equalsIgnoreCase("isafk")) {
                if (args.length != 1) {
                    ht.show(sender);
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if(target != null) {
                    ChatWriter.writeTo(sender, ChatWriterType.COMMAND, ChatColor.GRAY + target.getName() +
                            (afkList.keySet().contains(target) ? " is AFK." : " is not AFK."));
                }else{
                    ChatWriter.writeTo(sender, ChatWriterType.ERROR, "I cannot find this player!");
                }

            }
        }
        return true;
    }

    public static void doAFK(final Player target) {
        if (!afkList.keySet().contains(target)) {
            int id = Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(), new Runnable(){
                @Override
                public void run() {
                    try{
                    if(afkList.keySet().contains(target)){
                        afkList.remove(target);
                        target.kickPlayer("You have been AFK removed.");
                    }
                }catch(Exception e){}

                }
            }, 20 * 60 * 20);
            afkList.put(target, id);
            ChatWriter.write(ChatWriterType.SERVER, target.getName() + " is now AFK.");
        } else {
            Bukkit.getScheduler().cancelTask(afkList.get(target));
            afkList.remove(target);
            ChatWriter.write(ChatWriterType.SERVER, target.getName() + " is no longer AFK.");
        }
    }
}
