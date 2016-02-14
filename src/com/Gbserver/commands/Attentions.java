package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Attentions implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if(Sandbox.check(arg0)) return true;
        if (arg0.isOp())
            ChatWriter.write(ChatWriterType.ANNOUNCEMENT, Utilities.concat(arg3));
        else
            ChatWriter.writeTo(arg0, ChatWriterType.CONDITION, "You are not authorized to perform this action.");

        return true;
    }


}
