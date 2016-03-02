package com.Gbserver.listener;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Reaction implements ChatModule {

    //Subclass "Equation"
    public static List<Equation> equations = new LinkedList<>();


    //Settings
    private static final int firstMax = 1000;
    private static final int firstMin = -1000;
    private static final int secondMax = 2000;
    private static final int secondMin = -2000;

    public static void getRepeatingEvent() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
            public void run() {
                if (Bukkit.getOnlinePlayers().size() > 0) {
                    Equation e = getEquation();
                    e.getChatMessage();
                    currentEquation = e;
                    pending = true;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
                        public void run() {
                            if (pending) {
                                ChatWriter.write(ChatWriterType.CHAT,
                                        ChatColor.YELLOW + "REACTION: Time's up! Nobody got the answer.");
                                ChatWriter.write(ChatWriterType.CHAT,
                                        ChatColor.YELLOW + "The answer is: " + currentEquation.calculate());
                                currentEquation.close();
                                currentEquation = null;
                                pending = false;
                            }
                        }
                    }, 1200L);
                }
            }
        }, 0L, 18000L);
    }

    public static Equation getEquation() {
        //+-x are easy
        //On ÷, need to check if remainder is 0.
        int op = Utilities.getRandom(Equation.PLUS, Equation.POWER);
        int first = Utilities.getRandom(firstMin, firstMax);
        int second = Utilities.getRandom(secondMin, secondMax);
        while (op == Equation.DIVIDE && first % second != 0) {
            first = Utilities.getRandom(firstMin, firstMax);
            second = Utilities.getRandom(secondMin, secondMax);
        }
        if (op == Equation.POWER) {
            second = Utilities.getRandom(3, 10);
        }
        Equation e = new Equation(first, op, second);
        return e;
        //long r = e.calculate();
        //e.close();
        //return r;
    }

    //LISTENER-------------------------------------
    private static Equation currentEquation;
    private static boolean pending = false;

    public String handleChat(String msg, Player player) {
        if (pending) {
            long ans = 0;
            try {
                ans = Long.parseLong(msg);
            } catch (Exception e) {
                return "true";
            }
            if (ans == currentEquation.calculate()) {
                //Got it correct!
                player.sendMessage(ChatWriter.getMessage(ChatWriterType.CHAT, "Congratulations! You got it right!"));
                ChatWriter.write(ChatWriterType.CHAT, ChatColor.YELLOW + "REACTION: " + player.getName() + " got the answer! The answer is " + ans);
                currentEquation.close();
                currentEquation = null;
                pending = false;
                return "false";

            } else {
                ChatWriter.writeTo(player, ChatWriterType.CHAT, "I don't think that is the right answer. Try again!");
                return "false";
            }
        }
        return "true";
    }

    @Override
    public String getName() {
        return "MathReaction Interpreter";
    }

    @Override
    public HashMap<String, String> passThru(HashMap<String, String> hs) {
        hs.put("enabled", handleChat(hs.get("msg"), Bukkit.getPlayer(hs.get("sender"))));
        return hs;
    }
}
