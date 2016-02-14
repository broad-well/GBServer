package com.Gbserver.variables.nms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by michael on 2/2/16.
 */
public class ReflectUtils {
    public static Field getPrivateField(Class patient, String fieldName){
        try {
            Field f = patient.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Method getPrivateMethod(Class patient, String methodName){
        try{
            Method m = patient.getDeclaredMethod(methodName);
            m.setAccessible(true);
            return m;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
