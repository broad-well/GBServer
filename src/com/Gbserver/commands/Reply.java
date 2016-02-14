package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import com.Gbserver.variables.Sandbox;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by michael on 12/18/15.
 */
public class Reply implements CommandExecutor{
    private HelpTable ht = new HelpTable("/r <message>", "To reply to a recent person who has messaged you.", "r", "reply");
    public boolean onCommand(CommandSender sender, Command cmd, String lab, String[] args){
        if(Sandbox.check(sender)) return true;
        if(args.length == 0){
            ht.show(sender);
            return true;
        }else{
            if(Tell.last.has(sender)){
                CommandSender destination = (CommandSender) Tell.last.get(sender);
                String msg = Utilities.concat(args);
                sender.sendMessage(
                        ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName() + " > " + destination.getName() + ": " + msg);
                Tell.beep(sender);
                destination.sendMessage(
                        ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName() + " > " + destination.getName() + ": " + msg);
                Tell.beep(destination);
            }else{
                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You haven't messaged anyone recently.");
                return true;
            }
        }
        return true;
    }
}
