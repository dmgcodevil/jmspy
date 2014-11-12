package com.github.dmgcodevil.jmspy.example.prototype;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

/**
 * Created by dmgcodevil on 11/12/2014.
 */
public class Check {
    private ExtMap<String, String, String> extMap;


    public static void main(String[] args) throws NoSuchFieldException {
        java.lang.reflect.Type rtype = Check.class.getDeclaredField("extMap").getGenericType();
        if (rtype instanceof ParameterizedType) {
            ParameterizedType genericType = (ParameterizedType) rtype;
            if (genericType.getActualTypeArguments().length > 0) {
                System.out.println(Arrays.toString(genericType.getActualTypeArguments()));
            }
        }
    }
}
