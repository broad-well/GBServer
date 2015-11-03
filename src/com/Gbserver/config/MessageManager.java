package com.Gbserver.config;

import java.util.HashMap;
import java.util.logging.Logger;

import com.embryopvp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageManager {

    private static MessageManager instance = new MessageManager();
    private static ConfigManager configManager = ConfigManager.getInstance();
    private HashMap<PrefixType, String>prefix = new HashMap<PrefixType, String>();
    public enum PrefixType {

        MAIN, INFO, WARNING, ERROR, PORTAL, PERMISSIONS, VOTE, CRATE

    }

    public static MessageManager getInstance() {
        return instance;
    }

    public void setup(){
        FileConfiguration f = configManager.getMessagesConfig();
        prefix.put(PrefixType.MAIN, MessageUtils.replaceColors(f.getString("prefix.main")));
        prefix.put(PrefixType.INFO, MessageUtils.replaceColors(f.getString("prefix.states.info")));
        prefix.put(PrefixType.WARNING, MessageUtils.replaceColors(f.getString("prefix.states.warning")));
        prefix.put(PrefixType.ERROR, MessageUtils.replaceColors(f.getString("prefix.states.error")));
        prefix.put(PrefixType.PORTAL, MessageUtils.replaceColors(f.getString("prefix.portal")));
        prefix.put(PrefixType.PERMISSIONS, MessageUtils.replaceColors(f.getString("prefix.permissions")));
        prefix.put(PrefixType.VOTE, MessageUtils.replaceColors(f.getString("prefix.vote")));
        prefix.put(PrefixType.CRATE, MessageUtils.replaceColors(f.getString("prefix.crate")));
    }

    /**
     * SendMessage
     *
     * Loads a Message from messages.yml, converts its colors and replaces vars in the form of {$var} with its correct values,
     * then sends to the player, adding the correct prefix
     *
     * @param type
     * @param input
     * @param player
     * @param vars
     */
    public void sendFMessage(PrefixType type, String input, Player player, String ... args) {
        String msg = configManager.getMessagesConfig().getString("messages." + input);
        boolean enabled = configManager.getMessagesConfig().getBoolean("messages."+input+"_enabled", true);
        if(msg == null){player.sendMessage(ChatColor.RED+"Failed to load message for messages."+input); return;}
        if(!enabled)return;
        if(args != null && args.length != 0){msg = MessageUtils.replaceVars(msg, args);}
        msg = MessageUtils.replaceColors(msg);
        player.sendMessage(prefix.get(PrefixType.MAIN) + " " + prefix.get(type) + msg);

    }

    /**
     * SendMessage
     *
     * Sends a pre formated message from the plugin to a player, adding correct prefix first
     *
     * @param type
     * @param msg
     * @param p
     */

    public void sendPrefixMessage(PrefixType type, String msg, Player player){
        player.sendMessage(prefix.get(type) + " " + msg );
    }

    public void sendMessage(PrefixType type, String msg, Player player){
        player.sendMessage(prefix.get(PrefixType.MAIN)+ " "+prefix.get(type)+ msg );
    }

    public void logMessage(PrefixType type, String msg) {
        Logger logger = Bukkit.getServer().getLogger();
        switch (type) {
            case INFO:  logger.info(prefix.get(type)+ msg); break;
            case WARNING: logger.warning(prefix.get(type) + msg); break;
            case ERROR: logger.severe(prefix.get(type) + msg); break;
            default:
                break;
        }
    }

    public void broadcastFMessage(PrefixType type, String input, String ...args ) {
        String msg = configManager.getMessagesConfig().getString("messages." + input);
        boolean enabled = configManager.getMessagesConfig().getBoolean("messages."+input+"_enabled", true);
        if(msg == null){Bukkit.broadcastMessage(ChatColor.RED+"Failed to load message for messages."+input);return;}
        if(!enabled)return;
        if(args != null && args.length != 0){msg = MessageUtils.replaceVars(msg, args);}
        msg = MessageUtils.replaceColors(msg);
        Bukkit.broadcastMessage(prefix.get(PrefixType.MAIN)+ prefix.get(type)+ " "+msg);
    }

    public void broadcastMessage(PrefixType type, String msg){
        Bukkit.broadcastMessage(prefix.get(PrefixType.MAIN)+ " "+prefix.get(type)+ " "+msg );
    }

    public void broadcastPrefixMessage(PrefixType type, String msg){
        Bukkit.broadcastMessage(prefix.get(type)+ " "+msg );
    }

}
