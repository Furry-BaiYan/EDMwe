//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Deobfuscator3000\mappings"!

//Decompiled by Procyon!

package fr.alexdoru.mwe.gui.huds;

import fr.alexdoru.mwe.config.*;
import fr.alexdoru.mwe.features.*;
import net.minecraft.client.renderer.*;
import fr.alexdoru.mwe.utils.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import java.util.*;
import net.minecraft.client.resources.*;
import fr.alexdoru.mwe.scoreboard.*;

public class PlayerCompassHUD extends AbstractRenderer
{
    private static final ResourceLocation texture;
    
    public PlayerCompassHUD() {
        super(MWEConfig.compassHUDPosition);
    }
    
    public void render(final ScaledResolution resolution) {
        final String[] teamColors = { FinalKillCounter.getColorPrefixFromTeam(0), FinalKillCounter.getColorPrefixFromTeam(1), FinalKillCounter.getColorPrefixFromTeam(2), FinalKillCounter.getColorPrefixFromTeam(3) };
        this.guiPosition.updateAbsolutePosition(resolution);
        final int hudXpos = this.guiPosition.getAbsoluteRenderX();
        int hudYpos = this.guiPosition.getAbsoluteRenderY();
        GlStateManager.pushMatrix();
        for (final String teamColor : teamColors) {
            double closestDistance = Double.MAX_VALUE;
            String closestPlayerName = null;
            EntityPlayer closestEntity = null;
            for (final EntityPlayer otherPlayer : PlayerCompassHUD.mc.theWorld.playerEntities) {
                final String displayName = NameUtil.getFormattedNameMisc(otherPlayer.getName());
                if (displayName.contains(teamColor) && otherPlayer != PlayerCompassHUD.mc.thePlayer) {
                    final double distance;
                    if ((distance = PlayerCompassHUD.mc.thePlayer.getDistanceToEntity((Entity)otherPlayer)) >= closestDistance) {
                        continue;
                    }
                    closestDistance = distance;
                    closestPlayerName = displayName;
                    closestEntity = otherPlayer;
                }
            }
            if (closestPlayerName != null) {
                final int iconSize = 8;
                final int textXOffset = hudXpos + 8 + 6;
                final String info = teamColor + String.format("%.0f", closestDistance) + EnumChatFormatting.RESET;
                PlayerCompassHUD.mc.fontRendererObj.drawStringWithShadow(info, (float)textXOffset, (float)hudYpos, -1);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                final GameProfile gameProfile = closestEntity.getGameProfile();
                final AbstractClientPlayer abstractClientPlayer = new AbstractClientPlayer(PlayerCompassHUD.mc.theWorld, gameProfile) {};
                PlayerCompassHUD.mc.getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
                Gui.drawScaledCustomSizeModalRect(hudXpos, hudYpos, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                if (closestEntity.isWearing(EnumPlayerModelParts.HAT)) {
                    Gui.drawScaledCustomSizeModalRect(hudXpos, hudYpos, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                }
                final double degrees = this.getDegrees(closestEntity);
                final int arrowSize = 12;
                final int arrowX = textXOffset + PlayerCompassHUD.mc.fontRendererObj.getStringWidth(info) + 4;
                final float arrowY = hudYpos + PlayerCompassHUD.mc.fontRendererObj.FONT_HEIGHT / 2.0f - arrowSize / 2.0f;
                GlStateManager.pushMatrix();
                this.getArrowColor(teamColor);
                GlStateManager.translate((float)(arrowX + arrowSize / 2), arrowY + arrowSize / 2, 0.0f);
                GlStateManager.rotate((float)degrees - 90.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate((float)(-arrowSize / 2), (float)(-arrowSize / 2), 0.0f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(PlayerCompassHUD.texture);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, arrowSize, arrowSize, (float)arrowSize, (float)arrowSize);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.resetColor();
                GlStateManager.popMatrix();
                final double selfPosY = PlayerCompassHUD.mc.thePlayer.posY;
                final double otherPosY = closestEntity.posY;
                final int posYDifference = (int)Math.round(otherPosY - selfPosY);
                final String displayText = (posYDifference > 0) ? (EnumChatFormatting.GRAY + "+" + posYDifference) : ((posYDifference < 0) ? (EnumChatFormatting.GRAY + "-" + Math.abs(posYDifference)) : (EnumChatFormatting.GRAY + "0"));
                PlayerCompassHUD.mc.fontRendererObj.drawStringWithShadow(displayText, (float)(arrowX + 16), (float)hudYpos, -1);
                hudYpos += 11;
            }
        }
        GlStateManager.popMatrix();
    }
    
    private double getDegrees(final EntityPlayer player) {
        final double deltaX = player.posX - PlayerCompassHUD.mc.thePlayer.posX;
        final double deltaZ = player.posZ - PlayerCompassHUD.mc.thePlayer.posZ;
        final double angle = Math.atan2(deltaZ, deltaX);
        double degrees = Math.toDegrees(angle);
        degrees = (degrees + 360.0) % 360.0;
        degrees -= PlayerCompassHUD.mc.thePlayer.rotationYaw;
        if (degrees < 0.0) {
            degrees += 360.0;
        }
        return degrees;
    }
    
    public void renderDummy() {
        GlStateManager.pushMatrix();
        final String[] teamColors = { EnumChatFormatting.RED.toString(), EnumChatFormatting.GREEN.toString(), EnumChatFormatting.YELLOW.toString(), EnumChatFormatting.BLUE.toString() };
        final int hudXpos = this.guiPosition.getAbsoluteRenderX();
        int hudYpos = this.guiPosition.getAbsoluteRenderY();
        for (final String teamColor : teamColors) {
            final double closestDistance = 0.0;
            final int textXOffset = hudXpos + 14;
            final String info = ((teamColor != null) ? teamColor : "") + String.format("%.0f", closestDistance) + EnumChatFormatting.RESET;
            PlayerCompassHUD.mc.fontRendererObj.drawStringWithShadow(info, (float)textXOffset, (float)hudYpos, -1);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            PlayerCompassHUD.mc.getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkinLegacy());
            Gui.drawScaledCustomSizeModalRect(hudXpos, hudYpos, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
            Gui.drawScaledCustomSizeModalRect(hudXpos, hudYpos, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
            final int arrowSize = 12;
            final int arrowX = textXOffset + PlayerCompassHUD.mc.fontRendererObj.getStringWidth(info) + 4;
            final float arrowY = hudYpos + PlayerCompassHUD.mc.fontRendererObj.FONT_HEIGHT / 2.0f - 6.0f - 1.0f;
            GlStateManager.pushMatrix();
            this.getArrowColor(teamColor);
            GlStateManager.translate((float)(arrowX + 6), arrowY + 6.0f, 0.0f);
            GlStateManager.translate(-6.0f, -6.0f, 0.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(PlayerCompassHUD.texture);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, 12, 12, 12.0f, 12.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.resetColor();
            GlStateManager.popMatrix();
            final String displayText = EnumChatFormatting.GRAY + "+2";
            PlayerCompassHUD.mc.fontRendererObj.drawStringWithShadow(displayText, arrowX + 16.0f, (float)hudYpos, -1);
            hudYpos += 11;
        }
        GlStateManager.popMatrix();
    }
    
    public boolean isEnabled(final long currentTimeMillis) {
        return this.guiPosition.isEnabled() && ScoreboardTracker.isInMwGame();
    }
    
    private void getArrowColor(final String str) {
        if (str.charAt(1) == '9') {
            GlStateManager.color(0.3f, 0.3f, 1.0f, 1.0f);
        }
        else if (str.charAt(1) == 'a') {
            GlStateManager.color(0.3f, 1.0f, 0.3f, 1.0f);
        }
        else if (str.charAt(1) == 'c') {
            GlStateManager.color(1.0f, 0.3f, 0.3f, 1.0f);
        }
        else if (str.charAt(1) == 'e') {
            GlStateManager.color(1.0f, 1.0f, 0.3f, 1.0f);
        }
    }
    
    static {
        texture = new ResourceLocation("squadhud:whitearrow.png");
    }
}
