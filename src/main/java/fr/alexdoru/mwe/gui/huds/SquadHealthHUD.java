//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Deobfuscator3000\mappings"!

//Decompiled by Procyon!

package fr.alexdoru.mwe.gui.huds;

import fr.alexdoru.mwe.asm.hooks.GuiPlayerTabOverlayHook_ColoredScores;
import fr.alexdoru.mwe.asm.hooks.NetHandlerPlayClientHook_PlayerMapTracker;
import fr.alexdoru.mwe.asm.interfaces.NetworkPlayerInfoAccessor;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.features.SquadHandler;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import fr.alexdoru.mwe.utils.ColorUtil;
import fr.alexdoru.mwe.utils.NameUtil;
import net.minecraft.client.network.*;

import java.util.List;
import java.util.regex.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import net.minecraft.client.*;
import com.mojang.authlib.*;
import net.minecraft.entity.player.*;
import java.util.*;
import net.minecraftforge.fml.relauncher.*;
import com.google.common.collect.*;
import net.minecraft.scoreboard.*;

public class SquadHealthHUD extends AbstractRenderer
{
    public static SquadHealthHUD instance;
    private static final Ordering<NetworkPlayerInfo> ordering;
    private static ResourceLocation texture;
    private static final Pattern squadSuffixPattern;
    
    public SquadHealthHUD() {
        super(MWEConfig.squadHUDPosition);
        SquadHealthHUD.instance = this;
    }
    
    public void render(final ScaledResolution resolution) {
        final Scoreboard scoreboard = mc.theWorld.getScoreboard();
        final ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(0);
        if (mc.isIntegratedServerRunning() && scoreobjective == null) {
            return;
        }
        final List<NetworkPlayerInfo> list = new ArrayList<NetworkPlayerInfo>();
        for (final String squadmateName : SquadHandler.getSquad().keySet()) {
            final NetworkPlayerInfo networkPlayerInfo = NetHandlerPlayClientHook_PlayerMapTracker.getPlayerInfo(squadmateName);
            if (networkPlayerInfo != null) {
                list.add(networkPlayerInfo);
            }
        }
        if (list.size() <= 1) {
            return;
        }
        int maxNameWidth = 0;
        int maxScoreWidth = 0;
        int maxDistanceWidth = 0;
        int maxArrowWidth = 0;
        int maxFinalWidth = 0;
        final List<NetworkPlayerInfo> playerlistToRender = new ArrayList<NetworkPlayerInfo>(SquadHealthHUD.ordering.sortedCopy(list));
        for (final NetworkPlayerInfo networkplayerinfo : playerlistToRender) {
            final GameProfile gameprofile1 = networkplayerinfo.getGameProfile();
            final EntityPlayer player1 = mc.theWorld.getPlayerEntityByUUID(gameprofile1.getId());
            maxNameWidth = Math.max(maxNameWidth, mc.fontRendererObj.getStringWidth(this.getPlayerName(networkplayerinfo)));
            if (scoreobjective != null && scoreobjective.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                maxScoreWidth = Math.max(maxScoreWidth, mc.fontRendererObj.getStringWidth(" " + scoreboard.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreobjective).getScorePoints()));
            }
            else if (player1 != null) {
                final int health = (int)(player1.getHealth() + player1.getAbsorptionAmount());
                maxScoreWidth = Math.max(maxScoreWidth, mc.fontRendererObj.getStringWidth(" " + GuiPlayerTabOverlayHook_ColoredScores.getColoredHP(EnumChatFormatting.GREEN,health) + health));
            }
            if (ScoreboardTracker.isInMwGame) {
                final int playerFinalkills = ((NetworkPlayerInfoAccessor)networkplayerinfo).getFinalKills();
                if (playerFinalkills != 0) {
                    maxFinalWidth = Math.max(maxFinalWidth, mc.fontRendererObj.getStringWidth(" " + playerFinalkills + " "));
                }
            }
            if (player1 != null && player1 != mc.thePlayer) {
                final String distanceString = EnumChatFormatting.RESET + "(" + EnumChatFormatting.LIGHT_PURPLE + (int)player1.getDistanceToEntity(mc.thePlayer) + "m " + getColor(Math.round(player1.posY - mc.thePlayer.posY)) + Math.round(player1.posY - mc.thePlayer.posY) + EnumChatFormatting.RESET + ")";
                maxArrowWidth = Math.max(maxArrowWidth, 12);
                maxDistanceWidth = Math.max(maxDistanceWidth, mc.fontRendererObj.getStringWidth(distanceString));
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
            final NetworkPlayerInfo networkplayerinfo2 = playerlistToRender.get(i);
            final String formattedName = this.getPlayerName(networkplayerinfo2);
            final GameProfile gameprofile2 = networkplayerinfo2.getGameProfile();
            final EntityPlayer entityplayer = mc.theWorld.getPlayerEntityByUUID(gameprofile2.getId());
            if (flag) {
                mc.getTextureManager().bindTexture(networkplayerinfo2.getLocationSkin());
                Gui.drawScaledCustomSizeModalRect(xDrawingPos, yDrawingPos, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                if (entityplayer == null || entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                    Gui.drawScaledCustomSizeModalRect(xDrawingPos, yDrawingPos, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                }
                xDrawingPos += 9;
            }
            mc.fontRendererObj.drawStringWithShadow(formattedName, (float)xDrawingPos, (float)yDrawingPos, 16777215);
            final int xStartFinalDrawingPos = xDrawingPos + maxNameWidth + 1;
            final int xDistanceDrawingPos = xStartFinalDrawingPos + maxFinalWidth;
            final int xStartScoreDrawingPos = xStartFinalDrawingPos + maxFinalWidth + maxDistanceWidth + maxArrowWidth;
            if (maxScoreWidth + maxFinalWidth + maxDistanceWidth + maxArrowWidth > 5) {
                if (scoreobjective != null && networkplayerinfo2.getGameType() != WorldSettings.GameType.SPECTATOR && scoreobjective.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                    final int scorePoints = scoreobjective.getScoreboard().getValueFromObjective(gameprofile2.getName(), scoreobjective).getScorePoints();
                    final String scoreString = GuiPlayerTabOverlayHook_ColoredScores.getColoredHP(EnumChatFormatting.GREEN,scorePoints) + " " + scorePoints;
                    mc.fontRendererObj.drawStringWithShadow(scoreString, (float)xStartScoreDrawingPos, (float)yDrawingPos, 16777215);
                }
                else if (entityplayer != null) {
                    final int health2 = (int)(entityplayer.getHealth() + entityplayer.getAbsorptionAmount());
                    final String hpString = GuiPlayerTabOverlayHook_ColoredScores.getColoredHP(EnumChatFormatting.GREEN,health2) + " " + health2;
                    mc.fontRendererObj.drawStringWithShadow(hpString, (float)xStartScoreDrawingPos, (float)yDrawingPos, 16777215);
                }
                if (ScoreboardTracker.isInMwGame) {
                    final int playersFinals = ((NetworkPlayerInfoAccessor)networkplayerinfo2).getFinalKills();
                    if (playersFinals != 0) {
                        final String finalsString = EnumChatFormatting.GOLD + " " + playersFinals + " ";
                        mc.fontRendererObj.drawStringWithShadow(finalsString, (float)xStartFinalDrawingPos, (float)yDrawingPos, 16777215);
                    }
                }
                if (entityplayer != null && entityplayer != mc.thePlayer) {
                    final String distanceString2 = EnumChatFormatting.RESET + "(" + EnumChatFormatting.LIGHT_PURPLE + (int)entityplayer.getDistanceToEntity(mc.thePlayer) + "m " + getColor(Math.round(entityplayer.posY - mc.thePlayer.posY)) + Math.round(entityplayer.posY - mc.thePlayer.posY) + EnumChatFormatting.RESET + ")";
                    mc.fontRendererObj.drawStringWithShadow(distanceString2, (float)xDistanceDrawingPos, (float)yDrawingPos, 16777215);
                    final double deltaX = entityplayer.posX - mc.thePlayer.posX;
                    final double deltaZ = entityplayer.posZ - mc.thePlayer.posZ;
                    final double angle = Math.atan2(deltaZ, deltaX);
                    double degrees = Math.toDegrees(angle);
                    degrees = (degrees + 360.0) % 360.0;
                    degrees -= mc.thePlayer.rotationYaw;
                    if (degrees < 0.0) {
                        degrees += 360.0;
                    }
                    final int arrowSize = 8;
                    final int arrowX = xDistanceDrawingPos + mc.fontRendererObj.getStringWidth(distanceString2) + 2;
                    final float arrowY = (float)(yDrawingPos + mc.fontRendererObj.FONT_HEIGHT / 2 - arrowSize / 2);
                    GL11.glPushMatrix();
                    GL11.glDisable(2929);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    ColorUtil.color(new Color(255, 85, 255).getRGB());
                    GL11.glTranslatef((float)(arrowX + arrowSize / 2), arrowY + arrowSize / 2, 0.0f);
                    GL11.glRotatef((float)degrees - 90.0f, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef((float)(-arrowSize / 2), (float)(-arrowSize / 2), 0.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(SquadHealthHUD.texture);
                    Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, arrowSize, arrowSize, (float)arrowSize, (float)arrowSize);
                    GlStateManager.resetColor();
                    GL11.glDisable(3042);
                    GL11.glEnable(2929);
                    GL11.glPopMatrix();
                    GlStateManager.disableBlend();
                    GlStateManager.disableAlpha();
                }
            }
        }
        GlStateManager.popMatrix();
    }
    
    private String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String formattedName = NameUtil.getFormattedName(networkPlayerInfoIn);
        return SquadHealthHUD.squadSuffixPattern.matcher(formattedName).replaceFirst("");
    }
    
    public void renderDummy() {
        GlStateManager.pushMatrix();
        final int hudXpos = this.guiPosition.getAbsoluteRenderX();
        final int hudYpos = this.guiPosition.getAbsoluteRenderY();
        final int listSize = 4;
        final int maxNameWidth = mc.fontRendererObj.getStringWidth(mc.thePlayer.getName());
        final int maxScoreWidth = mc.fontRendererObj.getStringWidth(" 00");
        final int maxFinalWidth = mc.fontRendererObj.getStringWidth(" 0");
        final int maxLineWidth = maxNameWidth + maxFinalWidth + maxScoreWidth;
        Gui.drawRect(hudXpos, hudYpos, hudXpos + maxLineWidth + 2, hudYpos + 36 + 1, Integer.MIN_VALUE);
        for (int i = 0; i < 4; ++i) {
            final int xDrawingPos = hudXpos + 1;
            final int yDrawingPos = hudYpos + 1 + i * 9;
            Gui.drawRect(xDrawingPos, yDrawingPos, hudXpos + maxLineWidth + 1, yDrawingPos + 8, 553648127);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final String formattedName = EnumChatFormatting.GREEN + mc.thePlayer.getName();
            mc.fontRendererObj.drawStringWithShadow(formattedName, (float)xDrawingPos, (float)yDrawingPos, -1);
            final int xStartFinalDrawingPos = xDrawingPos + maxNameWidth + 1;
            final int xStartScoreDrawingPos = xStartFinalDrawingPos + maxFinalWidth;
            if (maxScoreWidth + maxFinalWidth > 5) {
                final int scorePoints = 12 + (i * 18 + 22) % 8;
                final String scoreString = GuiPlayerTabOverlayHook_ColoredScores.getColoredHP(EnumChatFormatting.GREEN,scorePoints) + " " + scorePoints;
                mc.fontRendererObj.drawStringWithShadow(scoreString, (float)xStartScoreDrawingPos, (float)yDrawingPos, 16777215);
                final String finalsString = EnumChatFormatting.GOLD + " " + (3 + (i * 28 + 15) % 5);
                mc.fontRendererObj.drawStringWithShadow(finalsString, (float)xStartFinalDrawingPos, (float)yDrawingPos, 16777215);
            }
        }
        GlStateManager.popMatrix();
    }
    
    public boolean isEnabled(final long currentTimeMillis) {
        return MWEConfig.squadHUDPosition.isEnabled() && SquadHandler.getSquad().size() > 1;
    }

    private EnumChatFormatting getColor(long idk) {
        if (idk < 0) return EnumChatFormatting.RED;
        if (idk > 0) return EnumChatFormatting.GREEN;
        return EnumChatFormatting.YELLOW;
    }
    
    static {
        ordering = Ordering.from(new PlayerComparator());
        SquadHealthHUD.texture = new ResourceLocation("squadhud:whitearrow.png");
        squadSuffixPattern = Pattern.compile("^" + EnumChatFormatting.GOLD + "\\[" + EnumChatFormatting.DARK_GREEN + "S" + EnumChatFormatting.GOLD + "\\] ");
    }
    
    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<NetworkPlayerInfo>
    {
        private PlayerComparator() {
        }
        
        @Override
        public int compare(final NetworkPlayerInfo p_compare_1_, final NetworkPlayerInfo p_compare_2_) {
            final ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            final ScorePlayerTeam scoreplayerteam2 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare((Comparable)((scoreplayerteam != null) ? scoreplayerteam.getRegisteredName() : ""), (Comparable)((scoreplayerteam2 != null) ? scoreplayerteam2.getRegisteredName() : "")).compare((Comparable)p_compare_1_.getGameProfile().getName(), (Comparable)p_compare_2_.getGameProfile().getName()).result();
        }
    }
}
