package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.listener.Rank;
import com.Gbserver.mail.FileParser;
import com.Gbserver.variables.EnhancedMap;
import com.Gbserver.variables.EnhancedPlayer;
import com.Gbserver.variables.PermissionManager;
import com.Gbserver.variables.PermissionManager.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

public class DevOperation implements CommandExecutor{


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(isEligible(commandSender)){
            if(strings.length == 0) {
                commandSender.sendMessage("Available Options: " +
                        "InsertPermission, " +
                        "ListPermission, " +
                        "InsertRank, " +
                        "ListRank, " +
                        "DeleteRank, " +
                        "GetUUID, " +
                        "GetRecentMessenger, " +
                        "FlushMessages, " +
                        "GetName. " +
                        "Case sensitive.");
                return true;
            }

            switch(strings[0]){
                case "InsertPermission":
                    if(strings.length < 3) {

                        return true;
                    }
                    Permissions perm = Permissions.valueOf(strings[2]);
                    if(perm == null) return true;
                    OfflinePlayer play = Bukkit.getOfflinePlayer(strings[1]);
                    if(play == null) return true;
                    PermissionManager.perms.put(play.getUniqueId(), perm);
                    commandSender.sendMessage("Permission inserted: " + play.getName() + " -> " + perm);
                    return true;
                case "ListPermission":
                    commandSender.sendMessage("All Permissions: ");
                    for(Map.Entry<UUID, Permissions> permission : PermissionManager.perms.entrySet()){
                        commandSender.sendMessage(
                                Bukkit.getOfflinePlayer(permission.getKey()).getName() + " -> " + permission.getValue().name()
                        );
                    }
                    break;
                case "InsertRank":
                    if(strings.length < 3) return true;
                    ChatFormatter.rankData.put(Bukkit.getOfflinePlayer(strings[1]).getUniqueId(), ChatFormatter.fromConfig(strings[2]));
                    try {
                        ChatFormatter.$export$();
                        commandSender.sendMessage("Rank inserted: " + Bukkit.getOfflinePlayer(strings[1]).getName()
                                + ", " + ChatFormatter.rankData.get(Bukkit.getOfflinePlayer(strings[1]).getUniqueId()).getPrefix());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "ListRank":
                    commandSender.sendMessage("All Ranks: ");
                    for(Map.Entry<UUID, Rank> permission : ChatFormatter.rankData.entrySet()){
                        try {
                            commandSender.sendMessage(
                                    Bukkit.getOfflinePlayer(permission.getKey()).getName() + " -> " + permission.getValue().getPrefix()
                            );
                        }catch(Exception ignored){}
                    }
                    break;
                case "DeleteRank":
                    if(strings.length < 2) return true;
                    ChatFormatter.rankData.remove(Bukkit.getOfflinePlayer(strings[1]).getUniqueId());
                    try {
                        ChatFormatter.$export$();
                        commandSender.sendMessage("Rank removed from " + Bukkit.getOfflinePlayer(strings[1]).getName());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "GetRecentMessenger":
                    commandSender.sendMessage("Messaging History: ");
                    for(EnhancedMap.Entry entry : Tell.last){
                        commandSender.sendMessage(((CommandSender) entry.getFirst()).getName() + " <-> " +
                                ((CommandSender) entry.getSecond()).getName());
                    }
                    break;
                case "GetUUID":
                    if(strings.length < 2) return true;
                    commandSender.sendMessage(Bukkit.getOfflinePlayer(strings[1]).getUniqueId().toString());
                    return true;
                case "GetName":
                    if(strings.length < 2) return true;
                    commandSender.sendMessage(Bukkit.getOfflinePlayer(UUID.fromString(strings[1])).getName());
                    return true;
                case "FlushMessages":
                    try {
                        FileParser.getInstance().saveBuffer();
                    } catch (IOException e) {
                        commandSender.sendMessage(Utilities.getStackTrace(e));
                    }
                case "TestFeature":
                    //devops TestFeature NewConfigs <args>
                    if(strings.length == 1){
                        commandSender.sendMessage("TestFeature subcommands: NewConfigs");
                        break;
                    }
                    switch(strings[1]){
                        case "NewConfigs":
                            if(strings.length < 4) return true;
                            EnhancedPlayer ep = null;
                            try {
                                ep = EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(strings[2]));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(ep == null){
                                commandSender
                                        .sendMessage("NULLPOINTER! Consult the console.");
                                break;
                            }
                            switch(strings[1]){
                                case "permissions":
                                    commandSender.sendMessage(ep.getPermission().toString());
                                    break;
                                case "ranks":
                                    commandSender.sendMessage(ep.getRank().getPrefix());
                                    break;
                                case "homes":
                                    commandSender.sendMessage(ep.getHome().toString());
                                    break;
                            }
                            break;
                        default:
                            commandSender.sendMessage("Unknown option.");
                    }

                case "Serialize.Location":
                    if(!(commandSender instanceof Player)) return true;

                default:
                    commandSender.sendMessage("Bad option.");
                    return true;
            }
        }else{
            commandSender.sendMessage(ChatColor.RED + "You are not authorized to do this operation. Your permission: " +
                    PermissionManager.getPermission((Player) commandSender) + ", required above: "+
                    PermissionManager.Permissions.PRIVILEGED);
            return true;
        }
        return true;
    }

    public boolean isEligible(CommandSender sender) {
        return sender instanceof ConsoleCommandSender || sender instanceof Player && PermissionManager.getPermission((Player) sender).isAbove(Permissions.PRIVILEGED);
    }
}
