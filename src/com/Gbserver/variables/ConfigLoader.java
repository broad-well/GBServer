package com.Gbserver.variables;

import com.Gbserver.commands.Jail;
import com.Gbserver.commands.Mute;
import com.Gbserver.commands.Warp;
import com.Gbserver.listener.BlockData;
import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.listener.IPCollector;
import com.Gbserver.listener.StatOnlineTime;
import com.Gbserver.mail.FileParser;

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
            Couple.input();
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
            Couple.output();
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
