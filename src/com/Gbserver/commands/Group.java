package com.Gbserver.commands;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Group implements CommandExecutor{
    public static HelpTable ht = new HelpTable("/group <[yourMessage],[create],[join <target>],[leave <target>],[close],[kick <target>]>", "For Party chatting","/g","group");
    public boolean onCommand(CommandSender sender, Command cmd, String text, String[] args){
        if(Sandbox.check(sender)) return true;
        ChatGroup group;
        //!!! Add requesting to join !!!//
        if(args.length == 0){
            ht.show(sender);
            return true;
        }
        switch(args[0]){
            case "add":
                if(args.length == 1){
                    ht.show(sender);
                    return true;
                }
                if((group = ChatGroup.getOwnedGroup(sender)) == null){
                    sender.sendMessage(CPrefix.Prf.CONDITION + "You do not own any ChatGroup.");
                    return true;
                }
                for(int i = 1; i < args.length; i++){
                    group.addMember(Bukkit.getPlayer(args[i]));
                    sender.sendMessage(CPrefix.Prf.CHAT + "Added " + Sh.pc("yellow") + Bukkit.getPlayer(args[i]).getName());
                }
                break;
            case "create":
                new ChatGroup(sender);
                sender.sendMessage(CPrefix.Prf.CHAT + "You have created a new group. Use " +
                        Sh.pc("yellow") + "/group add <player>" + Sh.pc("gray") + " to add players.");
                break;
            case "join":
                if(args.length == 1){
                    ht.show(sender);
                    return true;
                }
                if(!ChatGroup.join(sender, args[1])){
                    sender.sendMessage(CPrefix.Prf.CHAT + "That group does not exist.");
                }else{
                    sender.sendMessage(CPrefix.Prf.CHAT + "You have joined the group!");
                }
                break;
            case "leave":
                if((group = ChatGroup.getGroupIn(sender)) == null){
                    sender.sendMessage(CPrefix.Prf.CONDITION + "You are not in any ChatGroup.");
                    return true;
                }
                group.removeMember(sender);
                sender.sendMessage(CPrefix.Prf.CONDITION + "You have left " +
                        ChatColor.YELLOW + group.getOwner().getName() + "'s " + ChatColor.GRAY + "group.");
                break;
            case "kick":
                if(args.length == 1){
                    ht.show(sender);
                    return true;
                }
                if((group = ChatGroup.getOwnedGroup(sender)) == null){
                    sender.sendMessage(CPrefix.Prf.CONDITION + "You do not own any ChatGroup.");
                    return true;
                }
                for(int i = 1; i < args.length; i++){
                    Bukkit.getPlayer(args[i]).sendMessage(CPrefix.Prf.CONDITION + "You have been ejected from the group by its owner.");
                    group.removeMember(Bukkit.getPlayer(args[i]));
                }
                break;
            case "close":
                if((group = ChatGroup.getGroupIn(sender)) == null){
                    sender.sendMessage(CPrefix.Prf.CONDITION + "You are not in any ChatGroup.");
                    return true;
                }
                if(!group.destroy(sender)){
                    sender.sendMessage(CPrefix.Prf.CHAT + "You are not the creator of the chat; You cannot destroy it.");
                    return true;
                }
                sender.sendMessage(CPrefix.Prf.CONDITION + "Your chat group has been closed.");
                break;
            default:
                if((group = ChatGroup.getGroupIn(sender)) == null){
                    sender.sendMessage(CPrefix.Prf.CONDITION + "You are not in any ChatGroup.");
                    return true;
                }
                group.sendMsg(sender, Utilities.concat(args));
        }
        return true;
    }
}
