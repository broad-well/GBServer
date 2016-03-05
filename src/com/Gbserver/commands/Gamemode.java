package com.Gbserver.commands;


import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.variables.CPrefix;
import com.Gbserver.variables.HelpTable;
import com.Gbserver.variables.Sandbox;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Gamemode implements CommandExecutor {

    private HelpTable ht = new HelpTable("/gm <c/s/p> (c=creative, s=survival, p=spectator)", "/gm is used to change a player's gamemode.", "", "gm");
    private static HashMap<String, GameMode> interpreter = new HashMap<String, GameMode>(){{
        put("c", GameMode.CREATIVE);
        put("1", GameMode.CREATIVE);
        put("creative", GameMode.CREATIVE);
        //---
        put("s", GameMode.SURVIVAL);
        put("0", GameMode.SURVIVAL);
        put("survival", GameMode.SURVIVAL);
        //---
        put("a", GameMode.ADVENTURE);
        put("2", GameMode.ADVENTURE);
        put("adventure", GameMode.ADVENTURE);
        //---
        put("p", GameMode.SPECTATOR);
        put("3", GameMode.SPECTATOR);
        put("spectator", GameMode.SPECTATOR);
    }};

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if(Utilities.validateSender(sender) && Utilities.validateGamePlay(sender) && Survival.checkSurvival(sender)){
            Player p = (Player) sender;
            if (args.length < 1) {
                ht.show(sender);
                return true;
            }
            if (!Main.onEvent || ChatFormatter.staff.contains(p.getName())) {
                p.setGameMode(interpreter.get(args[0]));
            } else {
                sender.sendMessage(CPrefix.Prf.EVENT + "During " + Main.eventName + ", /gm is limited to staff. Sorry!");
            }
        }
        return true;
    }

}
