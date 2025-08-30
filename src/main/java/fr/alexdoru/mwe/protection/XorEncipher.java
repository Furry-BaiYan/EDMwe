package fr.alexdoru.mwe.protection;

import by.radioegor146.nativeobfuscator.Native;

import java.nio.charset.StandardCharsets;

/**
 * @Author:EventListener
 */
@SuppressWarnings("all")
@Native
public class XorEncipher {
    @Native
    public static byte[] Xor(byte[] rawData,int n){
        byte[] encodeData = new byte[rawData.length];
        for (int i = 0; i < rawData.length; i++) {
            encodeData[i] = (byte)(rawData[i]^n);
        }
        return encodeData;
    }
    @Native
    public static String EncryptionString(String str,String key){
        int number = 0;
        for (int i = 0; i < key.length(); i++) {
            number *= key.charAt(i);
        }
        byte[] rawStr = str.getBytes(StandardCharsets.UTF_8);
        byte[] encodeStr = Xor(rawStr,number);
        StringBuffer encoded = new StringBuffer();
        for (byte b : encodeStr) {
            encoded.append(b).append("x");
        }
        return encoded.toString();
    }
    @Native
    public static String DecodeByXor(String str,String key){
        int number = 0;
        for (int i = 0; i < key.length(); i++) {
            number *= key.charAt(i);
        }
        String[] strings = str.substring(0,str.length()-1).split("x");
        byte[] rawData = new byte[strings.length];
        for (int i = 0; i < strings.length; i++) {
            rawData[i] = Byte.parseByte(strings[i]);
        }
        byte[] encodeData = Xor(rawData,number);
        return new String(encodeData, StandardCharsets.UTF_8);
    }
}