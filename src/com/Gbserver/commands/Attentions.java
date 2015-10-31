package com.Gbserver.commands;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Attentions implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        // TODO Auto-generated method stub
        if (arg0.isOp()) {
            ChatWriter.write(ChatWriterType.ANNOUNCEMENT, concat(arg3));
            return true;
        }else{
            ChatWriter.writeTo(arg0, ChatWriterType.CONDITION, "You are not authorized to perform this action.");
            return true;
        }
    }

    private String concat(String[] arg) {
        String output = "";
        for (int i = 0; i < arg.length; i++) {
            String s = arg[i];
            if (i == arg.length - 1) {
                output += s;
            } else {
                output += s + " ";
            }
        }
        return output;
    }


}
