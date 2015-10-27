package com.Gbserver.unicorn;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by michael on 10/25/15.
 */
public class Fonts {
    public static List<Font> fonts = new LinkedList<>();

    public static void removeFont(UUID uid){
        for(Font f : fonts){
            if(f.getUUID().equals(uid)){
                fonts.remove(f);
                return;
            }
        }
    }

    public static void removeFont(String name){
        for(Font f : fonts){
            if(f.getName().equals(name)){
                fonts.remove(f);
                return;
            }
        }
    }

    public static FontChar buildFontChar(final char data, final int[][] value){
        if(value.length != 8) return null;
        for(int[] array : value){
            if(array.length != 8){
                return null;
            }
        }
        return new FontChar() {
            @Override
            public int[][] getData() {
                return value;
            }

            @Override
            public char getChar() {
                return data;
            }
        };
    }

    public static Font interpretPackage(String name, int[][][] Package) {
        List<FontChar> chars = new LinkedList<>();
        for(int i = 0; i < Font.totalChars; i++){
            chars.add(buildFontChar(Font.ALL_CHARS[i],Package[i]));
        }
            return new Font(name, chars.toArray(new FontChar[1]));

    }
}
