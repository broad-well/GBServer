package com.Gbserver.listener;

import com.Gbserver.Main;
import com.Gbserver.commands.Tell;
import com.Gbserver.listener.chatmodules.ChatIgnore;
import com.Gbserver.listener.chatmodules.ChatMail;
import com.Gbserver.listener.chatmodules.ChatMute;
import com.Gbserver.listener.chatmodules.ChatTeam;
import com.Gbserver.variables.DebugLevel;
import com.Gbserver.variables.EnhancedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ChatFormatter implements Listener {
    public static List<Player> setCancelled = new LinkedList<>();
    private static final Yaml helper = new Yaml();
    private static final DebugLevel dl = new DebugLevel(3, "Chat Executor");
    //For fiestas.
    public static List<String> staff = Arrays.asList("_Broadwell", "Xrandon", "Ehcto", "Anairda", "SallyGreen");
    private static List<ChatModule> modules = new LinkedList<ChatModule>(){{
        add(new ChatMail());
        add(new ChatTeam());
        add(new ChatMute());
        add(new ChatIgnore());
        add(new Reaction());
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
            String msg = String.format("%s" + ChatColor.GRAY + "%s" + ChatColor.RESET + " %s",
                    generateTag(pce.getPlayer(), true),
                    chatPacket.get("sender"),
                    chatPacket.get("msg"));
            for (Player recipient : listStrPl((List<String>) helper.load(chatPacket.get("recipients")))) {
                recipient.sendMessage(msg);
            }
            Bukkit.getConsoleSender().sendMessage(msg);
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




