package com.Gbserver.listener;

import com.Gbserver.Main;
import com.Gbserver.commands.Team;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ChatFormatter implements Listener {
    public static List<Player> setCancelled = new LinkedList<Player>();
    //For fiestas.
    public static List<String> staff = Arrays.asList("_Broadwell", "SallyGreen", "Ehcto");
    public static Path rankFile = ConfigManager.getPathInsidePluginFolder("ranks.dat");

    public static HashMap<UUID, Rank> rankData = new HashMap<>();
    /*public static HashMap<String, Rank> Rankdata = new HashMap<String, Rank>() {{
        put("_Broadwell", Rank.OWNER);
        put("MarkNutt", Rank.BANANA);
        put("Ehcto", Rank.GHOST);
        put("Anairda", Rank.CAT);
        put("Zenithian4", Rank.POTATO);
        put("SallyGreen", Rank.GATOR);
        put("Elenwen", Rank.BIRD);
        put("Mystal", Rank.DUCK);
        put("AcidWolf", Rank.DOG);
        put("Flystal", Rank.DEEQ);
        put("spacetrain31", Rank.DEV);
    }};*/



    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent pce) {
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

    }

    public static String generateTag(Player player, boolean isChat) {
        if(staff.contains(player.getName()) && Main.isHalloween){
            return ChatColor.DARK_RED + "" + ChatColor.BOLD + "Host ";
        }
        Rank format = rankData.get(player.getUniqueId());
        String returning = "";
        if (format == null) return returning;
        if (isChat) {
            return format.getPrefix();
        } else {
            return format.getPrefix() + ChatColor.RESET + player.getName();
        }
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

    public static void $import$() throws IOException {
        List<String> lines = new LinkedList<>();
        Scanner scanner = new Scanner(new FileInputStream(rankFile.toFile()));
        while(scanner.hasNextLine()){
            lines.add(scanner.nextLine());
        }
        scanner.close();
        for(String line : lines){
            String[] data = line.split(" ");
            //uuid^Owner,RED
            rankData.put(UUID.fromString(data[0]),
                    Rank.fromConfig(data[1]));
        }
    }

    public static void $export$() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new FileOutputStream(rankFile.toFile()));
        for(Map.Entry<UUID, Rank> entry : rankData.entrySet()){
            writer.println(entry.getKey().toString() + " " + entry.getValue().configOutput());
        }
        writer.flush();
        writer.close();
    }

    public static Rank fromConfig(String text) {return Rank.fromConfig(text);}
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


