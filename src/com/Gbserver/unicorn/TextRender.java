package com.Gbserver.unicorn;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.LinkedList;
import java.util.List;

public class TextRender {
    //Direction to render
    public static final int NORTH = 1;
    public static final int EAST = 2;
    public static final int SOUTH = 3;
    public static final int WEST = 4;

    public static void render(Font font, String text, Location starting, Material material, final int DIRECTION){
        System.out.println("Rendering");
        char[] textChars = text.toCharArray();
        List<int[][]> textPixelz = new LinkedList<>();
        for(char ch : textChars){
            int[][] pixels = font.getPixels(ch);
            if(pixels == null) return;
            textPixelz.add(font.getPixels(ch));
        }
        System.out.println(textPixelz);
        {
            FontWriter fontWriter = new FontWriter(starting, material, textPixelz.toArray(new int[1][1][1]), DIRECTION);
            fontWriter.writeText();
            fontWriter.close();
        }
        System.out.println("FontWriter done");
    }
}
