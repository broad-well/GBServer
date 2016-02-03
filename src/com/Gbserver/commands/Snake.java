package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.nms.SnakeTail;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

/**
 * Created by michael on 2/2/16.
 */
public class Snake implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Utilities.validateSender(sender)){
            SnakeTail.spawn(((Player)sender).getLocation());
            sender.sendMessage("SnakeTail spawned");
        }
        return true;
    }
}
