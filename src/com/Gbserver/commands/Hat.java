package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by michael on 10/10/15.
 */
public class Hat implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Utilities.validateSender(sender)) {
            Player p = (Player) sender;
            ItemStack is;
            if ((is = p.getItemInHand()).getType().isBlock()) {
                ItemStack[] armor = p.getInventory().getArmorContents();
                armor[3] = is;
                p.getInventory().setArmorContents(armor);
                ChatWriter.writeTo(sender, ChatWriterType.CONDITION, "Hat applied. Enjoy your new hat!");
                return true;
            } else {
                ChatWriter.writeTo(sender, ChatWriterType.CONDITION, "This item cannot be used for a hat.");
                return true;
            }
        } else {
            return false;
        }
    }
}
