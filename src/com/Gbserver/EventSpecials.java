package com.Gbserver;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventSpecials implements Listener{
    //Currently: Christmas & New Year
    //Use 185 for worldborder


    public static Calendar get2016() {
        Calendar output = Calendar.getInstance();
        output.setTime(new Date(0));
        output.set(Calendar.DAY_OF_MONTH, 1);
        output.set(Calendar.MONTH, 0);
        output.set(Calendar.YEAR, 2016);
        return output;
    }
    public static int CountDownThread;
    private static final String prefix = ChatColor.GREEN + "Countdown> ";
    public static void setup2016Countdown() {
        // Check situation.
        Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(),new Runnable()
        {
            public void run() {
                while(true){
                    if((get2016().getTimeInMillis() -
                            Calendar.getInstance(TimeZone.getTimeZone("America/New_York")).getTimeInMillis()) / 1000 <= 60*60){
                        break;
                    }
                }
                Bukkit.broadcastMessage(prefix + "One hour left for EST!");
                //Per minute mode, until 30 seconds left.

            }
        });

    }
    private static final int YEARS = 0,
            WEEKS = 1,
            DAYS = 2,
            HOURS = 3,
            MINUTES = 4,
            SECONDS = 5,
    //                1...MIN  HOUR DAY  YEAR
            SEC_IN_YEAR = 60 * 60 * 24 * 365,
    //                1...MIN  HOUR DAY  WEEK
            SEC_IN_WEEK = 60 * 60 * 24 * 7,
    //               1...MIN  HOUR DAY
            SEC_IN_DAY = 60 * 60 * 24;
    private static long[] humanFriendly(long seconds){ // say 605000
        long[] output = new long[6]; //6 arguments
        output[YEARS] = seconds / SEC_IN_YEAR;
        seconds -= SEC_IN_YEAR * output[YEARS];
        output[WEEKS] = seconds / SEC_IN_WEEK;
        seconds -= SEC_IN_WEEK * output[WEEKS];
        output[DAYS] = seconds / SEC_IN_DAY;
        seconds -= SEC_IN_DAY * output[DAYS];
        output[HOURS] = seconds / (60 * 60);
        seconds -= (60 * 60) * output[HOURS];
        output[MINUTES] = seconds / 60;
        seconds -= 60 * output[MINUTES];
        output[SECONDS] = seconds;
        return output;
    }
    //Actual listeners

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde){
        // 6488 74 -3276 6465 74 -3312
        // Quit team, clear inventory
        final Player dead = pde.getEntity();
        if(Utilities.isInRangeOf(
                new Location(Bukkit.getWorld("world"), 6488, 100, -3276),
                new Location(Bukkit.getWorld("world"), 6465, 20, -3312),
                dead.getLocation())){
            pde.setKeepInventory(false);
            pde.setDeathMessage(ChatColor.YELLOW + dead.getName() + " has died during spleef.");
            dead.setHealth(20);
            dead.teleport(new Location(Bukkit.getWorld("world"), 6482.5, 55.2, -3282.5));
            dead.sendMessage(ChatColor.DARK_AQUA.toString() + ChatColor.ITALIC + "Good game!");
            dead.getInventory().clear();
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam("red").removeEntry(dead.getName());
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam("blue").removeEntry(dead.getName());

            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams leave red " + dead.getName());
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams leave blue " + dead.getName());
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "clear " + dead.getName());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Utilities.getInstance(), new Runnable() {
                @Override
                public void run() {
                    dead.setGameMode(GameMode.SPECTATOR);
                    dead.teleport(new Location(Bukkit.getWorld("world"), 6481, 83, -3294));
                    ChatWriter.writeTo(dead, ChatWriterType.GAME, "Please wait for the completion of the previous game.");
                }
            }, 10L);
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        pje.getPlayer().sendMessage(ChatColor.RED + "Santa> " + ChatColor.GOLD + "Merry Christmas, fellow " +
                ChatColor.YELLOW + pje.getPlayer().getName() + ChatColor.GOLD + "! Hoe, ho ho!");
        pje.getPlayer().sendMessage(ChatColor.GOLD + "Broadwell> " + ChatColor.AQUA +
                "I would like to show you a few interesting commands to use during this event:");
        pje.getPlayer().sendMessage(ChatColor.GOLD + "Broadwell> " + ChatColor.DARK_AQUA +
                "/ping, /friend, /admin, /group, /vote, /hat, /heal, /ride, /quit");
        pje.getPlayer().sendMessage(ChatColor.GOLD + "Broadwell> " + ChatColor.DARK_AQUA.toString() +
                ChatColor.ITALIC + "Also take a look at my newest invention, SelectorScript! "
                + ChatColor.YELLOW + "/selscript");
        ChatWriter.writeTo(pje.getPlayer(), ChatWriterType.EVENT, "Your hunger is covered by us! No food required.");
    }
}
