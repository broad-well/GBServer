package com.Gbserver.variables;

import com.Gbserver.Utilities;
import com.Gbserver.listener.Rank;
import com.Gbserver.variables.PermissionManager.Permissions;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
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
    static EnhancedPlayer fromDump(String serializedPlayer, HashMap<String, String> dump){
        EnhancedPlayer build = new EnhancedPlayer(Identity.deserializeIdentity(serializedPlayer));
        if(dump.keySet().contains("Permission")) build.setPermission(Permissions.valueOf(dump.get("Permission")));
        if(dump.keySet().contains("Rank")) build.setRank(Rank.fromConfig(dump.get("Rank")));
        if(dump.keySet().contains("ColorPreference")) build.setColorPref(TeamColor.fromString(dump.get("ColorPreference")));
        if(dump.keySet().contains("Contract"))
            build.setContract(Double.parseDouble(dump.get("Contract")));
        else
            build.setContract(-1);
        if(dump.keySet().contains("Birthday")) build.setBirthday(
                Integer.valueOf(dump.get("Birthday").split(",")[0]),
                Integer.valueOf(dump.get("Birthday").split(",")[1])); //12,26
        if(dump.keySet().contains("IgnoreList")) {
            for (String str : (ArrayList<String>) new Yaml().load(dump.get("IgnoreList"))) {
                if (Preferences.get().get("Debug").equals("true")) {
                    System.out.println("EnhancedPlayerParser: " + str);
                }
                build.addIgnored(Identity.deserializeIdentity(str));
            }
        }
        return build;
    }

    private OfflinePlayer pl;
    private Permissions myPerm; //default: guest
    private Location home; // nullable
    private Rank rank; // nullable
    private TeamColor colorPref; // nullable
    private Integer[] birthday; // nullable
    private List<OfflinePlayer> ignored = new LinkedList<>(); // blank only, not nullable
    private double contract = -1;

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

    public void setContract(double c){
        contract = c;
    }

    public void addIgnored(OfflinePlayer op) { ignored.add(op); }

    public boolean isIgnoring(OfflinePlayer op) { return ignored.contains(op); }

    public Integer[] getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer... i) {birthday = i;}

    public double getContract() {return contract;}
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

    public void setIgnoreList(List<OfflinePlayer> list){
        if(list == null) return;
        ignored = list;
    }

    public List<OfflinePlayer> getIgnoreList() {return ignored;}

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
        if(contract != -1) build.put("Contract", String.valueOf(contract));
        if(birthday != null) build.put("Birthday", String.valueOf(birthday[0]) + "," + String.valueOf(birthday[1]));
        String[] ilbuild = new String[ignored.size()];
        for(OfflinePlayer op : ignored){
            ilbuild[ignored.indexOf(op)] = Identity.serializeIdentity(op);
        }
        build.put("IgnoreList", new Yaml().dump(ilbuild));
        return build;
    }


    public static class ConfigAgent {
        private static Yaml yaml = new Yaml();

        private static void setupDumperOptions(){
            DumperOptions oD = new DumperOptions();
            oD.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            yaml = new Yaml(oD);
        }

        public static final ConfigLoader.ConfigUser configUser = new ConfigLoader.ConfigUser() {


            public void load() {
                try {
                    FileReader fr = new FileReader(ConfigManager.getPathInsidePluginFolder("data.dat").toFile());
                    Object obj = yaml.load(fr);
                    fr.close();
                    if (obj instanceof HashMap) {
                        cache.clear();
                        HashMap<String, HashMap<String, String>> options = (HashMap<String, HashMap<String, String>>) obj;
                        for (Map.Entry<String, HashMap<String, String>> entry : options.entrySet()) {
                            cache.add(EnhancedPlayer.fromDump(entry.getKey(), entry.getValue()));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("EnhancedPlayer: ConfigAgent: Error: Exception during file seek!");
                    e.printStackTrace();
                }
            }

            public void unload() {
                try {
                    setupDumperOptions();
                    HashMap<String, HashMap<String, String>> toDump = new HashMap<>();
                    for (EnhancedPlayer ep : cache) {
                        toDump.put(Identity.serializeIdentity(ep.toPlayer()), ep.toDump());
                    }
                    FileWriter fw = new FileWriter(ConfigManager.getPathInsidePluginFolder("data.dat").toFile());
                    yaml.dump(toDump, fw);
                    fw.flush();
                    fw.close();
                } catch (Exception e) {
                    System.out.println("EnhancedPlayer: ConfigAgent: Error: Exception during file seek!");
                    e.printStackTrace();
                }
            }
        };

    }

}
