package fr.alexdoru.mwe.asm.hooks;

import fr.alexdoru.mwe.enums.MWClass;
import fr.alexdoru.mwe.features.PhxRespawnListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public final class GuiPlayerTabOverlayHook_PhxRespawnIcons {

    private static final String PHX_TAG = "[PHX]";
    private static final String ICON_SPACING = "  ";
    private static final int SOURCE_SIZE = 200;
    private static final int ICON_SIZE = 8;
    private static final ResourceLocation RESPAWNED_ICON = new ResourceLocation("phxres", "yes.png");
    private static final ResourceLocation NOT_RESPAWNED_ICON = new ResourceLocation("phxres", "no.png");

    private GuiPlayerTabOverlayHook_PhxRespawnIcons() {
    }

    public static String addIconSpacing(String playerName, NetworkPlayerInfo playerInfo) {
        if (playerName == null || playerInfo == null || !isPhoenix(playerInfo)) {
            return playerName;
        }
        final int phxTagEnd = getPhxTagEnd(playerName);
        if (phxTagEnd < 0 || playerName.startsWith(ICON_SPACING, phxTagEnd)) {
            return playerName;
        }
        return playerName.substring(0, phxTagEnd) + ICON_SPACING + playerName.substring(phxTagEnd);
    }

    public static void renderIcon(NetworkPlayerInfo playerInfo, String playerName, int x, int y) {
        if (playerInfo == null || playerName == null) {
            return;
        }
        final String profileName = playerInfo.getGameProfile().getName();
        if (!isPhoenix(playerInfo)) {
            return;
        }
        final int phxTagEnd = getPhxTagEnd(playerName);
        if (phxTagEnd < 0) {
            return;
        }

        final Minecraft mc = Minecraft.getMinecraft();
        final int iconX = x + mc.fontRendererObj.getStringWidth(playerName.substring(0, phxTagEnd));
        final ResourceLocation icon = PhxRespawnListener.isRespawned(profileName) ? RESPAWNED_ICON : NOT_RESPAWNED_ICON;

        mc.getTextureManager().bindTexture(icon);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Gui.drawScaledCustomSizeModalRect(iconX, y, 0.0F, 0.0F, SOURCE_SIZE, SOURCE_SIZE, ICON_SIZE, ICON_SIZE, SOURCE_SIZE, SOURCE_SIZE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static int getPhxTagEnd(String playerName) {
        int matchedCharacters = 0;
        for (int i = 0; i < playerName.length(); i++) {
            final char current = playerName.charAt(i);
            if (current == '\u00A7' && i + 1 < playerName.length()) {
                i++;
                continue;
            }

            final char normalized = Character.toUpperCase(current);
            if (normalized == PHX_TAG.charAt(matchedCharacters)) {
                matchedCharacters++;
                if (matchedCharacters == PHX_TAG.length()) {
                    return i + 1;
                }
            } else {
                matchedCharacters = normalized == PHX_TAG.charAt(0) ? 1 : 0;
            }
        }
        return -1;
    }

    private static boolean isPhoenix(NetworkPlayerInfo playerInfo) {
        final String name = playerInfo.getGameProfile().getName();
        if (PhxRespawnListener.isPhoenix(name)) {
            return true;
        }
        return playerInfo.getPlayerTeam() != null && MWClass.fromTeamTag(playerInfo.getPlayerTeam().getColorSuffix()) == MWClass.PHOENIX;
    }
}
