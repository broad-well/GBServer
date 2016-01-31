package com.Gbserver.variables;

import com.Gbserver.commands.Jail;
import com.Gbserver.commands.Mute;
import com.Gbserver.commands.SaveMoment;
import com.Gbserver.commands.Warp;
import com.Gbserver.listener.*;
import com.Gbserver.mail.FileParser;
import org.bukkit.command.Command;

/**
 * Created by michael on 1/16/16.
 */
public class ConfigLoader {
    private static final String PREFIX = "[ConfigLoader] ";
    public static boolean load() {

        try {
            System.out.println(PREFIX + "Loading (importing)...");
            ConfigManager.input(); //Fixed Position
            EnhancedPlayer.ConfigAgent.$import$();
            StatOnlineTime.input();
            Sandbox.io(false);
            BlockData.input();
            Mute.inport();
            Jail.input();
            IgnoreList.input();
            SaveMoment.input();
            BanDB.input();
            Couple.input();
            Serializer.input();
            CommandProfile.input();
            FileParser.getInstance().updateBuffer();
            IPCollector.inTake();
            Territory.Import();
            System.out.println(PREFIX + "Loading finished with no errors.");
            return true;
        }catch(Exception e){
            System.out.println(PREFIX + "Error during load! Stack trace follows.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean unload() {
        try {
            System.out.println(PREFIX + "Unloading (exporting)...");
            EnhancedPlayer.ConfigAgent.$export$();
            StatOnlineTime.output();
            Sandbox.io(true);
            BlockData.output();
            Mute.export();
            Jail.output();
            IgnoreList.output();
            SaveMoment.output();
            BanDB.output();
            Couple.output();
            Serializer.output();
            CommandProfile.output();
            FileParser.getInstance().saveBuffer();
            IPCollector.outFlush();
            Territory.Export();
            ConfigManager.output(); //Fixed Position
            System.out.println(PREFIX + "Unloading finished with no errors.");
            return true;
        }catch(Exception e){
            System.out.println(PREFIX + "Error during unload! Stack trace follows.");
            e.printStackTrace();
            return false;
        }
    }
}
