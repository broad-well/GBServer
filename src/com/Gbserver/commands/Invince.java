package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class Invince implements CommandExecutor {

    public static List<Player> inLine = new LinkedList<Player>();
    public static List<LivingEntity> saved = new LinkedList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("protect")) {
            if (Utilities.validateSender(sender)) {
                Player p = (Player) sender;
                inLine.add(p);
                p.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "Please click on a LivingEntity to make it (un)invincible."));
                return true;
            }
        }
        return false;
    }

}
