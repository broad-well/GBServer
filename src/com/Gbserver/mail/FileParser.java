package com.Gbserver.mail;

import com.Gbserver.Utilities;
import org.bukkit.OfflinePlayer;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.HashMap;
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
    private Yaml amigo = new Yaml();
    public static FileParser getInstance(){return instance;}
    public void updateBuffer() throws IOException, ParseException {
        FileReader fr = new FileReader(MailMan.datfile.toFile());
        Object obj = amigo.load(fr);
        //List<HashMap<String, String>> is what we expect.
        if(obj instanceof List){
            msgs = new LinkedList<>();
            List<HashMap<String, String>> casted = (List<HashMap<String, String>>) obj;
            for(HashMap<String, String> entry : casted){
                msgs.add(Message.parseDump(entry));
            }
        }
    }

    public void saveBuffer() throws IOException {
        List<HashMap<String, String>> toBuild = new LinkedList<>();
        for(Message msg : msgs){
            toBuild.add(msg.getYamlDump());
        }
        FileWriter fw = new FileWriter(MailMan.datfile.toFile());
        amigo.dump(toBuild, fw);
        fw.flush();
        fw.close();
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
}
