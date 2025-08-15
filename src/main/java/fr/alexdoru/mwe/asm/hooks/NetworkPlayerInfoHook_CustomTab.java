package fr.alexdoru.mwe.asm.hooks;

import com.mojang.authlib.GameProfile;
import fr.alexdoru.mwe.features.FinalKillCounter;
import fr.alexdoru.mwe.features.PhxRespawnListener;
import fr.alexdoru.mwe.utils.NameUtil;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

@SuppressWarnings("unused")
public class NetworkPlayerInfoHook_CustomTab {

    public static IChatComponent getDisplayName(GameProfile gameProfileIn) {
        return PhxRespawnListener.isRespawned(gameProfileIn.getName()) ?
                new ChatComponentText(EnumChatFormatting.RED + "⚔ " + EnumChatFormatting.RESET + NameUtil.getMWPlayerData(gameProfileIn, false).displayName.getFormattedText()) :
                NameUtil.getMWPlayerData(gameProfileIn, false).displayName;
    }

    public static int getPlayersFinals(String playername) {
        return FinalKillCounter.getPlayersFinals(playername);
    }

    public static IChatComponent changeDisplayName(IChatComponent original, IChatComponent customName) {
        return original != null ? original : customName;
    }

}
