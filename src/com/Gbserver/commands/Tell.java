package com.Gbserver.commands;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
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

public class Tell implements CommandExecutor {
    String[] global;
    String globalOutput;

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
                    sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully sent message to " + p.getName() + ": " + globalOutput));

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
                }
                tp.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ": " + globalOutput);
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                tp.playNote(tp.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
                player.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "Successfully sent message to " + tp.getName() + ": " + globalOutput));
                return true;
            }


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
