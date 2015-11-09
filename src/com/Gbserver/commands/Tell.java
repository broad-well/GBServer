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
    String[] global;
    String globalOutput;
    public static EnhancedMap<CommandSender> last = new EnhancedMap<>();
    private HelpTable ht = new HelpTable("/tell <target player> <message>", "/tell is a generic command for whispers.", "msg, t, m", "tell");

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("tell") || label.equalsIgnoreCase("t") || label.equalsIgnoreCase("msg")
                || label.equalsIgnoreCase("m")) {
            if (!(sender instanceof Player)) {
                if (args.length < 2) {
                    ht.show(sender);
                    return true;
                }
                Player p;
                if ((p = Bukkit.getPlayer(args[0])) == null) {
                    return true;
                } else {
                    global = args;
                    globalOutput = "";
                    concatenateMultipleSpaces();
                    p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Console: " + globalOutput);
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                    last.delete(sender); //override.
                    last.put(sender, p); // x to x
                    sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName() + ": " + globalOutput);
                }
                return true;
            }
            Player player = (Player) sender;
            if (args.length < 2) {
                ht.show(sender);
                return true;
            } else {
                global = args;
                globalOutput = "";
                concatenateMultipleSpaces();
                Player tp = null;
                tp = Bukkit.getPlayer(args[0]);
                if (tp == null) {
                    ChatWriter.writeTo(sender, ChatWriterType.ERROR, "That player cannot be found.");
                    return true;
                }
                tp.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ": " + globalOutput);
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                last.delete(sender); //override.
                last.put(sender, tp); // x to x
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ": " + globalOutput);
                return true;
            }


        } else if (label.equalsIgnoreCase("reply") || label.equalsIgnoreCase("r")){
            if(!last.has(sender)){
                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You have not messaged anyone recently.");
                return true;
            }
            String message = Utilities.concat(args);
            if(message.isEmpty()){
                ChatWriter.writeTo(sender, ChatWriterType.CHAT, "Missing message. Use /r <your message>.");
                return true;
            }
            if(!(last.get(sender) instanceof CommandSender)) {
                ChatWriter.writeTo(sender, ChatWriterType.ERROR, "We are sorry, but the server is making no sense right now.");
                return true;
            }
            ((CommandSender) last.get(sender)).sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName() + ": " + message);
            if(last.get(sender) instanceof Player) {
                ((Player) last.get(sender)).playNote(((Player) last.get(sender)).getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                ((Player) last.get(sender)).playNote(((Player) last.get(sender)).getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                ((Player) last.get(sender)).playNote(((Player) last.get(sender)).getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                ((Player) last.get(sender)).playNote(((Player) last.get(sender)).getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                ((Player) last.get(sender)).playNote(((Player) last.get(sender)).getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
            }
            sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName() + ": " + message);
            return true;
        } else {
            return false;
        }
    }

    void concatenateMultipleSpaces() {
        for (int i = 1; i < global.length; i++) {
            //args[1] + " " + args[2] + " " + args[3] + " " etc.
            globalOutput = globalOutput + global[i] + " ";
        }
    }

}
