package com.Gbserver.variables;

import java.awt.*;

/**
 * Created by michael on 10/31/15.
 */
public enum TeamColor {
    BLUE,RED;
    public String toString() {
        switch(this){
            case BLUE: return "Blue";
            case RED: return "Red";
        }
        return null;
    }

    public Color toColor() {
        switch(this){
            case BLUE: return Color.BLUE;
            case RED: return Color.RED;
        }
        return null;
    }

    //--

    public static TeamColor fromColor(Color c){
        if(c == Color.BLUE) return BLUE;
        if(c == Color.RED) return RED;
        return null;
    }
}
