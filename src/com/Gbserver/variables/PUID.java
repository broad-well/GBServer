package com.Gbserver.variables;

import com.Gbserver.Utilities;

/**
 * Created by michael on 1/16/16.
 */
public class PUID {
    //8 character length
    public static final String AVAIL_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    private String uid;
    public PUID(String uid){
        //Check input
        if(uid.length() % 8 != 0) return; //Has to be multiple of 8
        for(char c : uid.toCharArray())
            if(!AVAIL_CHARACTERS.contains(String.valueOf(c))) return; //Invalid characters


        this.uid = uid;
    }

    public String toString(){
        return uid;
    }

    public boolean equals(Object anotherObject){
        return anotherObject instanceof PUID && ((PUID) anotherObject).uid.equals(uid);
    }

    public static PUID randomPUID(int size) {
        String uid = "";
        for(int i = 0; i < size; i++){
            uid += AVAIL_CHARACTERS.charAt(Utilities.getRandom(0, AVAIL_CHARACTERS.length()));
        }
        return new PUID(uid);
    }
}
