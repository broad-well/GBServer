package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;

import java.util.Set;

public class Sit implements CommandExecutor {
    public static Block target;
    public static Bat chair;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender) && Utilities.validateGamePlay(sender)) {
            Player p = (Player) sender;
            if (Chairs.getChair(p) == null) {
                target = p.getTargetBlock((Set<Material>) null, 100);
                Chair c = Chairs.getChair(target.getLocation(), p);
                p.sendMessage("*You sit comfortably*");
            } else {
                Chairs.delChair(Chairs.getChair(p));
                p.sendMessage("*You eject painfully*");
            }
            return true;
        }
        return true;
    }
}
