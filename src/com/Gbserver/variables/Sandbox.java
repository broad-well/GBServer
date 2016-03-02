package com.Gbserver.variables;

import com.Gbserver.commands.Jail;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

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

    public static final ConfigLoader.ConfigUser configUser = new ConfigLoader.ConfigUser() {
        public void load() {
            contents.clear();
            for(Map.Entry<String, String> entry : ConfigManager.smartGet("Sandbox").entrySet()){
                contents.add(Identity.deserializeIdentity(entry.getKey()).getUniqueId());
            }
        }

        public void unload() {
            HashMap<String, String> build = new HashMap<>();
            for (UUID uid : contents)
                build.put(Identity.serializeIdentity(Bukkit.getOfflinePlayer(uid)), "");

            ConfigManager.entries.put("Sandbox", build);
        }
    };

}
