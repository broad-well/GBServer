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
import java.text.ParseException;
import java.util.*;

public class EnhancedPlayer {

    //static
    public static final Path file = ConfigManager.getPathInsidePluginFolder("datas.dat");
    public static EnhancedPlayer getEnhanced(OfflinePlayer p) throws IOException, ParseException {
        return deserialize(ConfigAgent.linesOf(p));
    }
    private static EnhancedPlayer deserialize(List<String> input) throws ParseException {
        /*
        #UUID
          permission:
          rank:
          home:
         */
        if(!input.get(0).startsWith("#")) throw new ParseException("Not starting with #: " + input.get(0),1);
        EnhancedPlayer output = new EnhancedPlayer
                (Bukkit.getOfflinePlayer(UUID.fromString(input.get(0).replaceAll("#", ""))));
        for(int i = 1; i < input.size(); i++){
            String line = input.get(i);
            if(!line.startsWith("  ")) throw new ParseException("Indent required near line " + line, 1);
            String[] set = line.trim().split(":");
            switch(set[0]){
                case "permission":
                    Permissions pm = Permissions.valueOf(set[1]);
                    if(pm == null) throw new ParseException("Unknown permission value: " + set[1], 1);
                    output.setPermission(pm);
                    break;
                case "rank":
                    output.setRank(Rank.fromConfig(set[1]));
                    break;
                case "home":
                    output.setHome(Utilities.deserializeLocation(set[1]));
                    break;
                default:
                    throw new ParseException("Unknown field: " + set[0], 1);
            }
        }
        return output;
    }
    private OfflinePlayer pl;
    private Permissions myPerm; //default: guest
    private Location home; // nullable
    private Rank rank; // nullable

    private EnhancedPlayer(OfflinePlayer pl) {
        this.pl = pl;
    }
    private void setPermission(Permissions myPerm) {
        this.myPerm = myPerm;
    }

    private void setHome(Location home) {
        this.home = home;
    }

    private void setRank(Rank rank) {
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

    private String serialize() {
        String returning = "#" + pl.getUniqueId().toString() + "\n";
        if(myPerm != null) returning += "  permission:" + myPerm.toString() + "\n";
        if(rank != null) returning += "  rank:" + rank.toString() + "\n";
        if(home != null) returning += "  home:" + Utilities.serializeLocation(home) + "\n";
        return returning;
    }

}
class ConfigAgent {
    public static HashMap<String, String> getEntries(OfflinePlayer p) throws IOException, ParseException {
        //parse process
        List<String> lines = Files.readAllLines(EnhancedPlayer.file, Charset.defaultCharset());
        int pline = lines.indexOf("#" + p.getUniqueId().toString());
        if(pline == -1) return null;
        pline++;
        HashMap<String, String> output = new HashMap<>();
        while(true){
            if(lines.size() <= pline) break; //reached end of file
            String line = lines.get(pline);
            if(line.startsWith("#")) break; //reached end of person
            if(!line.startsWith("  ")) throw new ParseException("Unexpected non-indent near " + line, 1);
            line = line.trim();
            String[] data = line.split(":");
            if(data.length < 2) throw new ParseException("Not enough data on one line near " + line, 1);
            output.put(data[0], data[1]);
            pline++;
        }
        return output.isEmpty() ? null : output;
    }

    public static List<String> linesOf(OfflinePlayer p) throws IOException, ParseException {
        List<String> lines = Files.readAllLines(EnhancedPlayer.file, Charset.defaultCharset());
        List<String> output = new ArrayList<>();
        int pline = lines.indexOf("#" + p.getUniqueId().toString());
        if(pline == -1) return null;
        output.add(lines.get(pline));
        pline++;
        while(true){
            if(lines.size() <= pline) break;
            String line = lines.get(pline);
            if(!line.startsWith("#") &&
                    !line.startsWith("  ") &&
                    !line.startsWith("//") &&
                    !line.trim().isEmpty()) throw new ParseException
                    ("Unknown field at " + line, 1);
            if(line.startsWith("  ")){
                output.add(line);
                pline++;
            }else{
                break;
            }
        }
        return output;
    }
}