package com.Gbserver.variables.minigame;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MGUtils {
    private static DebugLevel dl = new DebugLevel(3, "MinigameHelper");
    public static final int[] GAMEPLAY_LOCATION = {0, 100, 0};
    public static HashMap<Games, MGUtils> utilAccess = new HashMap<>();
    //-----
    public Minigame mg;
    public String selectedMap;
    public ScoreDisplay sd;
    public List<Integer> repeatingThreadPool = new LinkedList<>();

    public MGUtils(Minigame m) {
        mg = m;
        dl.debugWrite(5, "Initializing new MGUtils, identifier " + mg.getIdentifier() + ", Games enum " +
                Games.fromString(mg.getIdentifier()));
        utilAccess.put(Games.fromString(mg.getIdentifier()), this);
    }

    public void fixDuplicatePlayers() {
        List<Player> encountered = new LinkedList<>();
        List<Integer> toDelete = new LinkedList<>();
        for (Player p : mg.getPlayers()) {
            if (encountered.contains(p)) {
                dl.debugWrite("While fixing duplicate players, found duplicate name:" + p.getName() +
                        " in minigame:" + mg.getIdentifier());
                toDelete.add(mg.getPlayers().indexOf(p));
            } else {
                encountered.add(p);
            }
            onPlayerContentModification();
        }
        for (Integer i : toDelete) {
            encountered.remove(i.intValue());
        }
        //Finished.
        toDelete.clear();
        encountered.clear();
    }

    private int startCd = 10;
    private int tasknum;

    public boolean startGame() {
        if (mg.getRunlevel() != 2) {
            Bukkit.getLogger().warning("Invalid Run-level for starting game of " +
                    mg.getIdentifier());
            return false;
        }
        //Available maps?
        if (selectedMap == null) {
            Bukkit.getLogger().warning("Map not set for starting game of " + mg.getIdentifier());
            dl.debugWrite(5, "Runlevel of " + mg.getIdentifier() + " has been set to 1");
            mg.setRunlevel(1);
            return false;
        }
        //Teleportation
        for (int i = 0; i < mg.getPlayers().size(); i++) {
            mg.getPlayers().get(i).teleport(mg.getSpawnpoints().get(i));
        }
        //Secondary countdown
        startCd = 10;
        tasknum = Utilities.scheduleRepeat(new Runnable() {
            public void run() {
                if (startCd == 0) {
                    //Launch threads.
                    mg.getStartProcedure().run();
                    for (final Runnable run : mg.getThreads()) {
                        dl.debugWrite(mg.getIdentifier() + ": New Runnable scheduled and added to " +
                                "repeatingThreadPool: " + run.toString());
                        repeatingThreadPool.add(Utilities.scheduleRepeat(new Runnable() {
                            @Override
                            public void run() {
                                run.run();
                            }
                        }, 1L));
                    }
                    Bukkit.getScheduler().cancelTask(tasknum);
                } else {
                    for (Player p : mg.getPlayers()) {
                        p.sendMessage(ChatColor.GOLD.toString() + ChatColor.ITALIC + "Game starting in " + startCd + " seconds...");
                    }
                    startCd--;
                }
            }
        }, 20);
        dl.debugWrite(5, "Runlevel of " + mg.getIdentifier() + " has been set to 3");
        mg.setRunlevel(3);
        return true;

    }

    /**
     * To be called during <code>onEnable()</code>.
     *
     * @return True if initialization succeeds, otherwise false
     */
    public boolean initialize() {
        if (mg.getRunlevel() != 0) {
            Bukkit.getLogger().warning("Bad RunLevel for initialization of game " + mg.getIdentifier());
            return false;
        }
        refreshLobbyScoreboard();
        dl.debugWrite(5, "Runlevel of " + mg.getIdentifier() + " has been set to 1");
        mg.setRunlevel(1);
        return true;
    }

    public boolean refreshLobbyScoreboard() { //Call this when someone joins the lobby.
        if (mg.getRunlevel() > 2) {
            Bukkit.getLogger().warning("Bad RunLevel for refreshLobbyScoreboard; " +
                    mg.getIdentifier() + " game in progress / cleanup.");
            return false;
        }
        //Obtain scoreboard for lobby.
        sd = new ScoreDisplay(ChatColor.DARK_GREEN + ChatColor.ITALIC.toString() + mg.getIdentifier() + " Lobby", mg.getPlayers());
        sd.setLine("", 1);
        sd.setLine(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Players", 2);
        sd.setLine(mg.getPlayers().size() + "/" + mg.getMaxPlayers(), 3);
        sd.setLine(" ", 4);
        sd.setLine(ChatColor.GOLD.toString() + ChatColor.BOLD + "Map", 5);
        sd.setLine((selectedMap == null ? "NullMap" : selectedMap), 6);
        sd.setLine("  ", 7);
        return true;
    }


    private void clearScoreboard() {
        for (Player p : mg.getPlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

    private int countdown = 40, countdownId = 1;

    public boolean prepareforStart() { //Will start the game on itself
        if (mg.getRunlevel() != 1) {
            Bukkit.getLogger().warning("Bad RunLevel for prepareForStart() in minigame " + mg.getIdentifier());
            return false;
        }
        if (selectedMap == null) {
            Bukkit.getLogger().warning("In game " + mg.getIdentifier() + ", map has not been set. " +
                    "Aborting prepareforStart().");
            return false;
        }
        //Initialize countdown. (40 seconds)
        refreshLobbyScoreboard();
        sd.setLine(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Countdown", 8);
        countdownId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utilities.getInstance(), new Runnable() {
            public void run() {
                if (countdown == 0) {
                    startGame();
                    clearScoreboard();
                    Bukkit.getScheduler().cancelTask(countdownId); //Self-destroy
                } else {
                    countdown--;
                    sd.setLine(String.valueOf(countdown), 9);
                }
            }
        }, 5L, 20L);
        dl.debugWrite("Summoned new Countdown Repeating Task for " + mg.getIdentifier() + ", ID " + countdownId);
        Utilities.copy(mg.getMaps().get(selectedMap).getLow(),
                mg.getMaps().get(selectedMap).getHigh(),
                new Location(mg.getWorld(), GAMEPLAY_LOCATION[0], GAMEPLAY_LOCATION[1], GAMEPLAY_LOCATION[2]));
        dl.debugWrite(5, "Runlevel of " + mg.getIdentifier() + " has been set to 2");
        mg.setRunlevel(2);
        countdown = 40;
        return true;
    }

    public boolean stop(boolean abort) {
        if (mg.getRunlevel() != 3) {
            Bukkit.getLogger().warning("While " + (abort ? "aborting " : "stopping ") + mg.getIdentifier()
                    + ", Runlevel is not 3. Operation failed.");
            return false;
        }
        if (!abort && unitsLeft() > 1) {
            Bukkit.getLogger().warning("While stopping " + mg.getIdentifier() + ", multiple players / teams left." +
                    " Operation failed.");
            return false;
        }
        for (Integer task : repeatingThreadPool) {
            Bukkit.getScheduler().cancelTask(task);
        }
        repeatingThreadPool.clear();
        for (Player p : allParticipants()) {
            p.playSound(p.getLocation(), Sound.NOTE_PIANO, 20, 1);
            p.sendTitle(ChatColor.BOLD + ChatColor.AQUA.toString() + "Game " + (abort ? "aborted." : "finished."),
                    (abort ? "" :
                            (mg instanceof StandaloneMG ?
                                    ChatColor.YELLOW + mg.getPlayers().get(0).getName() + ChatColor.RESET +
                                            " has won the game!" :
                                    getTeam(mg.getPlayers().get(0)).toColor() + getTeam(mg.getPlayers().get(0)).toString() +
                                            " Team" + ChatColor.RESET + " has won the game!")));
        }
        dl.debugWrite(5, "Runlevel of " + mg.getIdentifier() + " has been set to 4");
        mg.setRunlevel(4);
        Bukkit.getScheduler().runTaskLater(Utilities.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player p : allParticipants()) {
                    p.teleport(mg.getWorld().getSpawnLocation());
                }
                mg.getPlayers().clear();
                mg.getSpectators().clear();
                dl.debugWrite(5, "Runlevel of " + mg.getIdentifier() + " has been set to 1");
                mg.setRunlevel(1);
            }
        }, 20 * 3);

        return true;
    }

    private List<Player> allParticipants() {
        SafeLinkedList<Player> build = new SafeLinkedList<>();
        build.addAll(mg.getPlayers());
        build.addAll(mg.getSpectators());
        return build;
    }


    public int unitsLeft() {
        if (mg instanceof StandaloneMG) {
            return mg.getPlayers().size();
        } else {
            SafeLinkedList<TeamColor> encountered = new SafeLinkedList<>();
            for (Player p : mg.getPlayers()) {
                encountered.insert(getTeam(p));
            }
            dl.debugWrite(4, "Queried unitsLeft(), hashmap " + ((TeamedMG) mg).getTeamConfig() + ", result " +
                    encountered.size());
            return encountered.size();
        }
    }

    public TeamColor getTeam(Player p) {
        if (mg instanceof TeamedMG) {
            for (Map.Entry<TeamColor, List<Player>> entry : ((TeamedMG) mg).getTeamConfig().entrySet()) {
                if (entry.getValue().contains(p)) return entry.getKey();
            }
            return null;
        } else return null;
    }

    public boolean eliminate(Player p) {
        if (mg.getRunlevel() != 3) {
            Bukkit.getLogger().warning("Cannot eliminate player, because runlevel not 3. " + mg.getIdentifier());
            return false;
        }
        if (!mg.getPlayers().contains(p)) {
            Bukkit.getLogger().warning("Cannot eliminate player, because not in game: " + p.getName() +
                    ". " + mg.getIdentifier());
            return false;
        }
        removePlayer(p);
        if (p.getHealth() > 0)
            p.setHealth(p.getHealth() - 1);
        p.setGameMode(GameMode.SPECTATOR);
        p.teleport(p.getLocation().add(0, 100, 0));
        p.sendMessage(ChatColor.BLUE + "Game> " + ChatColor.GRAY + "You have been eliminated.");
        mg.getSpectators().add(p);
        for (Player play : allParticipants()) {
            ChatWriter.writeTo(play, ChatWriterType.GAME, ChatColor.YELLOW + p.getName() +
                    ChatColor.GRAY + " has been eliminated.");
        }
        onPlayerContentModification();
        return true;
    }

    public boolean abandon(OfflinePlayer p) {
        removePlayer(p.getPlayer());
        if (mg.getSpectators().contains(p.getPlayer())) mg.getSpectators().remove(p.getPlayer());
        assert !allParticipants().contains(p.getPlayer()); //Make sure we don't enrage the GC!
        for (Player pl : mg.getPlayers()) {
            ChatWriter.writeTo(pl, ChatWriterType.GAME, ChatColor.YELLOW + p.getName() +
                    ChatColor.GRAY + " has abandoned the game.");
        }
        onPlayerContentModification();
        return true;
    }

    private void broadcast(String msg, boolean includeSpectators) {
        for (Player player : mg.getPlayers()) {
            player.sendMessage(msg);
        }
        if (includeSpectators) {
            for (Player spec : mg.getSpectators()) {
                spec.sendMessage(msg);
            }
        }
    }

    private void removePlayer(Player p) {
        if (p == null) return;
        if (mg instanceof StandaloneMG) {
            List<Player> current = mg.getPlayers();
            current.remove(p);
            mg.setPlayers(current);
        } else {
            for (List<Player> team : ((TeamedMG) mg).getTeamConfig().values()) {
                team.remove(p);
            }
        }
        //All methods that contain this function should use onPlayerContentModification().
    }

    public boolean interpret(String[] input, CommandSender sender) {
        switch (input[0]) {
            case "abort":
                return stop(true);
            case "stop":
                return stop(false);
            case "start":
                return prepareforStart();
            case "runlevel":
                sender.sendMessage(ChatColor.DARK_AQUA + "Runlevel of " + mg.getIdentifier() + ": " + mg.getRunlevel());
                return true;
            case "leave":
                if (mg.getRunlevel() > 2) {
                    sender.sendMessage("The current status of this game is not applicable to leave.");
                    return false;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Must be a player to execute this subcommand.");
                    return false;
                }
                removePlayer((Player) sender);
                refreshLobbyScoreboard();
                //RunLevel is not 3, so it does not really matter. *NOT CALLING onPlayerContentModification().*
                return true;
            case "join":
                if (mg.getRunlevel() > 2) {
                    sender.sendMessage("The current status of this game is not applicable to join.");
                    return false;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Must be a player to execute this subcommand.");
                    return false;
                }
                if (mg.getPlayers().size() >= mg.getMaxPlayers()) {
                    sender.sendMessage("Sorry, the game is full.");
                    return false;
                }
                if (mg instanceof StandaloneMG) {
                    List<Player> current = mg.getPlayers();
                    current.add((Player) sender);
                    mg.setPlayers(current);
                    sender.sendMessage(ChatColor.YELLOW + "You have been added to this game.");
                } else {
                    TeamColor selected = getVacantTeam();
                    ((TeamedMG) mg).getTeamConfig().get(selected).add((Player) sender);
                    sender.sendMessage(ChatColor.YELLOW + "You have been added to " + selected.toColor() +
                            selected.toString() + " Team");
                }
                refreshLobbyScoreboard();
                return true;
            case "list":
                if (mg instanceof StandaloneMG) {
                    for (Player p : mg.getPlayers()) {
                        sender.sendMessage(p.getDisplayName());
                    }
                } else {
                    for (Map.Entry<TeamColor, List<Player>> team : ((TeamedMG) mg).getTeamConfig().entrySet()) {
                        sender.sendMessage(team.getKey().toColor() + team.getKey().toString() + " Team:");
                        for (Player pl : team.getValue()) {
                            sender.sendMessage("  " + pl.getName());
                        }
                    }
                }
                return true;
            case "selectMap":
                if (!mg.getMaps().containsKey(input[1])) {
                    sender.sendMessage("This map does not exist.");
                    return false;
                }
                selectedMap = input[1];
                return true;
            default:
                sender.sendMessage("Options: runlevel, list, join, leave");
                return false;
        }
    }

    private TeamColor getVacantTeam() {
        if (mg instanceof StandaloneMG) return null;
        LinkedList<Integer> build = new LinkedList<>(); //Explicitly LinkedList because order matters.
        LinkedList<TeamColor> sequence = new LinkedList<>();
        for (Map.Entry<TeamColor, List<Player>> team : ((TeamedMG) mg).getTeamConfig().entrySet()) {
            sequence.add(team.getKey());
            build.add(team.getValue().size());
        }
        if (allEqual(build, build.get(0))) return (TeamColor) Utilities.randomElement(sequence);
        //Otherwise, find the least.
        int lowestSeen = Integer.MAX_VALUE;
        for (List<Player> pls : ((TeamedMG) mg).getTeamConfig().values()) {
            if (pls.size() < lowestSeen) lowestSeen = pls.size();
        }
        //3 2 1 3
        return sequence.get(build.indexOf(lowestSeen));
    }

    private boolean allEqual(List<Integer> list, Integer criteria) {
        for (Integer Int : list) {
            if (!Int.equals(criteria)) return false;
        }
        return true;
    }

    public boolean isEligible(Player p) { //For listeners
        return mg.getPlayers().contains(p) && mg.getRunlevel() == 3;
    }

    private void onPlayerContentModification() {
        if (unitsLeft() == 1) {
            stop(false);
        }
    }

}
