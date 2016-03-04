package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.listener.ProtectionListener;
import com.Gbserver.listener.Rank;
import com.Gbserver.listener.StatOnlineTime;
import com.Gbserver.mail.FileParser;
import com.Gbserver.variables.*;
import com.Gbserver.variables.PermissionManager.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class DevOperation implements CommandExecutor {
    private static final DebugLevel dl = new DebugLevel(2, "DevOps Command");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (Sandbox.check(sender)) return true;
        if (isEligible(sender)) {
            if (args.length == 0) {
                sender.sendMessage("Available Options: " +
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
                        "FlushConfigManager, " +
                        "Reload, " +
                        "InsertHome, " +
                        "RemoveHome, " +
                        "ReloadConfigs, " +
                        "RandomPUIDs, " +
                        "AlterPreferences, " +
                        "ListPreferences, " +
                        "ProtectWorldToggle, " +
                        "Birthday, " +
                        "ToggleNewbie, " +
                        "CommandProfiler, " +
                        "ScheduleExecute, " +
                        "ListProtection, " +
                        "Uptime, " +
                        "Serialize, " +
                        "ConfigManagerOperation, " +
                        "GetName. " +
                        "Case sensitive.");
                return true;
            }

            switch (args[0]) {
                case "InsertPermission":
                    if (args.length < 3) return true;
                    EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(args[1])).setPermission(Permissions.valueOf(args[2]));
                    sender.sendMessage("Permission inserted: " + args[1] + " -> " + Permissions.valueOf(args[2]));
                    return true;
                case "ListPermission":
                    sender.sendMessage("All Permissions: ");
                    for (EnhancedPlayer ep : EnhancedPlayer.cache) {
                        if (ep.getPermission() != null)
                            sender.sendMessage(ep.toPlayer().getName() + " -> " + ep.getPermission());
                    }
                    break;
                case "DeletePermission":
                    if (args.length == 1) return true;
                    for (EnhancedPlayer ep : EnhancedPlayer.cache) {
                        if (ep.toPlayer().getName().equals(args[1])) ep.setPermission(Permissions.GUEST);
                            sender.sendMessage(ep.toPlayer().getName() + " -> " + ep.getRank().getPrefix());
                    }
                    break;
                case "InsertRank":
                    if (args.length < 3) return true;
                    EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(args[1])).setRank(Rank.fromConfig(args[2]));
                    sender.sendMessage("Rank inserted: " + Bukkit.getOfflinePlayer(args[1]).getName()
                            + ", " + EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(args[1])).getRank().getPrefix());
                    break;
                case "ListRank":
                    sender.sendMessage("All Ranks: ");
                    for (EnhancedPlayer ep : EnhancedPlayer.cache) {
                        if (ep.getRank() != null)
                            sender.sendMessage(ep.toPlayer().getName() + " -> " + ep.getRank().getPrefix());
                    }
                    break;
                case "AllEnhancedPlayersInCache":
                    for (EnhancedPlayer ep : EnhancedPlayer.cache) {
                        sender.sendMessage(ep.serialize());
                    }
                    break;
                case "DeleteRank":
                    if (args.length < 2) return true;
                    EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(args[1])).setRank(null);
                    sender.sendMessage("Rank removed from " + Bukkit.getOfflinePlayer(args[1]).getName());

                    break;
                case "GetRecentMessenger":
                    sender.sendMessage("Messaging History: ");
                    for (EnhancedMap.Entry entry : Tell.last) {
                        sender.sendMessage(((CommandSender) entry.getFirst()).getName() + " <-> " +
                                ((CommandSender) entry.getSecond()).getName());
                    }
                    break;
                case "GetUUID":
                    if (args.length < 2) return true;
                    sender.sendMessage(Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString());
                    return true;
                case "GetName":
                    if (args.length < 2) return true;
                    sender.sendMessage(Bukkit.getOfflinePlayer(UUID.fromString(args[1])).getName());
                    return true;
                case "FlushMessages":
                    try {
                        FileParser.configUser.unload();
                    } catch (Exception e) {
                        sender.sendMessage(Utilities.getStackTrace(e));
                    }
                    break;
                case "StreamForceEnd":
                    Twitch.streamers.clear();
                    sender.sendMessage("Streams ended.");
                    break;
                case "EventDeop":
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!ChatFormatter.staff.contains(p.getName()) && p.isOp()) {
                            p.setOp(false);
                            sender.sendMessage("Deopped " + p.getName());
                        }
                    }
                    break;
                case "ListProtection":
                    for(Territory t : Territory.activeTerritories){
                        sender.sendMessage(Protection.verbalize(t));
                    }
                    break;
                case "ScheduleExecute":
                    if(args.length < 3) {
                        sender.sendMessage("ScheduleExecute usage: /devops ScheduleExecute <time in seconds> <command> [arguments]");
                        sender.sendMessage("Example: /devops ScheduleExecute 120 bc 2 minutes have passed!");
                        sender.sendMessage("The command will be executed on your behalf, so make sure you stay online!");
                        return true;
                    }
                    if(!Utilities.isNumber(args[1])){
                        sender.sendMessage("2nd argument is an invalid number, scheduling aborted.");
                    }else{
                        final CommandSender cs = sender;
                        final String cmd = buildCmd(args);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                    Bukkit.dispatchCommand(cs, cmd);
                            }
                        }, 20 * Integer.parseInt(args[1]));
                    }
                    break;
                case "DeletePlayer":
                    if (args.length == 1) return true;
                    List<EnhancedPlayer> toDelete = new LinkedList<>();
                    for (EnhancedPlayer ep : EnhancedPlayer.cache) {
                        if (ep.toPlayer().getName().equals(args[1])) {
                            toDelete.add(ep);
                        }
                    }
                    sender.sendMessage(toDelete.toString());
                    EnhancedPlayer.cache.removeAll(toDelete);

                    break;
                case "ToggleNewbie":
                    if(args.length == 1) return true;
                    String target = Identity.serializeIdentity(Bukkit.getOfflinePlayer(args[1]));
                    if(ConfigManager.smartGet("Newbies").keySet().contains(target)){
                        ConfigManager.entries.get("Newbies").remove(target);
                        sender.sendMessage("Player removed from focus.");
                    }else{
                        ConfigManager.entries.get("Newbies").put(target, "");
                        sender.sendMessage("Player added to focus.");
                    }
                    break;
                case "ToggleCommandEnabled":
                    break;
                case "Build":
                    ProtectionListener.isDisabled = !ProtectionListener.isDisabled;
                    break;
                case "ListTerritories":
                    for (Territory t : Territory.activeTerritories)
                        sender.sendMessage(t.getName() + " - Owned by: " + ChatColor.YELLOW + Bukkit.getOfflinePlayer(t.getOwner()).getName());
                    break;
                case "CommandProfiler":
                    //Usage: /devops CommandProfiler <cmdLabel> <enabled/?> <value>
                    if(args.length < 4) return true;
                    dl.debugWrite("Command profile change: " + "Label: " + args[1] + ", Key: " + args[2] + ", ProposedValue: " + args[3]);
                    if(!CommandProfile.data.containsKey(args[1])) CommandProfile.data.put(args[1], new HashMap<String, String>());
                    CommandProfile.data.get(args[1]).put(args[2], args[3]);
                    sender.sendMessage("Changed");
                    break;
                case "ConfigManagerOperation":
                    /*Usage: /devops ConfigManagerOperation Put <key> <insideKey> <insideValue>
                    /devops ConfigManagerOperation List <key>
                    /devops ConfigManagerOperation Get <key> <insideKey>
                     */
                    if(args.length < 3) break;
                    switch(args[1]){
                        case "Put":
                            if(args.length < 5) return true;
                            dl.debugWrite("ConfigManager insert in progress: Category: " +
                                    args[2] + ", Key: " + args[3] + ", Value: " + args[4] );
                            ConfigManager.smartGet(args[2]); //Make sure it exists.
                            ConfigManager.entries.get(args[2]).put(args[3], args[4]);
                            dl.debugWrite("ConfigManager insert complete");
                            sender.sendMessage("Inserted");
                            break;
                        case "List":
                            //args[2] is category
                            for(Map.Entry<String, String> entry : ConfigManager.smartGet(args[2]).entrySet()){
                                sender.sendMessage(entry.getKey() + " : " + entry.getValue());
                            }
                            break;
                        case "Get":
                            if(args.length < 4) return true;
                            sender.sendMessage(ConfigManager.smartGet(args[2]).get(args[3]));
                            break;
                        case "Remove":
                            if(args.length < 4) return true;
                            dl.debugWrite("ConfigManager removal in progress: Category " + args[2] + ", Key " + args[3]);
                            ConfigManager.smartGet(args[2]).remove(args[3]);
                            dl.debugWrite("ConfigManager removal complete");
                            sender.sendMessage("Removed");
                        default:
                            sender.sendMessage("Bad Option: " + args[1]);
                            break;
                    }
                    break;
                case "ProtectWorldToggle":
                    String perm = ConfigManager.smartGet("WorldProtect").get(Bukkit.getWorld(args[1]).getUID().toString());
                    if(perm == null) perm = "false";
                    perm = Boolean.toString(!Boolean.parseBoolean(perm));
                    //Apply.
                    ConfigManager.entries.get("WorldProtect").put(Bukkit.getWorld(args[1]).getUID().toString(), perm);
                    sender.sendMessage("Now " + (perm.equals("false") ? "not " : "") + "protecting this world.");
                    break;
                case "TestFeature":
                    //devops TestFeature NewConfigs <args>
                    if (args.length == 1) {
                        sender.sendMessage("TestFeature subcommands: NewConfigs, SelectorScript");
                        break;
                    }
                    switch (args[1]) {
                        case "NewConfigs":
                            if (args.length < 4) return true;
                            EnhancedPlayer ep = EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(args[3]));

                            if (ep == null) {
                                sender
                                        .sendMessage("NULLPOINTER! Consult the console.");
                                break;
                            }
                            switch (args[2]) {
                                case "permissions":
                                    sender.sendMessage(ep.getPermission().toString());
                                    break;
                                case "ranks":
                                    sender.sendMessage(ep.getRank().getPrefix());
                                    break;
                                case "homes":
                                    sender.sendMessage(ep.getHome().toString());
                                    break;
                            }
                            break;
                        case "SelectorScript":
                            if (args.length < 3) return true;
                            if (!args[2].startsWith("%")) {
                                sender.sendMessage("Input incompatible with SelectorScript.");
                                return true;
                            }
                            String build = "";
                            for (Player i : SelectorScriptParser.instance.parse(sender, args[2])) {
                                build += i.getName() + " ";
                            }
                            sender.sendMessage(build);
                            break;
                        default:
                            sender.sendMessage("Unknown option.");
                    }

                case "Serialize":
                    sender.sendMessage(Serializer.serialize(Bukkit.getWorld("world").getSpawnLocation()));
                    break;
                case "Uptime":
                    if(args.length != 2) return true;
                    long lastLogin = 0;
                    long sumTime = 0;
                    List<String> responseStack = new LinkedList<>();
                    boolean loggedIn = false;
                    for(StatOnlineTime.LogEntry entry : StatOnlineTime.cache.get(Bukkit.getOfflinePlayer(args[1]).getUniqueId())){
                        if(entry.isLogIn == !loggedIn){ //If IsCompatible
                            if(entry.isLogIn){
                                lastLogin = entry.timeframe.getTime();
                                if(loggedIn){
                                    responseStack.add("Server bad operation. Skipping current LogEntry.");
                                    continue;
                                }
                                loggedIn = true;
                            }else{
                                sumTime += (entry.timeframe.getTime() - lastLogin) / 1000;
                                responseStack.add("Successfully added " + ((entry.timeframe.getTime() - lastLogin) / 1000) + " seconds to the sumTime stack, date " +
                                        StatOnlineTime.LogEntry.parser.format(entry.timeframe));
                                loggedIn = false;
                            }
                        }else{
                            responseStack.add("Unmatched login/logout found. This is likely because of a faulty save. Time " +
                                    StatOnlineTime.LogEntry.parser.format(entry.timeframe));
                        }
                    }
                    if(Preferences.get().get("Debug").equals("true")) {
                        sender.sendMessage(ChatColor.AQUA + "Calculation finished. Debug logs below");
                        sender.sendMessage(responseStack.toArray(new String[1]));
                    }
                    sender.sendMessage(args[1] + " has been online for " + sumTime + " seconds.");
                    break;
                case "Birthday":
                    if(args.length == 1) return true;
                    switch(args[1]){
                        case "List":
                            for(EnhancedPlayer ep : EnhancedPlayer.cache){
                                if(ep.getBirthday() != null){
                                    sender.sendMessage(ep.toPlayer().getName() + " : " + (ep.getBirthday()[0] + 1) + "-" + ep.getBirthday()[1]);
                                }
                            }
                            break;
                        case "Add":
                            //Syntax: /devops Birthday Add <name> <month from 0> <day from 1>
                            if(args.length != 5) return true;
                            EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(args[2])).setBirthday
                                    (Integer.parseInt(args[3])-1, Integer.parseInt(args[4]));
                            sender.sendMessage("Finished");
                            break;
                        case "Remove":
                            if(args.length != 3) return true;
                            EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(args[2])).setBirthday(null);
                            sender.sendMessage("Finished");
                            break;
                        default:
                            sender.sendMessage("What is " + args[1] + "?");
                    }
                    break;
                case "FlushConfigManager":
                    ConfigManager.output();
                    sender.sendMessage("Complete");
                    break;
                case "NewPlayer":
                    if (args.length < 2) return true;
                    EnhancedPlayer ep = new EnhancedPlayer(Bukkit.getOfflinePlayer(args[1]));
                    if (args.length > 2) {
                        ep.setPermission(Permissions.valueOf(args[2]));
                    }
                    if (args.length > 3) {
                        ep.setRank(Rank.fromConfig(args[3]));
                    }
                    for (EnhancedPlayer epl : EnhancedPlayer.cache) {
                        if (epl.toPlayer().getName().equals(args[1])) {
                            sender.sendMessage("Player already exists");
                            return true;
                        }
                    }
                    EnhancedPlayer.cache.add(ep);
                    break;
                case "RandomPUIDs":
                    if(args.length < 2 || !Utilities.isNumber(args[1])) return true;
                    //Usage: /devops RandomPUIDs <word length> [amount], required 2, optional 3
                    int repeat = 1;
                    if(args.length == 3 && Utilities.isNumber(args[2])) repeat = Integer.parseInt(args[2]);
                    for(int i = 0; i < repeat; i++){
                        if(Integer.parseInt(args[1]) % 8 != 0){
                            sender.sendMessage("Word size should be multiple of 8.");
                            return true;
                        }
                        sender.sendMessage(PUID.randomPUID(Integer.parseInt(args[1])).toString());
                    }
                    break;
                case "DuplicatePlayerResolve":
                    List<String> knownPlayers = new LinkedList<>();
                    List<EnhancedPlayer> toDel = new LinkedList<>();
                    for (EnhancedPlayer enp : EnhancedPlayer.cache) {
                        if (knownPlayers.contains(enp.toPlayer().getName())) {
                            toDel.add(enp);
                            sender.sendMessage("Found duplicate, name " + enp.toPlayer().getName());
                        } else {
                            knownPlayers.add(enp.toPlayer().getName());
                        }

                    }
                    sender.sendMessage("Deleting duplicates");
                    EnhancedPlayer.cache.removeAll(toDel);
                    break;
                case "ListPreferences":
                    for(Map.Entry<String, String> entry : Preferences.get().entrySet()){
                        sender.sendMessage(entry.getKey() + " : " + entry.getValue());
                    }
                    break;
                case "AlterPreferences":
                    //Usage: /devops AlterPreferences key value
                    if(args.length < 3) return true;
                    Preferences.get().put(args[1], args[2]);
                    sender.sendMessage("Altered");
                    break;
                //Easter egg
                case "EXEC_M3A1T":
                    sender.sendMessage(ChatColor.MAGIC.toString() + ChatColor.BLUE + "All bugs achieved by " + ChatColor.YELLOW + "package_java" +
                            ChatColor.BLUE + " with Super Cow Powers." + ChatColor.MAGIC.toString());
                    sender.sendMessage(ChatColor.AQUA + "Use caution with this command.");
                    break;
                case "ClassModify":
                    sender.sendMessage("INOP. Stop, you geek.");
                    break;
                case "ReloadConfigs":
                    sender.sendMessage(
                            (ConfigLoader.unload() && ConfigLoader.load()) ?
                            "Reload configs complete" :
                            "Error during reload, see console");
                    break;
                case "SetColor":
                    //Usage: /devops SetColor <name> <color>, minimum/maximum args length requirement is 3.
                    if (args.length < 3) return true;
                    if (TeamColor.fromString(args[2]) == null) {
                        sender.sendMessage("Invalid color.");
                        return true;
                    }
                    EnhancedPlayer.getEnhanced(Bukkit.getOfflinePlayer(args[1]))
                            .setColorPref(TeamColor.fromString(args[2]));
                    sender.sendMessage("Color preference of " + args[1] + " has been set to " +
                            TeamColor.fromString(args[2]).toColor() + TeamColor.fromString(args[2]).toString());

                    break;
                case "InsertHome":
                    if(args.length == 1) return true;
                    Home.inport();
                    //Syntax: /devops InsertHome <player name>
                    //Sets home to current position, needs validateSender.
                    if(Utilities.validateSender(sender)){
                        Home.data.put(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), ((Player) sender).getLocation());
                        sender.sendMessage("Home set.");
                    }
                    Home.export();
                    break;
                case "RemoveHome":
                    if(args.length == 1) return true;
                    Home.inport();
                    Home.data.remove(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                    sender.sendMessage("Home removed.");
                    Home.export();
                    break;
                case "FlushPlayers":
                    try {
                        EnhancedPlayer.ConfigAgent.configUser.unload();
                        sender.sendMessage("Success");
                    } catch (Exception e) {
                        sender.sendMessage(Utilities.getStackTrace(e));
                    }
                    break;
                case "Reload":
                    if(args.length == 1) return true;
                    int cdown;
                    try{
                        cdown = Integer.parseInt(args[1]);
                    }catch(Exception e) {return true;}
                    ChatWriter.write(ChatWriterType.ANNOUNCEMENT, "RELOADING IN " + cdown + " SECONDS");
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(), new Runnable() {
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
                            ChatWriter.write(ChatWriterType.ANNOUNCEMENT, "RELOADING...");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
                        }
                    }, 20 * cdown);
                    break;
                case "ToggleInSandbox":
                    //usage: /devops ToggleInSandbox <player name>, minimum / maximum length of 2.
                    if (args.length != 2) {
                        sender.sendMessage("Invalid args length");
                        return true;
                    }
                    UUID targ = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                    if (Sandbox.contents.contains(targ)) {
                        Sandbox.contents.remove(targ);
                        sender.sendMessage("Player removed");
                    } else {
                        Sandbox.contents.add(targ);
                        sender.sendMessage("Player added");
                    }
                    break;
                default:
                    sender.sendMessage("Bad option.");
                    return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You are not authorized to do this operation. Your permission: " +
                    PermissionManager.getPermission((Player) sender) + ", required above: " +
                    PermissionManager.Permissions.PRIVILEGED);
            return true;
        }
        return true;
    }

    public boolean isEligible(CommandSender sender) {
        return sender instanceof ConsoleCommandSender || sender instanceof Player && EnhancedPlayer.getEnhanced((Player) sender).getPermission().isAbove(Permissions.PRIVILEGED);
    }

    private String buildCmd(String[] fullArgs){
        //cmd starts at /devops ScheduleExecute <seconds> <command>..., index 2
        String output = "";
        for(int i = 2; i < fullArgs.length; i++){
            output += fullArgs[i];
            if(i + 1 != fullArgs.length) output += " ";
        }
        return output;
    }
}
