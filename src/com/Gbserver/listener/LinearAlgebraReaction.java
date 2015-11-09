package com.Gbserver.listener;

import com.Gbserver.Utilities;
import com.Gbserver.variables.ReactionMath;

import java.util.HashMap;

/**
 * Created by michael on 11/6/15.
 */
public class LinearAlgebraReaction implements ReactionMath{
    private static final int EASY = 1; // 5x - 2 = 23
    private static final int SOSO = 2; // 7x - 15 = 3x - 4
    private static final int HARD = 3; // sqrt(3x)=sqrt(x)+15.74
    //----------
    private int difficulty;

    @Override
    public Number calculate() {
        return null;
    }

    @Override
    public LinearAlgebraReaction generate() {
        difficulty = Utilities.getRandom(1,3);
        //Detachable ints
        switch(difficulty){
            case EASY:

                break;
            case SOSO:
                break;
            case HARD:
                break;
        }
        return null;
    }

    @Override
    public void broadcast() {

    }

    @Override
    public void close() {

    }

    //-------PRIVATE

    private void getElements() {

    }
}
class Operator {
    public static final int PLS = 1;
    public static final int SBT = 2;
    public static final int MTP = 3;
    public static final int DVD = 4;
    public static final int SQR = 5;
    public static final int POW = 6;
    public static HashMap<Integer, String> formatting = new HashMap<Integer, String>(){{
        put(PLS,"+");
        put(SBT,"-");
        put(MTP,"");
        put(DVD,"/");
        put(SQR,"sqrt:");
        put(POW,"^");
    }};
}