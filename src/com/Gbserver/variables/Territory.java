package com.Gbserver.variables;

import com.Gbserver.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Territory extends CubicSelection {
    public static final Path file = ConfigManager.getPathInsidePluginFolder("territories.txt");
    public static final List<Territory> activeTerritories = new LinkedList<>();

    private UUID owner;
    private List<UUID> collabs;
    private String name;

    public Territory(Location high, Location low, UUID owner, String name) {
        super(high, low);
        this.owner = owner;
        collabs = new LinkedList<>();
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getCollaborators() {
        return collabs;
    }

    public String getName() {
        return name;
    }

    public boolean setName(UUID sender, String name) {
        if (sender == owner) {
            this.name = name;
            return true;
        } else return false;
    }

    public int scheduleTemporaryAllowance(final UUID visitor, long durationTicks) {
        if (collabs.contains(visitor)) return -1;
        collabs.add(visitor);
        return Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(), new Runnable() {
            public void run() {
                collabs.remove(visitor);
                if (Bukkit.getPlayer(visitor) != null)
                    Bukkit.getPlayer(visitor).sendMessage
                            (ChatColor.DARK_AQUA + "Your temporary access to " + Utilities.wrapIn
                                    (ChatColor.YELLOW, Bukkit.getOfflinePlayer(owner).getName(), ChatColor.DARK_AQUA) + "'s home has expired.");

            }
        }, durationTicks);
    }

    public boolean addCollaborator(UUID col) {
        return !collabs.contains(col) && collabs.add(col);
    }

    public boolean removeCollaborator(UUID col) {
        return collabs.contains(col) && collabs.remove(col);
    }

    public boolean hasCollaborator(UUID col) {
        return collabs.contains(col);
    }

    //Volatilizing utilities
    public static final ConfigLoader.ConfigUser configUser = new ConfigLoader.ConfigUser() {

        public void unload() throws IOException {
            String buildall = "";
            for (Territory t : activeTerritories) {
                String build = "Territory{" + Utilities.serializeLocation(t.high) + ";" +
                        Utilities.serializeLocation(t.low) + ";" + t.owner.toString() + ";" + t.name + "}\n";
                for (UUID collab : t.collabs) {
                    build += "  " + collab + "\n";
                }
                buildall += build;
            }

        /*Territory{worldUUID-aaaa-aaaa-aaaaaaaa,x,y,z,p,a;worldUUID-aaaa-aaaa-aaaaaaaa,x,y,z,p,a;ownerUUID-aaaa-aaaa-aaaaaaaa;"Home"}
            <collab UUID>
            <collab UUID>
            <collab UUID>
            <collab UUID>
          End Territory
         */
            Files.write(file, buildall.getBytes(), StandardOpenOption.WRITE);
        }

        public void load() throws IOException {
            List<Territory> build = new LinkedList<>();
            Territory current = null;
            List<String> lines = Files.readAllLines(file, Charset.defaultCharset());
            for (String line : lines) {
                if (line.startsWith("Territory{")) {
                    if (current != null && !build.contains(current)) build.add(current);
                    String[] args = line.substring(10, line.trim().length() - 1).split(";");
                    current = new Territory(Utilities.deserializeLocation(args[0]),
                            Utilities.deserializeLocation(args[1]),
                            UUID.fromString(args[2]),
                            args[3].replaceAll("\"", ""));
                } else if (current != null && line.startsWith("  ")) {
                    try {
                        line = line.trim();
                        UUID col = UUID.fromString(line);
                        if (!current.hasCollaborator(col)) current.addCollaborator(col);
                    } catch (Exception e) {
                    }
                } else {
                    System.out.println("Territory Import Subsystem Error: What's this at line " + (lines.indexOf(line) + 1) + "?!: " + line);
                }
            }
            if (current != null) build.add(current);
            activeTerritories.clear();
            activeTerritories.addAll(build);
        }
    };

}
