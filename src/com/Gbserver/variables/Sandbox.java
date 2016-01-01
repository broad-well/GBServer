package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Sandbox {
    private static Path file = ConfigManager.getPathInsidePluginFolder("sandbox.txt");
    public static LinkedList<UUID> contents = new LinkedList<>();

    public static boolean check(CommandSender sender) {
        //Prevent errors while casting to player.
        if(sender instanceof Player){
            Player s = (Player) sender;
            if(contents.contains(s.getUniqueId())){
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
            //Build the config string.
            String out = "";
            for(UUID pl : contents){
                out += Identity.serializeIdentity(Bukkit.getOfflinePlayer(pl));
                if(!pl.equals(contents.get(contents.size()-1))) out += "\n";
            }
            Files.write(file, out.getBytes(), StandardOpenOption.CREATE);
        }else{
            contents.clear();
            for(String line : Files.readAllLines(file, Charset.defaultCharset())){
                if(!line.isEmpty()){
                    OfflinePlayer op = Identity.deserializeIdentity(line);
                    if(op != null && !contents.contains(op.getUniqueId())) contents.add(op.getUniqueId());
                }
            }
        }
    }
}
