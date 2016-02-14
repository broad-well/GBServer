package com.Gbserver.variables;

import java.util.HashMap;

/**
 * Created by michael on 1/16/16.
 */
public class Preferences {
    public static HashMap<String, String> get() {
        return ConfigManager.smartGet("Preferences");
    }
}
