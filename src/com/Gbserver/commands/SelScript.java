package com.Gbserver.commands;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import com.Gbserver.variables.SelectorScriptParser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by michael on 12/15/15.
 */
public class SelScript implements CommandExecutor {
    public static SelectorScriptParser ssp = SelectorScriptParser.instance;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(Sandbox.check(sender)) return true;
        if(args.length == 0){
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, ChatColor.BOLD + "Help for using SelectorScript");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "SelectorScript is a powerful scripting language for selecting online " +
                    "players in a server. It is similar to built-in player selectors like " + ChatColor.YELLOW + "@a, @e, @p, and @r" +
                    ChatColor.GRAY + ". However, instead of \"" + ChatColor.YELLOW + "@" + ChatColor.GRAY +
                    "\", in SelectorScript you start with \"" + ChatColor.YELLOW + "%" + ChatColor.GRAY + "\", e.g."  + ChatColor.ITALIC
                            + ChatColor.YELLOW + "%all" + ChatColor.GRAY + ".");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "There are several options available." + ChatColor.YELLOW +
                    " \"all\", \"random\", \"me\", and the name of a player. " + ChatColor.GRAY +
                    "All options (except \"all\") supports \"!\" before the option, " +
                    "which means to exclude the option from the selection. Options can be linked together using" +
                    " a comma. E.g. " + ChatColor.YELLOW + "%all,!_Broadwell" + ChatColor.GRAY +
                    " includes all players currently on the server, except " + ChatColor.YELLOW + "_Broadwell" + ChatColor.GRAY + ".");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, ChatColor.BOLD + "Examples of SelectorScript: ");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "%random,random,random,me -> 3 random players chosen from online players and the command caller");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "%all,!me,!random -> all players online, except the command caller and a random player chosen from the online players");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "%_Broadwell,Dinnerbone -> the player named _Broadwell and the player named Dinnerbone (excludes those that are offline)");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Bad example: %!random -> will always return a selection of no players, because there are only exclusive options");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, ChatColor.GOLD + "Time to get exploring! Use " + ChatColor.YELLOW + "/selscript <your script>" + ChatColor.GOLD +
                    " to test your SelectorScript code." + ChatColor.ITALIC + " Have fun!");
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, ChatColor.ITALIC + "Note: SelectorScript is in alpha testing. If there's a bug, use /mail to report to _Broadwell.");
            return true;
        }
        if(!args[0].startsWith("%")){
            ChatWriter.writeTo(sender, ChatWriterType.ERROR, "A SelectorScript statement must start with \"%\". Your input did not start with \"%\".");
            return true;
        }
        ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Selected players based on " + ChatColor.YELLOW + args[0] +
                ChatColor.GRAY + ":");
        for(Player p : ssp.parse(sender, args[0])){
            ChatWriter.writeTo(sender, ChatWriterType.COMMAND, ChatColor.YELLOW + p.getName());
        }
        return true;
    }
}
