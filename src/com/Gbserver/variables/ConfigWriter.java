package com.Gbserver.variables;

import com.Gbserver.listener.Announce;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by michael on 10/10/15.
 */
public class ConfigWriter {
    private FileConfiguration fc;

    public ConfigWriter() {
        fc = Announce.getPlugin().getConfig();
    }

    public void write(String path, Object value) {
        fc.set(path, value);
    }

    public Object read(String path) {
        return fc.get(path);
    }

    public void closeUp() {
        fc = null;
    }

}

