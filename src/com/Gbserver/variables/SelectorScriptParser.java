package com.Gbserver.variables;

import com.Gbserver.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 12/13/15.
 */
public class SelectorScriptParser {
    public static final SelectorScriptParser instance = new SelectorScriptParser();

    /*
    Usage of SelectorScript
    %condition,condition,condition
    --
    Conditions:
    * all
    * random
    * me
    * <player name>
    * !<player name>
    --

     */
    public List<Player> parse(CommandSender sender, String input){
        List<Player> output = new LinkedList<>();
        if(!input.startsWith("%")) return output;
        String[] conditions = input.substring(1).split(",");
        for(String cond : conditions){
            switch(cond){
                case "all":
                    output.clear();
                    output.addAll(Bukkit.getOnlinePlayers());
                    break;
                case "random":
                    Player selected = (Player)
                            Bukkit.getOnlinePlayers().toArray()
                                    [Utilities.getRandom(0, Bukkit.getOnlinePlayers().size())];
                    if(!output.contains(selected)) output.add(selected);
                    break;
                case "me":
                    if(sender instanceof Player) {
                        if (!output.contains(sender)) {
                            output.add((Player) sender);
                        }
                    }
                    break;
                case "!random":
                    Player selectedNot = (Player)
                            Bukkit.getOnlinePlayers().toArray()
                                    [Utilities.getRandom(0, Bukkit.getOnlinePlayers().size())];
                    if(output.contains(selectedNot)) output.remove(selectedNot);
                    break;
                case "!me":
                    if(sender instanceof Player) {
                        if (output.contains(sender)) {
                            output.remove(sender);
                        }
                    }
                    break;
                default:
                    if(!(Bukkit.getPlayer(cond) == null && Bukkit.getPlayer(cond.substring(1)) == null)) {
                        if (cond.startsWith("!") && output.contains(Bukkit.getPlayer(cond.substring(1)))) {
                            output.remove(Bukkit.getPlayer(cond.substring(1)));
                        } else {
                            if(!output.contains(Bukkit.getPlayer(cond))){
                                output.add(Bukkit.getPlayer(cond));
                            }
                        }
                    }
            }
        }
        return output;
    }


}
