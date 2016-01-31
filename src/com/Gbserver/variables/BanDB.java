package com.Gbserver.variables;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 1/23/16.
 */
public class BanDB implements Listener{
    private static File file = ConfigManager.getPathInsidePluginFolder("ban.yml").toFile();

    /*
    Properties format:
    Ident     : <uuid>
    BanModule : <string>
    BanReason : <string>
    Timestamp : <simpledateformat>
     */
    public static HashMap<OfflinePlayer, HashMap<String, String>> data = new HashMap<>();

    public static boolean isBanned(OfflinePlayer op){
        return data.keySet().contains(op);
    }

    public static void ban(OfflinePlayer op, HashMap<String, String> properties){
        data.put(op, properties);
        if(op.isOnline()){
            op.getPlayer().kickPlayer(buildBanReason(properties));
        }
    }

    public static String buildBanReason(HashMap<String, String> properties){
        return ChatColor.BOLD +
                "You have been banned from this server.\n" + ChatColor.RESET +
                ChatColor.AQUA + "Module " + ChatColor.RESET + ": " + ChatColor.DARK_AQUA + properties.get("BanModule") + "\n" +
                ChatColor.AQUA + "Reason " + ChatColor.RESET + ": " + ChatColor.DARK_AQUA + properties.get("BanReason") + "\n" +
                ChatColor.AQUA + "Time   " + ChatColor.RESET + ": " + ChatColor.DARK_AQUA + properties.get("Timestamp");
    }


    public static void output() throws IOException {
        HashMap<String, HashMap<String, String>> mapOutput = new HashMap<>();
        for(Map.Entry<OfflinePlayer, HashMap<String, String>> entry : data.entrySet())
            mapOutput.put(Identity.serializeIdentity(entry.getKey()), entry.getValue());
        FileWriter fw = new FileWriter(file);
        SwiftDumpOptions.BLOCK_STYLE().dump(mapOutput, fw);
        fw.flush();
        fw.close();
    }

    public static void input() throws IOException {
        FileReader fr = new FileReader(file);
        Object obj = SwiftDumpOptions.BLOCK_STYLE().load(fr);
        fr.close();
        if(obj instanceof HashMap){
            data.clear();
            for(Map.Entry<String, HashMap<String, String>> entry : ((HashMap<String, HashMap<String, String>>) obj).entrySet()){
                data.put(Identity.deserializeIdentity(entry.getKey()), entry.getValue());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        if(isBanned(pje.getPlayer())){
            pje.setJoinMessage("");
            pje.getPlayer().kickPlayer(buildBanReason(data.get(pje.getPlayer())));
        }
    }

}
