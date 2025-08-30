package fr.alexdoru.mwe.protection;

import by.radioegor146.nativeobfuscator.Native;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import java.io.*;
import java.util.Scanner;
@Native
public class VerificationUtils {
    @Native
    public static String getHwid(){
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            sc.next();
            String serial = sc.next();
            return serial;
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    @Native
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
    @Native
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
    @Native
    public static void sendMsg(ChannelHandlerContext ctx, String str) {
        ctx.writeAndFlush(Unpooled.copiedBuffer((fr.alexdoru.mwe.protection.XorEncipher.EncryptionString((str + ":"),"Obsidian.dev")).getBytes()));
    }
}
