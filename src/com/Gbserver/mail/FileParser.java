package com.Gbserver.mail;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ConfigManager;
import org.bukkit.OfflinePlayer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class FileParser {
    /*
    -={
    ---HEADER START---
    SENDER=
    RECEIVER=
    TIME=
    SUBJECT=
    READ=false/true
    ---HEADER END---
    Hello!

    Bye.
    }=- -> NEEDBUFFER
     */
    public static List<Message> msgs = new LinkedList<>();
    private static FileParser instance = new FileParser();
    public static FileParser getInstance(){return instance;}
    public void updateBuffer() throws IOException, ParseException {
        List<String> lines = Files.readAllLines(MailMan.datfile, Charset.defaultCharset());
        boolean in = false;
        msgs = new LinkedList<>();
        String currentEntry = "";
        for(String line : lines){
            switch (line.trim()) {
                case "-={":
                    if (in)
                        throw new ParseException("Divider entry inside a divider entry on line " + lines.indexOf(line), 1);
                    in = true;
                    currentEntry = "";
                    break;
                case "}=-":
                    if (!in)
                        throw new ParseException("Divider exit not inside a divider entry on line " + lines.indexOf(line), 1);
                    in = false;

                    msgs.add(Message.parseConfigEntry(currentEntry));
                    break;
                default:
                    if (in) {
                        currentEntry += line + "\n";
                    }
                    break;
            }

        }
    }

    public void saveBuffer() throws IOException {
        String output = "";
        for(Message msg : msgs)
            output += msg.getConfigEntry();
        Files.write(MailMan.datfile, output.getBytes(), StandardOpenOption.CREATE);
    }

    public List<Message> getMessagesOf(OfflinePlayer player){
        List<Message> output = new LinkedList<>();
        for(Message msg : msgs){
            if(msg.getReceiver().equals(player)){
                output.add(msg);
            }
        }
        return output;
    }
    public void letMessageRead(Message msg){
        /*if(!getAllStored().contains(msg)) {
            System.err.println("Void message! " + msg.getSender());
            return;
        }*/
        List<String> lines = null;
        try {
            lines = Files.readAllLines(MailMan.datfile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String line : lines)
            if(line.contains(msg.getUid().toString()))
                lines.set(lines.indexOf(line) + 1, "READ=true");
        //Trim!
        try {
            Files.write(MailMan.datfile, Utilities.ListToLines(lines).getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveMessage(Message msg) throws IOException {
        Files.write(MailMan.datfile, msg.getConfigEntry().getBytes(), StandardOpenOption.APPEND);
    }


    private static boolean hasOnlySpace(String str){
        return str.trim().length() == 0;
    }
}
