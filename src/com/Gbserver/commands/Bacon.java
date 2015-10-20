package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.HelpTable;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;
import java.util.List;

public class Bacon implements CommandExecutor {
    public static World world = Bukkit.getWorld("BaconBrawl");
    public static Location join = new Location(world, 709.5, 109.5, -722.5);
    public static Location play = new Location(world, 0, 102, 0);
    public static boolean isRunning = false;
    public static List<BaconPlayer> players = new LinkedList<>();
    public static List<String> log = new LinkedList<>();

    public static Pig entry;
    public static HelpTable ht = new HelpTable("/bacon <stop/start/leave/stats/log>", "This command is used for Bacon Brawl.", "", "bacon");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            ht.show(sender);
            return true;
        }

        switch (args[0]) {
            case "stop":
                stopGame();
                sender.sendMessage("Game stopped");
                return true;
            case "stats":
                //Players, Times hit, isRunning
                if (args.length == 2) {
                    //Specific player.
                    //Show hit times
                    Player target;
                    if ((target = Bukkit.getPlayer(args[1])) == null) {
                        ChatWriter.writeTo(sender, ChatWriterType.ERROR, "That player cannot be found.");
                        return true;
                    }
                    BaconPlayer bp;
                    if ((bp = BaconPlayer.getByHandle(target)) == null) {
                        ChatWriter.writeTo(sender, ChatWriterType.ERROR, "That player is not in this game.");
                        return true;
                    }
                    sender.sendMessage("-----" + target.getName() + " damages-----");
                    for (DamageRecord dr : bp.records) {
                        sender.sendMessage(dr.getChatOutput());
                    }

                } else {
                    List<String> message = new LinkedList<>();
                    message.add(ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "---Bacon Brawl Stats---");
                    message.add(ChatColor.GOLD + "Ongoing game?: " + isRunning);
                    message.add(ChatColor.BLUE + "-Players start-");
                    for (BaconPlayer bp : players) {
                        message.add(ChatColor.DARK_BLUE + "  - " + bp.getHandle().getName());
                    }
                    message.add(ChatColor.BLUE + "-Players end-");
                    message.add(ChatColor.GREEN + "----LOBBY INFO---");
                    message.add(ChatColor.GREEN + "Is lobby pig freezer running?: " + Bukkit.getScheduler().isCurrentlyRunning(frozenTask));
                    message.add(ChatColor.AQUA + "" + ChatColor.ITALIC + "For log, use /bacon log.");
                    message.add(ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "---Bacon Brawl Stats end---");
                    for (String m : message) {
                        sender.sendMessage(m);
                    }
                }
                return true;
            case "log":
                if (args.length == 2) {
                    log.clear();
                    sender.sendMessage("Cleared the log.");
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "--Bacon Logs--");
                    for (String msg : log) {
                        sender.sendMessage(ChatColor.DARK_GRAY + msg);
                    }
                }
                return true;
            case "start":
                for (BaconPlayer bp : players) {
                    disguiseToPig(bp);
                    bp.getHandle().teleport(play);
                    bp.getHandle().getInventory().clear();
                    bp.getHandle().setGameMode(GameMode.SURVIVAL);
                    bp.getHandle().getActivePotionEffects().clear();
                    bp.getHandle().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
                    ItemStack iron = new ItemStack(Material.IRON_AXE);
                    iron.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, -32767);
                    iron.addUnsafeEnchantment(Enchantment.DURABILITY, 32767);
                    bp.getHandle().setItemInHand(iron);
                }
                isRunning = true;
                sender.sendMessage("Game started");
                return true;
            case "leave":
                if (Utilities.validateSender(sender)) {
                    Player p = (Player) sender;
                    if (hasPlayer(p)) {
                        players.remove(BaconPlayer.getByHandle(p));
                        ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Removed you from the Bacon Brawl game.");
                        Bacon.log.add(ChatColor.BOLD + "[LEAVE] " + p.getName());
                    } else {
                        ChatWriter.writeTo(sender, ChatWriterType.ERROR, "You are not in the game.");
                    }
                    return true;
                } else {
                    return false;
                }
            default:
                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Unknown option: " + args[0]);
                ht.show(sender);
                return false;
        }
    }

    public static void disguiseToPig(BaconPlayer p) {
        MobDisguise md = new MobDisguise(EntityType.PIG);
        md.setViewSelfDisguise(false);
        DisguiseAPI.disguiseToAll(p.getHandle(), md);
    }

    public static void undisguise(BaconPlayer p) {
        DisguiseAPI.undisguiseToAll(p.getHandle());
    }

    public static void stopGame() {
        for (BaconPlayer p : players) {
            undisguise(p);
        }
        players.clear();
        log.clear();
        isRunning = false;
    }

    //------------------
    private static int frozenTask;

    public static void getLobby() {
        entry = (Pig) world.spawnEntity(join, EntityType.PIG);
        frozenTask = Utilities.setFrozen(entry);
    }

    public static void unload() {
        entry.remove();
        Bukkit.getScheduler().cancelTask(frozenTask);
    }

    //------------------
    public static void addPlayer(Player p) {
        new BaconPlayer(p);
    }

    public static boolean hasPlayer(Player p) {
        return BaconPlayer.getByHandle(p) != null;
    }

    public static void logDeath(BaconPlayer bp) {
        BaconLogs.log(true, "[DEATH] " + bp.getHandle().getName() + " has been killed.");
    }

}


class BaconLogs {

    public static void log(boolean isImpt, String msg) {
        if (isImpt) Bacon.log.add(ChatColor.BOLD + msg);
        if (!isImpt) Bacon.log.add(msg);
    }

    public static void clearLog() {
        Bacon.log.clear();
    }
}