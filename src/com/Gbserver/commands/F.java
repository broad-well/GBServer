package com.Gbserver.commands;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class F implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("f") || label.equalsIgnoreCase("friend")) {
            sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "This is not Meinplex, my Friend!"));
            return true;
        }
        return false;
    }

}
