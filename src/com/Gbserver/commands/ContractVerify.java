package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 1/20/16.
 */
public class ContractVerify implements CommandExecutor {
    private static List<PuidEntry> entries = new LinkedList<>();

    private static class PuidEntry {
        PUID puid;
        Date timeGen;
        boolean accept;

        public PuidEntry(PUID puid, Date timeOfGeneration, boolean accepted){
            this.puid = puid;
            timeGen = timeOfGeneration;
            accept = accepted;
        }

        public boolean isValid() {
            return (System.currentTimeMillis() - timeGen.getTime()) < (1000 * 60 * 5);
        }

        public boolean isContractAccepted() {return accept;}

        public String getPUID() {return puid.toString();}
    }
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
                download();
                if(checkFormat(args[0])) {
                    for (PuidEntry pe : entries) {
                        if (pe.getPUID().equals(args[0]) && pe.isValid()) {
                            //Selected PuidEntry.
                            sender.sendMessage("You are " + (pe.isContractAccepted() ? "accepting" : "declining") + " the contract.");
                        }
                    }
                }
            }else if(pV == currentVersion){
                //Up to date
                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "You don't need to read the player contract again.");
            }else{
                //Not up to date
                download();
                if(checkFormat(args[0])) {
                    for (PuidEntry pe : entries) {
                        if (pe.getPUID().equals(args[0]) && pe.isValid()) {
                            //Selected PuidEntry.
                            sender.sendMessage("You are " + (pe.isContractAccepted() ? "accepting" : "declining") + " the contract.");
                        }
                    }
                }
            }
        }
        return true;
    }

    private static void displayHelp(Player p){
        p.sendMessage(ChatColor.YELLOW + "/contractverify Usage: /contractverify <code>");
        p.sendMessage(ChatColor.BOLD.toString() + ChatColor.YELLOW + "Obtain code from reading the player contract.");
    }

    private static void download(){
        try {
            currentVersion = Double.parseDouble(Files.readAllLines(Paths.get("/var/www/html/eulaVersion"), Charset.defaultCharset()).get(0));
            entries.clear();
            for (String line : Files.readAllLines(Paths.get("/var/www/html/eulaPuid"), Charset.defaultCharset())) {
                String[] args = line.split(",");
                entries.add(new PuidEntry(new PUID(args[0]), sdf.parse(args[1]), Boolean.valueOf(args[2])));
            }
        }catch(Exception e){e.printStackTrace();}
    }

    private static boolean checkFormat(String arg){
        return arg.length() == 8;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
