package fr.alexdoru.mwe.utils;

import fr.alexdoru.mwe.Wrapper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtil implements Wrapper {
    public static void addMessage(String string) {
        mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.AQUA + "EDMwe" + EnumChatFormatting.GRAY + "] " + string));
    }
}
