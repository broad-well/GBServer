package com.Gbserver.listener;

import com.Gbserver.variables.ConfigManager;
import com.Gbserver.variables.Identity;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatOnlineTime implements Listener{
    private static final File logFile = ConfigManager.getPathInsidePluginFolder("logOnOffs.dat").toFile();
    public static HashMap<UUID, List<LogEntry>> cache = new HashMap<>();
    private static Yaml helper = new Yaml();

    public static class LogEntry {
        public static final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
        public boolean isLogIn;
        public Date timeframe;

        public LogEntry(boolean in){
            isLogIn = in;
            timeframe = new Date();
        }

        public HashMap<String, String> toDump() {
            HashMap<String, String> output = new HashMap<>();
            output.put("LoggingIn?", Boolean.toString(isLogIn));
            output.put("Time", parser.format(timeframe));
            return output;
        }

        public static LogEntry parseDump(HashMap<String, String> dump){
            LogEntry build = new LogEntry(Boolean.parseBoolean(dump.get("LoggingIn?")));
            try {
                build.timeframe = parser.parse(dump.get("Time"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return build;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        try {
            //Append to List in HashMap value column.
            List<LogEntry> previous = cache.get(pje.getPlayer().getUniqueId());
            if(previous == null) {
                cache.put(pje.getPlayer().getUniqueId(), new LinkedList<LogEntry>());
                previous = cache.get(pje.getPlayer().getUniqueId());
            }
            previous.add(new LogEntry(true));
            cache.put(pje.getPlayer().getUniqueId(), previous);
            //Added
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe){
        try {
            //Append to List in HashMap value column.
            List<LogEntry> previous = cache.get(pqe.getPlayer().getUniqueId());
            if(previous == null) {
                cache.put(pqe.getPlayer().getUniqueId(), new LinkedList<LogEntry>());
                previous = cache.get(pqe.getPlayer().getUniqueId());
            }
            previous.add(new LogEntry(false));
            cache.put(pqe.getPlayer().getUniqueId(), previous);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void output() throws IOException {
        HashMap<String, List<HashMap<String, String>>> superObject = new HashMap<>();
        for(Map.Entry<UUID, List<LogEntry>> entry : cache.entrySet()){
            //For each player, build a list of HashMaps which each represent a LogEntry.
            //Build the list!
            List<HashMap<String, String>> logEntries = new LinkedList<>();
            for(LogEntry le : entry.getValue()) logEntries.add(le.toDump());
            superObject.put(Identity.serializeIdentity(Bukkit.getOfflinePlayer(entry.getKey())), logEntries);
        }

        //Write!
        FileWriter fw = new FileWriter(logFile);
        helper.dump(superObject, fw);
        fw.flush();
        fw.close();
    }

    public static void input() throws IOException {
        FileReader fr = new FileReader(logFile);
        Object injection = helper.load(fr);
        fr.close();
        if(injection instanceof HashMap){
            cache.clear();
            HashMap<String, List<HashMap<String, String>>> superObject = (HashMap<String, List<HashMap<String, String>>>) injection;
            for(Map.Entry<String, List<HashMap<String, String>>> entry : superObject.entrySet()){
                List<LogEntry> logEntries = new LinkedList<>();
                for(HashMap<String, String> dump : entry.getValue()) logEntries.add(LogEntry.parseDump(dump));
                cache.put(Identity.deserializeIdentity(entry.getKey()).getUniqueId(), logEntries);
            }
        }
    }
}

