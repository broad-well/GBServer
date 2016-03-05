package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.CPrefix;
import com.Gbserver.variables.Sandbox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Hat implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (Utilities.validateSender(sender) && Utilities.validateGamePlay(sender) && Survival.checkSurvival(sender)) {
            Player p = (Player) sender;
            ItemStack is;
            if ((is = p.getItemInHand()).getType().isBlock()) {
                ItemStack[] armor = p.getInventory().getArmorContents();
                armor[3] = is;
                p.getInventory().setArmorContents(armor);
                sender.sendMessage(CPrefix.Prf.CONDITION + "Hat applied. Enjoy your new hat!");
                return true;
            } else {
                sender.sendMessage(CPrefix.Prf.CONDITION + "This item cannot be used for a hat.");
                return true;
            }
        } else {
            return false;
        }
    }
}
