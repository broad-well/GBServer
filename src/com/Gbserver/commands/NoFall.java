package com.Gbserver.commands;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NoFall implements CommandExecutor {
    public static boolean noFall = false;

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if(Sandbox.check(arg0)) return true;
        if(Survival.checkSurvival(arg0)) {
            noFall = !noFall;
            arg0.sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You have toggled No falling damage: Now " + noFall));
        }
        return true;
    }

}
