package com.Gbserver.listener.hiddencmds;

import com.Gbserver.variables.DebugLevel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdListener implements CommandExecutor, Listener {
    public interface CmdModule {
        String GetLabel();

        int Execute(String command, CommandSender sender);

        boolean IsEligible(CommandSender sender);
    }

    public static List<CmdModule> modules = Arrays.asList();
    private static DebugLevel dl = new DebugLevel(2, "HiddenCmds_ModuleHost");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Missing SubCommand Argument.");
            return true;
        }

        for (CmdModule mod : modules) {
            if (mod.GetLabel().equalsIgnoreCase(args[0])) {
                // todo Finish this!
            }
        }
        return true;
    }

    public static boolean ModuleCheck() {
        List<String> registeredCmds = new ArrayList<>();

        for (CmdModule mod : modules) {
            if (registeredCmds.contains(mod.GetLabel())) {
                dl.debugWrite("Encountered Duplicate Modules, " + mod.GetLabel());
                return false;
            } else {
                registeredCmds.add(mod.GetLabel());
            }
        }
        dl.debugWrite(4, "Module load successful.");
        return true;

    }


}
