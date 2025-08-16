package fr.alexdoru.mwe.utils;

import fr.alexdoru.mwe.Wrapper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class LogUtil implements Wrapper {
    public static void addMessage(String string) {
        mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "[" + EnumChatFormatting.AQUA + "EDMwe" + EnumChatFormatting.WHITE + "] " + string));
    }
    public static void addcfgmessage(String string) {
        mc.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s»%s %s",EnumChatFormatting.BOLD,EnumChatFormatting.GRAY,EnumChatFormatting.RESET,string)));
    }
}
