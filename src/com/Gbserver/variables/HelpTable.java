package com.Gbserver.variables;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpTable {
	public String usage;
	public String purpose;
	public String aliases;
	public String command;
	
	public HelpTable(String u, String p, String a, String c){
		usage = u;
		purpose = p;
		aliases = a;
		command = c;
	}
	
	public void show(CommandSender cs){
		String[] m = new String[3];
		m[0] = ChatColor.DARK_GRAY + "------Help for /" + command + "------";
		if (!(usage.equals(""))) {
			m[1] = ChatWriter.getMessage(ChatWriterType.HELP, "Usage: " + usage);
		}
		if (!(purpose.equals(""))) {
			m[2] = ChatWriter.getMessage(ChatWriterType.HELP, "Purpose: " + purpose);
		}
		if (!(aliases.equals(""))) {
			m[3] = ChatWriter.getMessage(ChatWriterType.HELP, "Aliases: " + aliases);
		}
		cs.sendMessage(m);
	}
}
