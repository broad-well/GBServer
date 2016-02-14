package com.Gbserver.commands;

import com.Gbserver.Main;
import com.Gbserver.Utilities;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.Sandbox;
import com.Gbserver.variables.Territory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Protection implements CommandExecutor {
    public static Main instance;

    public Protection(Main instance) {
        Protection.instance = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Sandbox.check(sender)) return true;
        if(Utilities.validateSender(sender)){
            if(args.length == 0){
                sender.sendMessage("/protection Usage: /protection <add/delete/list/addcollab/remcollab> [x1] [y1] [z1] [x2] [y2] [z2] [name]");
                return true;
            }
            switch(args[0]){
                case "add":
                    if(args.length < 8){
                        //Desired args count: 8
                        sender.sendMessage("/~ add Usage: /protection add <x1> <y1> <z1> <x2> <y2> <z2> <name>");
                        //                                            0   1    2    3    4    5    6    7
                        return true;
                    }
                    //Double check
                    double[] coordinates = new double[6];
                    try {
                        coordinates[0] = Double.parseDouble(args[1]);
                        coordinates[1] = Double.parseDouble(args[2]);
                        coordinates[2] = Double.parseDouble(args[3]);
                        coordinates[3] = Double.parseDouble(args[4]);
                        coordinates[4] = Double.parseDouble(args[5]);
                        coordinates[5] = Double.parseDouble(args[6]);
                    }catch(Exception e){
                        ChatWriter.writeTo(sender, ChatWriterType.ERROR, "Bad numbers in your command.");
                        return true;
                    }
                    //Is there already a territory with the same name?
                    for(Territory t : Territory.activeTerritories){
                        if(t.getName().equalsIgnoreCase(args[7])){
                            ChatWriter.writeTo(sender, ChatWriterType.ERROR, "Protection name already exists.");
                            return true;
                        }
                    }
                    Territory build = new Territory(
                            coordinates[1] < coordinates[4] ? //Is the first location lower than the second?
                                    new Location(((Player)sender).getWorld(), coordinates[3], coordinates[4], coordinates[5]) :
                                    new Location(((Player)sender).getWorld(), coordinates[0], coordinates[1], coordinates[2]),
                            coordinates[1] < coordinates[4] ? //Is the first location lower than the second?
                                    new Location(((Player)sender).getWorld(), coordinates[0], coordinates[1], coordinates[2]) :
                                    new Location(((Player)sender).getWorld(), coordinates[3], coordinates[4], coordinates[5]),
                            ((Player) sender).getUniqueId(),
                            args[7]);
                    Territory.activeTerritories.add(build);
                    ChatWriter.writeTo(sender,ChatWriterType.COMMAND, "Protection area successfully created by the name of " + ChatColor.YELLOW + args[7]
                            + ChatColor.GRAY + ".");
                    break;
                case "delete":
                    if(args.length < 2){
                        sender.sendMessage("/~ delete Usage: /protection delete <name>");
                        return true;
                    }
                    Territory toDelete = null;
                    for(Territory t : Territory.activeTerritories){

                        if((t.getName().equalsIgnoreCase(args[1]) || t.getName().startsWith(args[1])) &&
                                t.getOwner().equals(((Player) sender).getUniqueId())){
                            toDelete = t;
                        }
                    }
                    if(toDelete != null){
                        ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Successfully deleted this protection area.");
                        Territory.activeTerritories.remove(toDelete);
                    }else{
                        ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Unable to locate this area by your input. Either it does not exist or it is not yours.");
                    }
                    break;
                case "list":
                    Player p = (Player) sender;
                    p.sendMessage("--------------------");
                    for(Territory t : Territory.activeTerritories){
                        if(t.getOwner().toString().equalsIgnoreCase(p.getUniqueId().toString())){
                            p.sendMessage(verbalize(t));
                            p.sendMessage("--------------------");
                        }
                    }
                    break;
                case "addcollab":
                    if(args.length < 3){
                        //Desired args length: 3
                        sender.sendMessage("/~ addcollab Usage: /protection addcollab <name of area> <name of collaborator>");
                        sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "WARNING: MAKE SURE THE NAME OF THE COLLABORATOR IS CASE SENSITIVE AND ACCURATE!");
                        return true;
                    }
                    for(Territory t : Territory.activeTerritories){
                        if((t.getName().equalsIgnoreCase(args[1]) || t.getName().startsWith(args[1])) &&
                                t.getOwner().equals(((Player) sender).getUniqueId())){
                            if(!t.hasCollaborator(Bukkit.getOfflinePlayer(args[2]).getUniqueId())) {
                                t.addCollaborator(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "Successfully added this person to this area as a collaborator.");
                            }else{
                                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "This collaborator is already included.");
                            }
                            break;

                        }
                    }

                    break;
                case "remcollab":
                    if(args.length < 3){
                        //Desired args length: 3
                        sender.sendMessage("/~ remcollab Usage: /protection remcollab <name of area> <name of collaborator>");
                        sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "WARNING: MAKE SURE THE NAME OF THE COLLABORATOR IS CASE SENSITIVE AND ACCURATE!");
                        return true;
                    }
                    for(Territory t : Territory.activeTerritories){

                        if((t.getName().equalsIgnoreCase(args[1]) || t.getName().startsWith(args[1])) &&
                                t.getOwner().equals(((Player) sender).getUniqueId())){
                            if(t.hasCollaborator(Bukkit.getOfflinePlayer(args[2]).getUniqueId())){
                                t.removeCollaborator(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "That collaborator has been deleted.");
                            }else{
                                ChatWriter.writeTo(sender, ChatWriterType.COMMAND, "That collaborator is not in this area.");
                            }
                            break;
                        }
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Unknown option: " + args[0]);
                    break;
            }
        }
        return true;
    }

    public static String[] verbalize(Territory t){
        String[] output = new String[4];
        output[0] = ChatColor.AQUA + "---=== " + t.getName() + " Stats ===---";
        output[1] = ChatColor.DARK_AQUA + "Owner: " + ChatColor.YELLOW + Bukkit.getOfflinePlayer(t.getOwner()).getName();
        output[2] = ChatColor.DARK_GREEN + "HX:" + ChatColor.YELLOW + t.getHigh().getX() +
                ChatColor.DARK_GREEN + " HY:" + ChatColor.YELLOW + t.getHigh().getY() +
                ChatColor.DARK_GREEN + " HZ:" + ChatColor.YELLOW + t.getHigh().getZ() +
                ChatColor.GREEN + " LX:" + ChatColor.YELLOW + t.getLow().getX() +
                ChatColor.GREEN + " LY:" + ChatColor.YELLOW + t.getLow().getY() +
                ChatColor.GREEN + " LZ:" + ChatColor.YELLOW + t.getLow().getZ();
        output[3] = ChatColor.BLUE + "Collaborators: " + ChatColor.YELLOW;
        for(UUID col : t.getCollaborators()){
            output[3] += Bukkit.getOfflinePlayer(col).getName();
            if(t.getCollaborators().indexOf(col) != t.getCollaborators().size() - 1){
                output[3] +=  ChatColor.GRAY + ", " + ChatColor.YELLOW;
            }
        }
        return output;

    }

}
