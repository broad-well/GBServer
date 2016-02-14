package com.Gbserver.variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChatGroup {
    private CommandSender creator;
    private List<CommandSender> members;

    public ChatGroup(CommandSender creator) {
        members = new LinkedList<>();
        this.creator = creator;
        members.add(creator);
        groups.add(this);
    }

    public void addMember(CommandSender p){
        if(!members.contains(p)) members.add(p);
        for(CommandSender member : members){
            ChatWriter.writeTo(member, ChatWriterType.CHAT, ChatColor.YELLOW + p.getName() + ChatColor.GRAY + " has joined the group!");
        }
    }

    public void sendMsg(CommandSender from, String msg){
        for(CommandSender p : members){

                p.sendMessage(ChatColor.DARK_AQUA.toString() +
                        ChatColor.BOLD + "Group " +
                        ChatColor.RESET.toString() + ChatColor.GRAY +
                        from.getName() + " " +
                        ChatColor.RESET + msg);

        }
    }

    public boolean hasMember(CommandSender p){
        return members.contains(p);
    }

    public void removeMember(CommandSender p){
        members.remove(p);
        for(CommandSender member : members){
            ChatWriter.writeTo(member, ChatWriterType.CHAT, ChatColor.YELLOW + p.getName() + ChatColor.GRAY + " has left the group.");
        }
    }

    public List<CommandSender> members() {
        List<CommandSender> returning = new LinkedList<>();
        returning.addAll(members);
        return returning;
    }

    public CommandSender getOwner() {
        return creator;
    }
    public boolean destroy(CommandSender calledFrom) {
        if(!calledFrom.equals(creator)) return false;
        groups.remove(this);
        for(CommandSender p : members){
            ChatWriter.writeTo(p, ChatWriterType.CHAT, "The party has been closed.");
        }
        members.clear();
        members = null;
        return true;
    }
    
    //-------
    public static Collection<ChatGroup> groups = new LinkedList<>();
    
    public static ChatGroup getGroupIn(CommandSender p){
        for(ChatGroup group : groups){
            if(group.hasMember(p)) return group;
        }
        return null;
    }

    public static ChatGroup getOwnedGroup(CommandSender p){
        for(ChatGroup group : groups){
            if(group.getOwner().equals(p)) return group;
        }
        return null;
    }
    public static boolean join(CommandSender member, String creatorName){
        for(ChatGroup group : groups){
            if(group.getOwner().getName().equalsIgnoreCase(creatorName)){
                group.addMember(member);
                return true;
            }
        }
        return false;
    }
}
