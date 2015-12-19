package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.EnhancedMap;
import com.Gbserver.variables.HelpTable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

public class Tell implements CommandExecutor {
    public static EnhancedMap<CommandSender> last = new EnhancedMap<>();
    private HelpTable ht = new HelpTable("/tell <target player> <message>", "/tell is a generic command for whispers.", "msg, t, m", "tell");

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 2){
            ht.show(sender);
            return true;
        }
        //start
        String msg = concat(args);
        if(args[0].equalsIgnoreCase("console")){
            //Send to console.
            sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName() + " > CONSOLE: " + msg);
            beep(sender);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName() + " > CONSOLE: " + msg);
            last.put(sender, Bukkit.getConsoleSender());
        }else if(Bukkit.getPlayer(args[0]) == null){
            //NullPointer, no player
            sender.sendMessage(ChatColor.RED + "Problem: Where is " + args[0] + "? I cannot find this player online.");
            return true;
        }else{
            //Normal
            Player target = Bukkit.getPlayer(args[0]);
            sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD +
                    sender.getName() + " > " + target.getName() + ": " + msg);
            beep(sender);
            target.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD +
                    sender.getName() + " > " + target.getName() + ": " + msg);
            beep(target);
            last.put(sender, target);
        }
        return true;
    }

    private String concat(String[] args){
        String output = "";
        for(int i = 1; i < args.length; i++){
            output += args[i];
            if(i != args.length - 1) output += " ";
        }
        return output;
    }

    public static void beep(CommandSender sender){
        if(sender instanceof Player){
            Player s = (Player) sender;
            for(int i = 0; i < 5; i++)
                s.playNote(s.getLocation(), Instrument.PIANO, new Note(1));
        }
    }

}
