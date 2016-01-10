package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.unicorn.TextRender;
import com.Gbserver.unicorn.fonts.StandardFonts;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class Heal implements CommandExecutor {
    public static List<Player> players = new LinkedList<Player>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender) && Utilities.validateGamePlay(sender)) {
            Player p = (Player) sender;
            /*if (args.length > 0 || args[0].equals("draw")){
                TextRender.render(StandardFonts.CUBE_GOTHIC, args[1].toUpperCase(), p.getLocation(), Material.STAINED_CLAY, TextRender.EAST);
                p.sendMessage("rendered message");
                return true;
            }/*/
            players.add((Player) sender);
            sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND,
                    ChatColor.ITALIC + "Please click on a living entity to heal it."));
            return true;

        }
        return false;
    }

}
