package com.Gbserver.variables;

/**
 * Created by michael on 1/30/16.
 */
public class DebugLevel {
    public static boolean isApplicable(int level){
        return level
                <= Integer.parseInt(Preferences.get().get("Debug"));
    }
    private int level;
    private String name;

    public DebugLevel(int level, String name){
        this.level = level;
        this.name = name;
    }
    public void debugWrite(String text){
        debugWrite(level, text);
    }
    public void debugWrite(int level, String text){
        if(isApplicable(level)){
            System.out.println(name + ": " + text);
        }
    }
}
