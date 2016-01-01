package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class F implements CommandExecutor {
    static final String[] stuffs = {
            "pongityping!",
            "pingitypong!",
            "pang!",
            "pen!",
            "prawns!",
            "pung!",
            "piong!",
            "boing!",
            "poon!",
            "pronk!",
            "peng!",
            "plang!",
            "pong?",
            "pong?",
            "pong?"
    };


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if (label.equalsIgnoreCase("f") || label.equalsIgnoreCase("friend")) {
            sender.sendMessage(ChatWriter.getMessage(ChatWriterType.COMMAND, "This is not Meinplex, my Friend!"));
            return true;
        }else if (label.equalsIgnoreCase("ping")){
            sender.sendMessage(stuffs[Utilities.getRandom(0, stuffs.length)]);
            return true;
        }
        return false;
    }

}
