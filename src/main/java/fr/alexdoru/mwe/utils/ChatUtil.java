package fr.alexdoru.mwe.utils;

import fr.alexdoru.mwe.Wrapper;
import net.minecraft.util.ChatComponentText;

public class ChatUtil implements Wrapper {
    public static void addMessage(String string) {
        mc.thePlayer.addChatMessage(new ChatComponentText(string));
    }
}
