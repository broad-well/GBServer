package com.Gbserver.listener;

import com.Gbserver.Main;
import com.Gbserver.commands.Team;
import com.Gbserver.commands.Tell;
import com.Gbserver.listener.chatmodules.*;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ChatFormatter implements Listener {
    public static List<Player> setCancelled = new LinkedList<>();
    private static final Yaml helper = new Yaml();
    private static final DebugLevel dl = new DebugLevel(3, "Chat Executor");
    //For fiestas.
    public static List<String> staff = Arrays.asList("_Broadwell", "Xrandon", "Ehcto", "Anairda", "SallyGreen");
    public static Path rankFile = ConfigManager.getPathInsidePluginFolder("ranks.dat");
    private static List<ChatModule> modules = new LinkedList<ChatModule>(){{
        add(new ChatMail());
        add(new ChatTeam());
        add(new ChatMute());
        add(new ChatIgnore());
    }};

    //Mail module counterpart
    public static HashMap<Player, List<String>> activeBuffer = new HashMap<>();

    /*
    Chat Packet Documentation

    1. HashMap contents
    msg : <message to send>
    sender : <sender's name>
    enabled : <is enabled, stringy boolean>
    recipients : <yaml arrayList of strings>

    2. Interface
    The map will be assmebled at first, then be going thru all the enabled modules, passing thru manipulation and checks.

    3. MapParser
    The map will be finally ported to the MapParser, which assembles the message with rank to send.
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent pce) {
        pce.setCancelled(true);
        //Initialize hashmap
        HashMap<String, String> chatPacket = new HashMap<>();
        chatPacket.put("msg", pce.getMessage());
        chatPacket.put("sender", pce.getPlayer().getName());
        chatPacket.put("enabled", String.valueOf(!setCancelled.contains(pce.getPlayer())));
        chatPacket.put("recipients", helper.dump(listPlayers()));
        dl.debugWrite("Received for interfacing: chat msg hash " + chatPacket);

        //Go thru the interfaces
        for(ChatModule cm : modules){
            chatPacket = cm.passThru(chatPacket);
            if(chatPacket.get("enabled").equals("false")) break;
        }

        //Parse
        if(chatPacket.get("enabled").equals("true")) {
            dl.debugWrite("Received for writing: chat msg hash " + chatPacket);
            String msg = String.format("%s " + ChatColor.GRAY + "%s" + ChatColor.RESET + " %s",
                    generateTag(pce.getPlayer(), true),
                    chatPacket.get("sender"),
                    chatPacket.get("msg"));
            for (Player recipient : listStrPl((List<String>) helper.load(chatPacket.get("recipients")))) {
                recipient.sendMessage(msg);
            }
        }
        /*boolean containsit = Mail.mailWriteStatus.containsKey(pce.getPlayer());
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
                        Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "TEAM " + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + message);
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
                        if (!EnhancedPlayer.getEnhanced(pce.getPlayer()).isIgnoring(p)) {
                            p.sendMessage(output);
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage(output);
                    return;
                }
                pce.setCancelled(true);
                if (!setCancelled.contains(pce.getPlayer())) {
                    //pce.setFormat(ChatColor.DARK_GRAY + "%s " + ChatColor.RESET + "%s");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!EnhancedPlayer.getEnhanced(p).isIgnoring(pce.getPlayer())) {
                            p.sendMessage(generateTag(pce.getPlayer(), true) + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + pce.getMessage());
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage(generateTag(pce.getPlayer(), true) + ChatColor.GRAY + pce.getPlayer().getName() + " " + ChatColor.RESET + pce.getMessage());
                } else {
                    setCancelled.remove(pce.getPlayer());
                }

            } catch (Exception e) {
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
        }*/

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


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe){
        Tell.last.delete(pqe.getPlayer());
    }

    private static List<String> listPlayers(){
        List<String> build = new LinkedList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            build.add(p.getName());
        }
        return build;
    }

    private static List<Player> listStrPl(List<String> pls){
        List<Player> lp = new LinkedList<>();
        for(String str : pls){
            lp.add(Bukkit.getPlayer(str));
        }
        return lp;
    }

}




