package com.Gbserver.variables;

import com.Gbserver.Utilities;
import com.Gbserver.listener.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import com.Gbserver.variables.PermissionManager.Permissions;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.*;

public class EnhancedPlayer {

    //static
    public static List<EnhancedPlayer> cache = new LinkedList<>();
    public static final Path file = ConfigManager.getPathInsidePluginFolder("datas.dat");
    public static EnhancedPlayer getEnhanced(OfflinePlayer p) throws ParseException {
        for(EnhancedPlayer ep : cache){
            if(ep.toPlayer().getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString()))
                return ep;

        }
        EnhancedPlayer New = new EnhancedPlayer(p);
        System.out.println("Founded new EnhancedPlayer, " + p.getName());
        cache.add(New);
        return New;
    }
    public static boolean isPlayerIneligible(OfflinePlayer op, Permissions minimum){
        try {
            Permissions perm = getEnhanced(op).getPermission();
            return perm == null || perm.getLevel() < minimum.getLevel();
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        } catch (NullPointerException ignored){ return true;}

    }

    private OfflinePlayer pl;
    private Permissions myPerm; //default: guest
    private Location home; // nullable
    private Rank rank; // nullable

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    private long duration = 0;

    public EnhancedPlayer(OfflinePlayer pl) {
        this.pl = pl;
        this.myPerm = Permissions.GUEST;
    }
    public void setPermission(Permissions myPerm) {
        this.myPerm = myPerm;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public OfflinePlayer toPlayer() {
        return pl;
    }

    public Permissions getPermission() {
        return myPerm;
    }

    public Location getHome() {
        return home;
    }

    public Rank getRank() {
        return rank;
    }

    public String serialize() {
        String returning = "#" + Identity.serializeIdentity(pl) + "\n";
        if(myPerm != null) returning += "  permission:" + myPerm.toString() + "\n";
        if(rank != null) returning += "  rank:" + rank.configOutput() + "\n";
        if(home != null) returning += "  home:" + Utilities.serializeLocation(home) + "\n";
        if(duration != 0) returning += "  duration:" + duration + "\n";
        return returning;
    }


    public static class ConfigAgent {


        public static void $import$() throws IOException, ParseException {
            List<String> lines = Files.readAllLines(EnhancedPlayer.file, Charset.defaultCharset());
            cache.clear();
            EnhancedPlayer current = null;
            for(String line : lines){
                if(line.startsWith("#")){
                    if(current != null)
                        cache.add(current);
                    current = new EnhancedPlayer(Identity.deserializeIdentity(line.substring(1)));
                }else if(line.contains(":") && line.startsWith("  ")){
                    if(current == null){
                        throw new ParseException("Unknown declaration out of player indentation on line " +
                                (lines.indexOf(line) + 1), 1);
                    }else{
                        String[] entry = line.trim().split(":");
                        switch(entry[0]){
                            case "permission":
                                current.setPermission(Permissions.valueOf(entry[1]));
                                break;
                            case "rank":
                                current.setRank(Rank.fromConfig(entry[1]));
                                break;
                            case "home":
                                current.setHome
                                        (Utilities.deserializeLocation(entry[1]));
                                break;
                            case "duration":
                                current.setDuration(Long.parseLong(entry[1]));
                                break;
                            default:
                                throw new ParseException
                                        ("Unknown value declaration on line " + (lines.indexOf(line) + 1) + ", header " + entry[0], 1);
                        }
                    }
                }else if(line.startsWith("//") || line.trim().isEmpty()){}else{
                    throw new ParseException("Unknown field on line " + (lines.indexOf(line) + 1) + ", value \"" + line + "\"", 1);

                }
            }
            //Last one!
            if(current!= null) cache.add(current);
        }

        public static void $export$() throws IOException {
            String output = "";
            for(EnhancedPlayer ep : cache){
                output += ep.serialize();
            }
            Files.write(file, output.getBytes(), StandardOpenOption.CREATE);
        }

    }

}
