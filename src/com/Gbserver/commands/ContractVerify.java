package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.EnhancedPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by michael on 1/20/16.
 */
public class ContractVerify implements CommandExecutor {
    public static double currentVersion;
    /*
    Online file format:
    [puid]3nWoq5W4,yyyy-MM-dd hh:mm:ss
    Time is for time during PUID generation.
     */

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(Utilities.validateSender(sender)){
            //Check if read the contract yet.
            Player p = (Player) sender;
            double pV = EnhancedPlayer.getEnhanced(p).getContract();
            if(args.length < 1) {
                displayHelp(p);
                return true;
            }
            if(pV == -1){
                //Never read

            }else if(pV == currentVersion){
                //Up to date
                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You don't need to read the player contract again.");
            }else{
                //Not up to date

            }
        }
        return true;
    }

    private static void displayHelp(Player p){
        p.sendMessage(ChatColor.YELLOW + "/contractverify Usage: /contractverify <code>");
        p.sendMessage(ChatColor.BOLD.toString() + ChatColor.YELLOW + "Obtain code from reading the player contract.");
    }
}
