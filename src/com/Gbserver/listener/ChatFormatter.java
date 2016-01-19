package com.Gbserver.listener;

import com.Gbserver.Main;
import com.Gbserver.commands.Jail;
import com.Gbserver.commands.Mail;
import com.Gbserver.commands.Team;
import com.Gbserver.commands.Tell;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ChatFormatter implements Listener {
    public static List<Player> setCancelled = new LinkedList<>();
    //For fiestas.
    public static List<String> staff = Arrays.asList("_Broadwell", "Xrandon", "Ehcto", "Anairda", "SallyGreen");
    public static Path rankFile = ConfigManager.getPathInsidePluginFolder("ranks.dat");

    //Mail module counterpart
    public static HashMap<Player, List<String>> activeBuffer = new HashMap<>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent pce) {
        boolean containsit = Mail.mailWriteStatus.containsKey(pce.getPlayer());
        if(!containsit) {
            try {
                //Team chatting first.
                if (pce.getMessage().startsWith("@")) {
                    String message = pce.getMessage().substring(1);
                    Chat c;
                    pce.setCancelled(true);
                    if ((c = getChat(pce.getPlayer())) != null) {
                        for (Player p : c.getPlayersInChat()) {
                            p.sendMessage(ChatColor.BOLD + "TEAM " + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + message);
                        }
                        Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "LIMITED TEAM " + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + message);
                        return;
                    } else {
                        ChatWriter.writeTo(pce.getPlayer(), ChatWriterType.ERROR, "You are not in any team right now.");
                        return;
                    }
                }
                if (pce.getPlayer().getName().equalsIgnoreCase("jrmann100")) {
                    pce.setCancelled(true);
                    String output = ChatColor.BLUE + "j" + ChatColor.GREEN + "r" + ChatColor.RED + "m" + ChatColor.AQUA + "a" + ChatColor.GOLD + "n" + ChatColor.DARK_PURPLE + "n" + ChatColor.YELLOW + "100" + ChatColor.RESET + " " + pce.getMessage();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!(IgnoreList.getIgnoreList(p).isIgnored(pce.getPlayer()))) {
                            p.sendMessage(output);
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage(output);
                    return;
                }
                pce.setCancelled(true);
                if (!(setCancelled.contains(pce.getPlayer()))) {
                    //pce.setFormat(ChatColor.DARK_GRAY + "%s " + ChatColor.RESET + "%s");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!(IgnoreList.getIgnoreList(p).isIgnored(pce.getPlayer()))) {
                            p.sendMessage(generateTag(pce.getPlayer(), true) + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + pce.getMessage());
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage(generateTag(pce.getPlayer(), true) + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + pce.getMessage());
                } else {
                    setCancelled.remove(pce.getPlayer());
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }else{
            if(Mail.mailWriteStatus.get(pce.getPlayer()) == Mail.PENDING_SUBJECT){
                pce.setCancelled(true);
                activeBuffer.remove(pce.getPlayer());
                List<String> newset = new LinkedList<>();
                newset.add(pce.getMessage());
                activeBuffer.put(pce.getPlayer(), newset);
                Mail.mailWriteStatus.put(pce.getPlayer(), Mail.PENDING_MESSAGE);
                ChatWriter.writeTo(pce.getPlayer(), ChatWriterType.POSTMAN, "Successfully set the current message subject. You may now send messages for lines of the mail message. When done sending the message, use " + ChatColor.YELLOW +
                        "/mail send <recipient's name>" + ChatColor.GRAY + ".");
            }else{
                pce.setCancelled(true);
                String message = pce.getMessage() + "\n";
                List<String> strs = activeBuffer.get(pce.getPlayer());
                if(activeBuffer.get(pce.getPlayer()).size() > 1) {
                    strs.set(1, activeBuffer.get(pce.getPlayer()).get(1) + message);
                }else{
                    strs.add(message);
                }
                activeBuffer.put(pce.getPlayer(), strs);
                ChatWriter.writeTo(pce.getPlayer(), ChatWriterType.POSTMAN, "Added that line to your message.");
            }
        }

    }

    public static String generateTag(Player player, boolean isChat) {
        if(staff.contains(player.getName()) && Main.onEvent){
            return ChatColor.DARK_RED + "" + ChatColor.BOLD + "Host ";
        }
        Rank format = EnhancedPlayer.getEnhanced(player).getRank();
        String returning = "";
        if (format == null) return returning;
        return isChat ? format.getPrefix() : format.getPrefix() + ChatColor.RESET + player.getName();
    }

    //Games that support Team chatting:
    /* TF / BL / CTF */
    public static Chat getChat(Player p) {
        GameType[] types = {GameType.TF, GameType.BL, GameType.CTF};

        for (GameType gt : types) {
            if (gt.blue.contains(p)) {
                return new Chat(gt.type, Team.BLUE);
            }
            if (gt.red.contains(p)) {
                return new Chat(gt.type, Team.RED);
            }
        }
        return null;
    }
    public static Rank fromConfig(String text) {return Rank.fromConfig(text);}

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe){
        Tell.last.delete(pqe.getPlayer());
        System.out.println("Deleted chat entries from this person");
    }
}

class Chat {
    public LT type;
    public Team team;

    public Chat(LT t, Team e) {
        type = t;
        team = e;
    }

    public List<Player> getPlayersInChat() {
        GameType gt;
        switch (type) {
            case TF:
                gt = GameType.TF;
                break;
            case BL:
                gt = GameType.BL;
                break;
            case CTF:
                gt = GameType.CTF;
                break;
            default:
                gt = null;
                return null;
        }
        switch (team) {
            case BLUE:
                return gt.blue;
            case RED:
                return gt.red;
            default:
                return null;
        }
    }
}


