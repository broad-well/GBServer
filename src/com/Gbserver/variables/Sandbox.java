package com.Gbserver.variables;

import com.Gbserver.commands.Jail;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Sandbox {
    private static Path file = ConfigManager.getPathInsidePluginFolder("sandbox.txt");
    public static LinkedList<UUID> contents = new LinkedList<>();

    public static boolean check(CommandSender sender) {
        //Prevent errors while casting to player.
        if(sender instanceof Player){
            Player s = (Player) sender;
            if(contents.contains(s.getUniqueId()) || Jail.prisoners.contains(s)){
                //In sandbox, notify player.
                ChatWriter.writeTo(s, ChatWriterType.COMMAND, "You are currently in the sandbox, command execution cancelled.");
                return true;
            }else return false;
        }else{
            //Coming from console.
            return false;
        }
    }

    public static void io(boolean output) throws IOException {
        if(output){
            //No need to use the values
            HashMap<String, String> build = new HashMap<>();
            for(UUID uid : contents)
                build.put(Identity.serializeIdentity(Bukkit.getOfflinePlayer(uid)), "");

            ConfigManager.entries.put("Sandbox", build);
        }else{
            contents.clear();
            for(Map.Entry<String, String> entry : ConfigManager.smartGet("Sandbox").entrySet()){
                contents.add(Identity.deserializeIdentity(entry.getKey()).getUniqueId());
            }
        }
    }
}
