package com.wjc.utils;

import java.util.Random;

public class SaltUtils {
    public static String getSalt(int n){
        char[] chars= "ABCDEFGHIJKLMNOPGRSTUVWXYZabcdefghijklmnopgrstuvwxyz0123456789!@#$%&'^()".toCharArray();
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<n;i++){
            char aChar=chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getSalt(8));
    }
}
