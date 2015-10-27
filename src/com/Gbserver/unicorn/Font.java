package com.Gbserver.unicorn;

import java.util.UUID;

public class Font {
    //                                   0~9       " "    , . ? !      A~Z
    //                                   NUMBERS   SPACE  PUNCTUATION  LETTERS
    public static final int totalChars = 10      + 1    + 4          + 26;
    public static final char[] ALL_CHARS = {'0','1','2','3','4','5','6','7','8','9',
            ' ',
            ',','.','?','!',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
    };


    private FontChar[] chars;
    private UUID uniqueId;
    private String name;

    public Font(String name, FontChar... characters){
        if(characters.length != totalChars) return;
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
