package com.Gbserver.variables;

import com.Gbserver.Utilities;
import com.Gbserver.listener.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import com.Gbserver.variables.PermissionManager.Permissions;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
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
    public static EnhancedPlayer getEnhanced(OfflinePlayer p) {
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
        } catch (NullPointerException ignored){ return true;}

    }
    static EnhancedPlayer fromDump(String serializedPlayer, HashMap<String, String> dump){
        EnhancedPlayer build = new EnhancedPlayer(Identity.deserializeIdentity(serializedPlayer));
        if(dump.keySet().contains("Permission")) build.setPermission(Permissions.valueOf(dump.get("Permission")));
        if(dump.keySet().contains("Rank")) build.setRank(Rank.fromConfig(dump.get("Rank")));
        if(dump.keySet().contains("ColorPreference")) build.setColorPref(TeamColor.fromString(dump.get("ColorPreference")));
        return build;
    }

    private OfflinePlayer pl;
    private Permissions myPerm; //default: guest
    private Location home; // nullable
    private Rank rank; // nullable
    private TeamColor colorPref; // nullable

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

    public void setColorPref(TeamColor tc) {
        this.colorPref = tc;
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

    public TeamColor getColorPref() { return colorPref; }

    public String serialize() {
        String returning = "#" + Identity.serializeIdentity(pl) + "\n";
        if(myPerm != null) returning += "  permission:" + myPerm.toString() + "\n";
        if(rank != null) returning += "  rank:" + rank.configOutput() + "\n";
        if(home != null) returning += "  home:" + Utilities.serializeLocation(home) + "\n";
        if(duration != 0) returning += "  duration:" + duration + "\n";
        if(colorPref != null) returning += "  colorPreference:" + colorPref.toString() + "\n";
        return returning;
    }

    public HashMap<String, String> toDump() {
        HashMap<String, String> build = new HashMap<>();
        if(myPerm != null) build.put("Permission", myPerm.toString());
        if(rank != null) build.put("Rank", rank.configOutput());
        if(colorPref != null) build.put("ColorPreference", colorPref.toString());
        return build;
    }


    public static class ConfigAgent {
        private static Yaml yaml = new Yaml();

        private static void setupDumperOptions(){
            DumperOptions oD = new DumperOptions();
            oD.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            yaml = new Yaml(oD);
        }

        public static void $import$() throws IOException, ParseException {
            FileReader fr = new FileReader(ConfigManager.getPathInsidePluginFolder("data.dat").toFile());
            Object obj = yaml.load(fr);
            fr.close();
            if(obj instanceof HashMap){
                cache.clear();
                HashMap<String, HashMap<String, String>> options = (HashMap<String, HashMap<String, String>>) obj;
                for(Map.Entry<String, HashMap<String, String>> entry : options.entrySet()){
                    cache.add(EnhancedPlayer.fromDump(entry.getKey(), entry.getValue()));
                }
            }
        }

        public static void $export$() throws IOException {
            setupDumperOptions();
            HashMap<String, HashMap<String, String>> toDump = new HashMap<>();
            for(EnhancedPlayer ep : cache){
                toDump.put(Identity.serializeIdentity(ep.toPlayer()), ep.toDump());
            }
            FileWriter fw = new FileWriter(ConfigManager.getPathInsidePluginFolder("data.dat").toFile());
            yaml.dump(toDump, fw);
            fw.flush();
            fw.close();
        }

    }

}
