package com.Gbserver.variables.minigame;

/**
 * Created by michael on 2/6/16.
 */
public enum Games {
    RUNNER("Runner");
    String ident;

    Games(String ident) {
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    public static Games fromString(String ident) {
        for (Games g : values()) {
            if (g.getIdent().equalsIgnoreCase(ident)) return g;
        }
        return null;
    }
}
