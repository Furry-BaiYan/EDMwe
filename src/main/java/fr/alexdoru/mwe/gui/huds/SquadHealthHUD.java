//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Deobfuscator3000\mappings"!

//Decompiled by Procyon!

package fr.alexdoru.mwe.gui.huds;

import fr.alexdoru.mwe.config.*;
import net.minecraft.client.network.*;
import fr.alexdoru.mwe.features.*;
import fr.alexdoru.mwe.asm.hooks.*;
import fr.alexdoru.mwe.scoreboard.*;
import fr.alexdoru.mwe.asm.interfaces.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import fr.alexdoru.mwe.utils.*;
import net.minecraft.client.*;
import net.minecraft.scoreboard.*;
import java.util.*;
import net.minecraft.entity.player.*;
import com.mojang.authlib.*;
import net.minecraft.client.resources.*;

public class SquadHealthHUD extends AbstractRenderer
{
    private static final ResourceLocation texture;

    public SquadHealthHUD() {
        super(MWEConfig.squadHUDPosition);
    }

    public void render(final ScaledResolution resolution) {
        final Scoreboard scoreboard = mc.theWorld.getScoreboard();
        final ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(0);
        if (mc.isIntegratedServerRunning() && scoreobjective == null) {
            return;
        }
        final List<NetworkPlayerInfo> list = new ArrayList<NetworkPlayerInfo>();
        for (final String squadmateName : SquadHandler.getSquad().keySet()) {
            final NetworkPlayerInfo netInfo = NetHandlerPlayClientHook_PlayerMapTracker.getPlayerInfo(squadmateName);
            if (netInfo != null) {
                list.add(netInfo);
            }
        }
        if (list.size() <= 1) {
            return;
        }
        final List<NetworkPlayerInfo> playerlistToRender = NameUtil.sortedCopyOf(list);
        int maxNameWidth = 0;
        int maxScoreWidth = 0;
        int maxFinalWidth = 0;
        int maxDistanceWidth = 0;
        int maxArrowWidth = 0;
        for (final NetworkPlayerInfo networkplayerinfo : playerlistToRender) {
            final EntityPlayer squadPlayer = mc.theWorld.getPlayerEntityByUUID(networkplayerinfo.getGameProfile().getId());
            maxNameWidth = Math.max(maxNameWidth, mc.fontRendererObj.getStringWidth(this.getPlayerName(networkplayerinfo)));
            if (scoreobjective != null && scoreobjective.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                maxScoreWidth = Math.max(maxScoreWidth, mc.fontRendererObj.getStringWidth(" " + scoreboard.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreobjective).getScorePoints()));
            }
            if (ScoreboardTracker.isInMwGame()) {
                final int playerFinalkills = ((NetworkPlayerInfoAccessor)networkplayerinfo).getFinalKills();
                if (playerFinalkills != 0) {
                    maxFinalWidth = Math.max(maxFinalWidth, mc.fontRendererObj.getStringWidth(" " + playerFinalkills));
                }
            }
            if (squadPlayer != null && squadPlayer != mc.thePlayer) {
                final double yourPosY = mc.thePlayer.posY;
                final double targetPosY = squadPlayer.posY;
                final int posYDifference = (int)Math.round(targetPosY - yourPosY);
                final String displayText = (posYDifference > 0) ? ("+" + Math.round(targetPosY - yourPosY) + ")") : ((posYDifference < 0) ? ("-" + Math.abs(posYDifference) + ")") : "0)");
                maxArrowWidth = 20;
                maxDistanceWidth = Math.max(maxDistanceWidth, mc.fontRendererObj.getStringWidth("(" + (int)squadPlayer.getDistanceToEntity((Entity)mc.thePlayer) + "m" + displayText));
            }
        }
        GlStateManager.pushMatrix();
        final boolean flag = mc.isIntegratedServerRunning() || mc.getNetHandler().getNetworkManager().getIsencrypted();
        final int maxLineWidth = (flag ? 9 : 0) + maxNameWidth + maxFinalWidth + maxScoreWidth + maxDistanceWidth + maxArrowWidth;
        final int listSize = playerlistToRender.size();
        final int hudWidth = maxLineWidth + 2;
        final int hudHight = listSize * 9 + 1;
        this.guiPosition.updateAdjustedAbsolutePosition(resolution, hudWidth, hudHight);
        final int hudXpos = this.guiPosition.getAbsoluteRenderX();
        final int hudYpos = this.guiPosition.getAbsoluteRenderY();
        Gui.drawRect(hudXpos, hudYpos, hudXpos + hudWidth, hudYpos + hudHight, Integer.MIN_VALUE);
        for (int i = 0; i < listSize; ++i) {
            int xDrawingPos = hudXpos + 1;
            final int yDrawingPos = hudYpos + 1 + i * 9;
            Gui.drawRect(xDrawingPos, yDrawingPos, hudXpos + maxLineWidth + 1, yDrawingPos + 8, 553648127);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final NetworkPlayerInfo netInfo2 = playerlistToRender.get(i);
            final GameProfile gameprofile = netInfo2.getGameProfile();
            if (flag) {
                final EntityPlayer entityplayer = mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
                mc.getTextureManager().bindTexture(netInfo2.getLocationSkin());
                Gui.drawScaledCustomSizeModalRect(xDrawingPos, yDrawingPos, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                if (entityplayer == null || entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                    Gui.drawScaledCustomSizeModalRect(xDrawingPos, yDrawingPos, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                }
                xDrawingPos += 9;
            }
            mc.fontRendererObj.drawStringWithShadow(this.getPlayerName(netInfo2), (float)xDrawingPos, (float)yDrawingPos, 16777215);
            final int xStartFinalDrawingPos = xDrawingPos + maxNameWidth + 1;
            final int xStartScoreDrawingPos = xStartFinalDrawingPos + maxFinalWidth;
            final int xStartDistanceDrawingPos = xStartScoreDrawingPos + maxScoreWidth + 1;
            if (maxScoreWidth + maxFinalWidth + maxDistanceWidth + maxArrowWidth > 5) {
                if (scoreobjective != null && netInfo2.getGameType() != WorldSettings.GameType.SPECTATOR && scoreobjective.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                    final int scorePoints = scoreobjective.getScoreboard().getValueFromObjective(gameprofile.getName(), scoreobjective).getScorePoints();
                    final String scoreString = ColorUtil.getColoredHP(EnumChatFormatting.YELLOW, scorePoints) + " " + scorePoints;
                    mc.fontRendererObj.drawStringWithShadow(scoreString, (float)xStartScoreDrawingPos, (float)yDrawingPos, 16777215);
                }
                if (ScoreboardTracker.isInMwGame()) {
                    final int playersFinals = ((NetworkPlayerInfoAccessor)netInfo2).getFinalKills();
                    if (playersFinals != 0) {
                        final String finalsString = EnumChatFormatting.GOLD + " " + playersFinals;
                        mc.fontRendererObj.drawStringWithShadow(finalsString, (float)xStartFinalDrawingPos, (float)yDrawingPos, 16777215);
                    }
                }
                final EntityPlayer target;
                if ((target = mc.theWorld.getPlayerEntityByUUID(gameprofile.getId())) != null && target != mc.thePlayer) {
                    final String distanceString = EnumChatFormatting.WHITE + "(" + EnumChatFormatting.LIGHT_PURPLE + (int)target.getDistanceToEntity(mc.thePlayer) + "m";
                    mc.fontRendererObj.drawStringWithShadow(distanceString, (float)xStartDistanceDrawingPos, (float)yDrawingPos, 16777215);
                    final double degrees = this.getDegrees(target);
                    final int arrowSize = 10;
                    final int arrowX = xStartDistanceDrawingPos + mc.fontRendererObj.getStringWidth(distanceString) + 2;
                    final float arrowY = yDrawingPos + mc.fontRendererObj.FONT_HEIGHT / 2.0f - 5.0f;
                    GlStateManager.pushMatrix();
                    GlStateManager.color(1.0f, 0.3f, 1.0f, 1.0f);
                    GlStateManager.translate(arrowX + 5.0f, arrowY + 5.0f, 0.0f);
                    GlStateManager.rotate((float)degrees - 90.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.translate(-5.0f, -5.0f, 0.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(SquadHealthHUD.texture);
                    Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, 10, 10, 10.0f, 10.0f);
                    GlStateManager.resetColor();
                    GlStateManager.popMatrix();
                    final double selfPosY = mc.thePlayer.posY;
                    final double targetPosY2 = target.posY;
                    final int posYDifference2 = (int)Math.round(targetPosY2 - selfPosY);
                    final String displayText2 = (posYDifference2 > 0) ? (EnumChatFormatting.GREEN + "+" + Math.round(targetPosY2 - selfPosY) + EnumChatFormatting.WHITE + ")") : ((posYDifference2 < 0) ? (EnumChatFormatting.RED + "-" + Math.abs(posYDifference2) + EnumChatFormatting.WHITE + ")") : (EnumChatFormatting.YELLOW + "0" + EnumChatFormatting.WHITE + ")"));
                    mc.fontRendererObj.drawStringWithShadow(displayText2, (float)(arrowX + 14), (float)yDrawingPos, 16777215);
                    GlStateManager.disableBlend();
                    GlStateManager.disableAlpha();
                }
            }
        }
        GlStateManager.popMatrix();
    }

    private String getPlayerName(final NetworkPlayerInfo netInfo) {
        final String name = NameUtil.getFormattedName(netInfo);
        return name.startsWith(NameUtil.SQUAD_ICON) ? name.substring(NameUtil.SQUAD_ICON.length()) : name;
    }

    private double getDegrees(final EntityPlayer player) {
        final double deltaX = player.posX - mc.thePlayer.posX;
        final double deltaZ = player.posZ - mc.thePlayer.posZ;
        final double angle = Math.atan2(deltaZ, deltaX);
        double degrees = Math.toDegrees(angle);
        degrees = (degrees + 360.0) % 360.0;
        degrees -= mc.thePlayer.rotationYaw;
        if (degrees < 0.0) {
            degrees += 360.0;
        }
        return degrees;
    }

    public void renderDummy() {
        GlStateManager.pushMatrix();
        final int hudXpos = this.guiPosition.getAbsoluteRenderX();
        final int hudYpos = this.guiPosition.getAbsoluteRenderY();
        final int listSize = 4;
        final int maxNameWidth = mc.fontRendererObj.getStringWidth(mc.thePlayer.getName());
        final int maxScoreWidth = mc.fontRendererObj.getStringWidth(" 00");
        final int maxFinalWidth = mc.fontRendererObj.getStringWidth(" 0");
        final int maxDistanceWidth = mc.fontRendererObj.getStringWidth(" 00000000");
        final int maxArrowWidth = mc.fontRendererObj.getStringWidth(" 00");
        final int maxLineWidth = maxNameWidth + maxFinalWidth + maxScoreWidth + maxDistanceWidth + maxArrowWidth + 1;
        Gui.drawRect(hudXpos, hudYpos, hudXpos + maxLineWidth + 2, hudYpos + 36 + 1, Integer.MIN_VALUE);
        for (int i = 0; i < 4; ++i) {
            int xDrawingPos = hudXpos + 1;
            final int yDrawingPos = hudYpos + 1 + i * 9;
            Gui.drawRect(xDrawingPos, yDrawingPos, hudXpos + maxLineWidth + 1, yDrawingPos + 8, 553648127);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            mc.getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkinLegacy());
            Gui.drawScaledCustomSizeModalRect(xDrawingPos, yDrawingPos, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
            Gui.drawScaledCustomSizeModalRect(xDrawingPos, yDrawingPos, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
            xDrawingPos += 9;
            final String formattedName = EnumChatFormatting.GREEN + mc.thePlayer.getName();
            mc.fontRendererObj.drawStringWithShadow(formattedName, (float)xDrawingPos, (float)yDrawingPos, -1);
            final int xStartFinalDrawingPos = xDrawingPos + maxNameWidth + 1;
            final int xStartScoreDrawingPos = xStartFinalDrawingPos + maxFinalWidth;
            final int xStartDistanceDrawingPos = xStartScoreDrawingPos + maxScoreWidth + 1;
            if (maxScoreWidth + maxFinalWidth + maxDistanceWidth + maxArrowWidth > 5) {
                final int scorePoints = 12 + (i * 18 + 22) % 8;
                final String scoreString = ColorUtil.getColoredHP(EnumChatFormatting.YELLOW, scorePoints) + " " + scorePoints;
                mc.fontRendererObj.drawStringWithShadow(scoreString, (float)xStartScoreDrawingPos, (float)yDrawingPos, 16777215);
                final String finalsString = EnumChatFormatting.GOLD + " " + (3 + (i * 28 + 15) % 5);
                mc.fontRendererObj.drawStringWithShadow(finalsString, (float)xStartFinalDrawingPos, (float)yDrawingPos, 16777215);
                final int dummyDistance = 12 + (i * 18 + 22) % 8;
                final int dummyDistanceY = 3 + (i * 28 + 15) % 5;
                final String distanceString = EnumChatFormatting.WHITE + "(" + EnumChatFormatting.LIGHT_PURPLE + dummyDistance + "m";
                mc.fontRendererObj.drawStringWithShadow(distanceString, (float)xStartDistanceDrawingPos, (float)yDrawingPos, 16777215);
                final int arrowSize = 10;
                final int arrowX = xStartDistanceDrawingPos + mc.fontRendererObj.getStringWidth(distanceString) + 2;
                final float arrowY = yDrawingPos + mc.fontRendererObj.FONT_HEIGHT / 2.0f - arrowSize / 2.0f;
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0f, 0.3f, 1.0f, 1.0f);
                GlStateManager.translate(arrowX + (float)(arrowSize / 2), arrowY + arrowSize / 2, 0.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate((float)(-arrowSize / 2), (float)(-arrowSize / 2), 0.0f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(SquadHealthHUD.texture);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, arrowSize, arrowSize, (float)arrowSize, (float)arrowSize);
                GlStateManager.resetColor();
                GlStateManager.popMatrix();
                final String distanceString2 = EnumChatFormatting.GREEN + "+" + dummyDistanceY + EnumChatFormatting.WHITE + ")";
                mc.fontRendererObj.drawStringWithShadow(distanceString2, (float)(arrowX + 14), (float)yDrawingPos, 16777215);
                GlStateManager.disableBlend();
                GlStateManager.disableAlpha();
            }
        }
        GlStateManager.popMatrix();
    }

    public boolean isEnabled(final long currentTimeMillis) {
        return this.guiPosition.isEnabled() && SquadHandler.getSquad().size() > 1;
    }

    static {
        texture = new ResourceLocation("squadhud:whitearrow.png");
    }
}
