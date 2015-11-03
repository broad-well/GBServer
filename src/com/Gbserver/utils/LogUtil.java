package com.Gbserver.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.ConsoleCommandSender;

public class LogUtil {

    private static HashMap<Integer, String> Logs = new HashMap();
    private static Integer i = Integer.valueOf(0);

    public static void LogToConsole(String message) {
        ConsoleCommandSender console = org.bukkit.Bukkit.getConsoleSender();
        console.sendMessage(message);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String date = df.format(today);
        Integer localInteger1;
        if ((message != null) && (!message.equalsIgnoreCase(""))) {
            Logs.put(i, "[" + date + "]: " + message);
            localInteger1 = i; Integer localInteger2 = i = Integer.valueOf(i.intValue() + 1);
        }
    }

    public static void ClearLogs() {
        i = Integer.valueOf(0);
        Logs.clear();
    }

}
