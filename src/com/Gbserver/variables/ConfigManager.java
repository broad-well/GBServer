package com.Gbserver.variables;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * A ConfigManager for those who needs config.
 */
public class ConfigManager {
    private Path target;
    private int entrySize;
    private String separator = ",";
    private boolean doAutoSave = true;
    public HashMap<String, List<String>> data = new HashMap<>();

    /**
     * Creates a new instance of ConfigManager with default values of <code>separator = ","</code> and <code>doAutoSave = true</code>.
     * @param file
     * @param entries
     * @throws IOException
     */
    public ConfigManager(Path file, int entries) throws IOException {
        entrySize = entries;
        target = file;
        if(!target.toFile().exists()){
            target.toFile().createNewFile();
        }
        readData();
    }

    /**
     * Creates a new instance of ConfigManager with default values of <code>separator = ","</code>.
     * @param file
     * @param entries
     * @param autoSave
     * @throws IOException
     */
    public ConfigManager(Path file, int entries, boolean autoSave) throws IOException {
        entrySize = entries;
        target = file;
        doAutoSave = autoSave;
        if(!target.toFile().exists()){
            target.toFile().createNewFile();
        }
        readData();
    }

    /**
     * Creates a new instance of ConfigManager with default values of <code>doAutoSave = true</code>.
     * @param file
     * @param entries
     * @param separator
     * @throws IOException
     */
    public ConfigManager(Path file, int entries, String separator) throws IOException {
        entrySize = entries;
        target = file;
        this.separator = separator;
        if(!target.toFile().exists()){
            target.toFile().createNewFile();
        }
        readData();
    }

    /**
     * Creates a new instance of ConfigManager with all manual values.
     * @param file
     * @param entries
     * @param separator
     * @param autoSave
     * @throws IOException
     */
    public ConfigManager(Path file, int entries, String separator, boolean autoSave) throws IOException {
        entrySize = entries;
        target = file;
        this.separator = separator;
        doAutoSave = autoSave;
        if(!target.toFile().exists()){
            target.toFile().createNewFile();
        }
        readData();
    }

    /**
     * Adds an entry to the Config. If autosave is on, the ConfigManager will save the file in this method. The methods checks input for length and if it contains the selected separator.
     * You can ignore parameter <code>sep</code>. It is a separator between the entry's name and the entry's values. It can be null.
     * @param name
     * @param sep
     * @param data
     */
    public void add(String name, Object sep, String... data) throws IOException {
        if(data.length != entrySize || name.contains(separator)){
            return;
        }
        for(String s : data){
            if(s.contains(separator)){
                return;
            }
        }
        if(!this.data.containsKey(name)) {
            this.data.put(name, Arrays.asList(data));
        }
        if(doAutoSave){
            flush();
        }
    }



    /**
     * Saves the data in HashMap to file. It is automatically triggered by data changing functions when AutoSave is on.
     * @throws IOException
     */
    public void flush() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(target.toFile()));
        for(Map.Entry<String, List<String>> value : data.entrySet()){
            String thisOutput = value.getKey() + separator;
            //valueHeader^a^b^c^d^e^f^g
            for(String datavalue : value.getValue()){
                thisOutput += datavalue + separator;
            }
            //Needs to remove the last separator.
            thisOutput = thisOutput.substring(0,thisOutput.length()-1);
            writer.println(thisOutput);
        }
        writer.flush();
        writer.close();
    }

    /**
     * Reads the contents from the file. It is triggered when a substance of ConfigManager is created.
     * @throws IOException
     */
    public void readData() throws IOException {
        List<String> lines = Files.readAllLines(target, Charset.defaultCharset());
        for(String s : lines){
            List<String> content = new LinkedList<>();
            content.addAll(Arrays.asList(s.split(separator)));
            String header = content.get(0);
            content.remove(0);
            data.put(header, content);
        }
    }
    //STATIC
    public static Path getPathInsidePluginFolder(String name){
        return Paths.get("/home/michael/SpigotCache/plugins/Broadwell_Server_Plugin", name);
    }
}
