package com.Gbserver.listener.chatmodules;

import com.Gbserver.commands.Mail;
import com.Gbserver.listener.ChatFormatter;
import com.Gbserver.listener.ChatModule;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 1/30/16.
 */
public class ChatMail implements ChatModule{
    @Override
    public String getName() {
        return "Mail writer chat module";
    }

    @Override
    public HashMap<String, String> passThru(HashMap<String, String> hs) {
        if(Mail.mailWriteStatus.containsKey(Bukkit.getPlayer(hs.get("sender")))) {
            hs.put("enabled", "false");
            if (Mail.mailWriteStatus.get(Bukkit.getPlayer(hs.get("sender"))) == Mail.PENDING_SUBJECT) {
                ChatFormatter.activeBuffer.remove(Bukkit.getPlayer(hs.get("sender")));
                List<String> newset = new LinkedList<>();
                newset.add(hs.get("msg"));
                ChatFormatter.activeBuffer.put(Bukkit.getPlayer(hs.get("sender")), newset);
                Mail.mailWriteStatus.put(Bukkit.getPlayer(hs.get("sender")), Mail.PENDING_MESSAGE);
                ChatWriter.writeTo(Bukkit.getPlayer(hs.get("sender")), ChatWriterType.POSTMAN, "Successfully set the current message subject. You may now send messages for lines of the mail message. When done sending the message, use " + ChatColor.YELLOW +
                        "/mail send <recipient's name>" + ChatColor.GRAY + ".");
            } else {
                String message = hs.get("msg") + "\n";
                List<String> strs = ChatFormatter.activeBuffer.get(Bukkit.getPlayer(hs.get("sender")));
                if (ChatFormatter.activeBuffer.get(Bukkit.getPlayer(hs.get("sender"))).size() > 1) {
                    strs.set(1, ChatFormatter.activeBuffer.get(Bukkit.getPlayer(hs.get("sender"))).get(1) + message);
                } else {
                    strs.add(message);
                }
                ChatFormatter.activeBuffer.put(Bukkit.getPlayer(hs.get("sender")), strs);
                ChatWriter.writeTo(Bukkit.getPlayer(hs.get("sender")), ChatWriterType.POSTMAN, "Added that line to your message.");
            }

        }
        return hs;
    }
}
