package com.Gbserver.unicorn;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.UUID;

public class Font {
    //                                   0~9       " "    , . ? !      A~Z
    //                                   NUMBERS   SPACE  PUNCTUATION  LETTERS
    public static final int totalChars = 10      + 1    + 4          + 26;

    private FontChar[] chars;
    private UUID uniqueId;
    private String name;

    public Font(String name, FontChar... characters) throws InvalidArgumentException {
        if(characters.length != totalChars) throw new InvalidArgumentException(new String[1]);
        chars = characters;
        this.name = name;
        uniqueId = UUID.randomUUID();
        Fonts.fonts.add(this);
    }

    public int[][] getPixels(char character){
        for(FontChar fc : chars){
            if(fc.getChar() == character){
                return fc.getData();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uniqueId;
    }
    public void close() {
        Fonts.fonts.remove(this);
        chars = null;
        name = null;
        uniqueId = null;
    }
}
