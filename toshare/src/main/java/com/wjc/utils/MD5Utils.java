package com.wjc.utils;

import org.apache.shiro.codec.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

public class MD5Utils {
        /**
         * MD5加密类
         * @param str 要加密的字符串
         * @return    加密后的字符串
         */
        public static String code(String str){
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(str.getBytes());
                byte[]byteDigest = md.digest();
                int i;
                StringBuilder buf = new StringBuilder("");
                for (byte b : byteDigest) {
                    i = b;
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }
                //32位加密
                return buf.toString();
                // 16位的加密
                //return buf.toString().substring(8, 24);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }

        }

        public static String generate(String password) {
            Random r = new Random();
            StringBuilder sb = new StringBuilder(16);
            sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
            int len = sb.length();
            if (len < 16) {
                for (int i = 0; i < 16 - len; i++) {
                    sb.append("0");
                }
            }
            String salt = sb.toString();
            password = md5Hex(password + salt);
            char[] cs = new char[48];
            for (int i = 0; i < 48; i += 3) {
                assert password != null;
                cs[i] = password.charAt(i / 3 * 2);
                char c = salt.charAt(i / 3);
                cs[i + 1] = c;
                cs[i + 2] = password.charAt(i / 3 * 2 + 1);
            }
            return new String(cs);
        }
        /**
         * 获取十六进制字符串形式的MD5摘要
         */
        private static String md5Hex(String src) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] bs = md5.digest(src.getBytes());
                return new String(new Hex().encode(bs));
            } catch (Exception e) {
                return null;
            }
        }
        public static boolean verify(String password, String md5) {
            char[] cs1 = new char[32];
            char[] cs2 = new char[16];
            for (int i = 0; i < 48; i += 3) {
                cs1[i / 3 * 2] = md5.charAt(i);
                cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
                cs2[i / 3] = md5.charAt(i + 1);
            }
            String salt = new String(cs2);
            return Objects.equals(md5Hex(password + salt), new String(cs1));
        }

        public static void main(String[] args) {
            System.out.println(code("123456"));
            System.out.println(generate("123456"));
        }
}

