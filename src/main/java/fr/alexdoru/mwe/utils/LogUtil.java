package fr.alexdoru.mwe.utils;

import fr.alexdoru.mwe.Wrapper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import static fr.alexdoru.mwe.MWE.nullcheck;

public class LogUtil implements Wrapper {
    public static void addMessage(String string) {
        mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "[" + EnumChatFormatting.AQUA + "EDMwe" + EnumChatFormatting.WHITE + "] " + string));
    }
    public static void addcfgmessage(String string) {
        mc.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s»%s %s",EnumChatFormatting.BOLD,EnumChatFormatting.GRAY,EnumChatFormatting.RESET,string)));
    }

    public static void log(String string) {
        if (nullcheck()) {
            System.out.println(string);
            return;
        }
        mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "[" + EnumChatFormatting.AQUA + "EDMwe" + EnumChatFormatting.WHITE + "] " + string));
    }

    /**
     * SendMessage With IRCPrefix
     * @param string
     */
    public static void sendIRCMessage(String string) {
        if (nullcheck()) {
            System.out.println(String.format("%s[%sIRC%s]%s",EnumChatFormatting.GRAY,EnumChatFormatting.AQUA,EnumChatFormatting.GRAY,EnumChatFormatting.RESET) + string);
            return;
        }
        mc.thePlayer.addChatMessage(new ChatComponentText(String.format("%s[%sIRC%s]%s",EnumChatFormatting.GRAY,EnumChatFormatting.AQUA,EnumChatFormatting.GRAY,EnumChatFormatting.RESET) + string));
    }
}
