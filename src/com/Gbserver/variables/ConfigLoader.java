package com.Gbserver.variables;

import com.Gbserver.commands.Jail;
import com.Gbserver.commands.Mute;
import com.Gbserver.commands.SaveMoment;
import com.Gbserver.listener.BlockData;
import com.Gbserver.listener.IPCollector;
import com.Gbserver.listener.StatOnlineTime;
import com.Gbserver.listener.Survey;
import com.Gbserver.mail.FileParser;

import java.util.Arrays;
import java.util.List;

public class ConfigLoader {
    public static List<ConfigUser> userList = Arrays.asList(
            EnhancedPlayer.ConfigAgent.configUser,
            StatOnlineTime.configUser,
            Sandbox.configUser,
            BlockData.configUser,
            Mute.configUser,
            Jail.configUser,
            IgnoreList.configUser,
            SaveMoment.configUser,
            BanDB.configUser,
            Couple.configUser,
            Serializer.configUser,
            CommandProfile.configUser,
            FileParser.configUser,
            IPCollector.configUser,
            Territory.configUser,
            Survey.configUser
    );

    public interface ConfigUser {
        void load() throws Exception;

        void unload() throws Exception;
    }

    private static final DebugLevel dl = new DebugLevel(2, "ConfigLoader");
    public static boolean load() {

        try {
            dl.debugWrite("Loading (importing)...");
            ConfigManager.input(); //Fixed Position
            for (ConfigUser cu : userList) {
                try {
                    cu.load();
                } catch (Exception e) {
                    dl.debugWrite(0, "Error during load of " + cu.toString() + "! Stack trace follows.");
                    e.printStackTrace();
                }
            }
            dl.debugWrite("Loading finished with no errors.");
            return true;
        }catch(Exception e){
            dl.debugWrite(0, "Error! Stack trace follows.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean unload() {
        try {
            dl.debugWrite("Unloading (exporting)...");
            for (ConfigUser cu : userList) {
                try {
                    cu.unload();
                } catch (Exception e) {
                    dl.debugWrite(0, "Error during unload of " + cu.toString() + "! Stack trace follows.");
                    e.printStackTrace();
                }
            }
            ConfigManager.output(); //Fixed Position
            dl.debugWrite("Unloading finished with no errors.");
            return true;
        }catch(Exception e){
            dl.debugWrite(0, "Error! Stack trace follows.");
            e.printStackTrace();
            return false;
        }
    }
}