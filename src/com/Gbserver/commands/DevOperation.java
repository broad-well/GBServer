package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.listener.ProtectionListener;
import com.Gbserver.listener.Rank;
import com.Gbserver.mail.FileParser;
import com.Gbserver.variables.*;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DevOperation implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(Sandbox.check(commandSender)) return true;
        if(isEligible(commandSender)){
            if(strings.length == 0) {
                commandSender.sendMessage("Available Options: " +
                        "InsertPermission, " +
                        "ListPermission, " +
                        "DeletePermission, " +
                        "InsertRank, " +
                        "ListRank, " +
                        "DeleteRank, " +
                        "GetUUID, " +
                        "GetRecentMessenger, " +
                        "FlushMessages, " +
                        "AllEnhancedPlayersInCache, " +
                        "NewPlayer, " +
                        "DeletePlayer, " +
                        "StreamForceEnd, " +
                        "EventDeop, " +
                        "DuplicatePlayerResolve, " +
                        "SetColor, " +
                        "FlushPlayers, " +
                        "ToggleInSandbox, " +
                        "ListTerritories, " +
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
                case "DeletePermission":
                    if(strings.length == 1) return true;
                    UUID toD = null;
                    for(UUID uid : PermissionManager.perms.keySet()){
                        if(Bukkit.getOfflinePlayer(strings[1]).getUniqueId().equals(uid)){
                            toD = uid;
                            commandSender.sendMessage("Found permission to delete");
                        }
                    }
                    if(toD != null)
                        PermissionManager.perms.remove(toD);
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
                case "AllEnhancedPlayersInCache":
                    for(EnhancedPlayer ep : EnhancedPlayer.cache){
                        commandSender.sendMessage(ep.serialize());
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
                    break;
                case "StreamForceEnd":
                    Twitch.streamers.clear();
                    commandSender.sendMessage("Streams ended.");
                    break;
                case "EventDeop":
                    for(Player p : Bukkit.getOnlinePlayers()){
                        if(!ChatFormatter.staff.contains(p.getName()) && p.isOp()){
                            p.setOp(false);
                            commandSender.sendMessage("Deopped " + p.getName());
                        }
                    }
                    break;
                case "DeletePlayer":
                    if(strings.length == 1)return true;
                    List<EnhancedPlayer> toDelete = new LinkedList<>();
                    for(EnhancedPlayer ep : EnhancedPlayer.cache) {
                        if (ep.toPlayer().getName().equals(strings[1])) {
                            toDelete.add(ep);
                        }
                    }
                    commandSender.sendMessage(toDelete.toString());
                    EnhancedPlayer.cache.removeAll(toDelete);

                    break;
                case "Build":
                   ProtectionListener.isDisabled = !ProtectionListener.isDisabled;
                    break;
                case "ListTerritories":
                    for(Territory t : Territory.activeTerritories)
                        commandSender.sendMessage(t.getName() + " - Owned by: " + ChatColor.YELLOW + Bukkit.getOfflinePlayer(t.getOwner()).getName());
                    break;
                case "TestFeature":
                    //devops TestFeature NewConfigs <args>
                    if(strings.length == 1){
                        commandSender.sendMessage("TestFeature subcommands: NewConfigs, SelectorScript");
                        break;
                    }
                    switch(strings[1]){
                        case "NewConfigs":
                            if(strings.length < 4) return true;
                            EnhancedPlayer ep = null;
                            try {
                                ep = EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(strings[3]));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(ep == null){
                                commandSender
                                        .sendMessage("NULLPOINTER! Consult the console.");
                                break;
                            }
                            switch(strings[2]){
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
                        case "SelectorScript":
                            if(strings.length < 3) return true;
                            if(!strings[2].startsWith("%")){
                                commandSender.sendMessage("Input incompatible with SelectorScript.");
                                return true;
                            }
                            String build = "";
                            for(Player i : SelectorScriptParser.instance.parse(commandSender, strings[2])){
                                build += i.getName() + " ";
                            }
                            commandSender.sendMessage(build);
                            break;
                        default:
                            commandSender.sendMessage("Unknown option.");
                    }

                case "Serialize.Location":
                    if(!(commandSender instanceof Player)) return true;
                    break;
                case "NewPlayer":
                    if(strings.length < 2) return true;
                    EnhancedPlayer ep = new EnhancedPlayer(Bukkit.getOfflinePlayer(strings[1]));
                    if(strings.length > 2){
                        ep.setPermission(Permissions.valueOf(strings[2]));
                    }
                    if(strings.length > 3){
                        ep.setRank(Rank.fromConfig(strings[3]));
                    }
                    for(EnhancedPlayer epl : EnhancedPlayer.cache){
                        if(epl.toPlayer().getName().equals(strings[1])){
                            commandSender.sendMessage("Player already exists");
                            return true;
                        }
                    }
                    EnhancedPlayer.cache.add(ep);
                    break;
                case "DuplicatePlayerResolve":
                    List<String> knownPlayers = new LinkedList<>();
                    List<EnhancedPlayer> toDel = new LinkedList<>();
                    for(EnhancedPlayer enp : EnhancedPlayer.cache){
                        if(knownPlayers.contains(enp.toPlayer().getName())){
                            toDel.add(enp);
                            commandSender.sendMessage("Found duplicate, name " + enp.toPlayer().getName());
                        } else {
                          knownPlayers.add(enp.toPlayer().getName());
                        }

                    }
                    commandSender.sendMessage("Deleting duplicates");
                    EnhancedPlayer.cache.removeAll(toDel);
                    break;
                //Easter egg
                case "EXEC_M3A1T":
                    commandSender.sendMessage(ChatColor.MAGIC.toString() + ChatColor.BLUE + "All bugs achieved by " + ChatColor.YELLOW + "package_java" +
                            ChatColor.BLUE + " with Super Cow Powers." + ChatColor.MAGIC.toString());
                    commandSender.sendMessage(ChatColor.AQUA + "Use caution with this command.");
                    break;
                case "ClassModify":
                    commandSender.sendMessage("INOP. Stop, you geek.");
                    break;
                case "SetColor":
                    //Usage: /devops SetColor <name> <color>, minimum/maximum args length requirement is 3.
                    if(strings.length < 3) return true;
                    try {
                        if(TeamColor.fromString(strings[2]) == null){
                            commandSender.sendMessage("Invalid color.");
                            return true;
                        }
                        EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(strings[1]))
                                .setColorPref(TeamColor.fromString(strings[2]));
                        commandSender.sendMessage("Color preference of " + strings[1] + " has been set to " +
                                TeamColor.fromString(strings[2]).toColor() + TeamColor.fromString(strings[2]).toString());
                    } catch (ParseException e) {
                        commandSender.sendMessage(ChatColor.RED + "CAUGHT ERROR! *** Stack Trace attached");
                        commandSender.sendMessage(Utilities.getStackTrace(e));
                    }
                    break;
                case "FlushPlayers":
                    try {
                        EnhancedPlayer.ConfigAgent.$export$();
                        commandSender.sendMessage("Success");
                    } catch (IOException e) {
                        commandSender.sendMessage(Utilities.getStackTrace(e));
                    }break;
                case "ToggleInSandbox":
                    //usage: /devops ToggleInSandbox <player name>, minimum / maximum length of 2.
                    if(strings.length != 2){
                        commandSender.sendMessage("Invalid args length");
                        return true;
                    }
                    UUID target = Bukkit.getOfflinePlayer(strings[1]).getUniqueId();
                    if(Sandbox.contents.contains(target)){
                        Sandbox.contents.remove(target);
                        commandSender.sendMessage("Player removed");
                    }else{
                        Sandbox.contents.add(target);
                        commandSender.sendMessage("Player added");
                    }
                    break;
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
