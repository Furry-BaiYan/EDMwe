//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Deobfuscator3000\mappings"!

//Decompiled by Procyon!

package fr.alexdoru.mwe.gui.huds;

import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.features.FinalKillCounter;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import fr.alexdoru.mwe.utils.ColorUtil;
import fr.alexdoru.mwe.utils.NameUtil;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.entity.*;

import java.text.DecimalFormat;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClosestPlayerHUD extends AbstractRenderer
{
    public static ClosestPlayerHUD instance;
    private static final ResourceLocation texture;
    private final Minecraft mc;
    private static final String[] bwColorArray;
    private static final String[] dummyColorArray;

    public ClosestPlayerHUD() {
        super(MWEConfig.compassHUDPosition);
        mc = Minecraft.getMinecraft();
        ClosestPlayerHUD.instance = this;
    }

    /**
     * Helper class to store data for each row to be rendered.
     * This allows calculating the maximum width before drawing anything.
     */
    private static class RenderRow {
        final String teamColor;
        final EntityPlayer player;
        final String info;
        final String posDiffText;
        final String hpText;
        final double degrees;
        final int rowWidth;

        RenderRow(String teamColor, EntityPlayer player, String info, String posDiffText,String hpText, double degrees, int rowWidth) {
            this.teamColor = teamColor;
            this.player = player;
            this.info = info;
            this.posDiffText = posDiffText;
            this.hpText = hpText;
            this.degrees = degrees;
            this.rowWidth = rowWidth;
        }
    }

    public void render(final ScaledResolution resolution) {
        this.guiPosition.updateAbsolutePosition(resolution);
        final boolean isInBedwars = ScoreboardTracker.isInBedwars;
        if (!MWEConfig.compassHUDPosition.isEnabled() || (!ScoreboardTracker.isInMwGame && !isInBedwars)) {
            return;
        }

        final EntityPlayerSP thePlayer = mc.thePlayer;
        final double playerX = thePlayer.posX;
        final double playerZ = thePlayer.posZ;
        final double playerY = thePlayer.posY;

        String[] teamColors = { FinalKillCounter.getColorPrefixFromTeam(0), FinalKillCounter.getColorPrefixFromTeam(1), FinalKillCounter.getColorPrefixFromTeam(2), FinalKillCounter.getColorPrefixFromTeam(3) };
        if (isInBedwars) {
            teamColors = ClosestPlayerHUD.bwColorArray;
        }

        final List<RenderRow> rowsToRender = new ArrayList<>();
        int maxWidth = 0;

        for (final String teamColor : teamColors) {
            double closestDistance = Double.MAX_VALUE;
            EntityPlayer closestEntity = null;
            for (final EntityPlayer otherPlayer : mc.theWorld.playerEntities) {
                final String displayName = NameUtil.getFormattedNameWithoutIcons(otherPlayer.getDisplayName().getFormattedText());
                final String unformattedName = NameUtil.getFormattedNameWithoutIcons(otherPlayer.getDisplayName().getUnformattedText());
                if (displayName.contains(teamColor) && unformattedName.substring(0, 2).equals(teamColor) && otherPlayer != thePlayer) {
                    final double distance = thePlayer.getDistanceToEntity(otherPlayer);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestEntity = otherPlayer;
                    }
                }
            }

            if (closestEntity != null) {
                final int iconSize = 10;
                final int arrowSize = 10;
                final List<EntityPlayer> playerEntityInRadiusList = getPlayerEntityInRadiusList(closestEntity, 16.0);

                String info = teamColor + String.format("%.0f", closestDistance) + EnumChatFormatting.RESET;
                if (playerEntityInRadiusList != null && !playerEntityInRadiusList.isEmpty()) {
                    info = teamColor + String.format("%.0f", closestDistance) + "(" + playerEntityInRadiusList.size() + ")" + EnumChatFormatting.RESET;
                }

                final int posYDifference = (int)Math.round(closestEntity.posY - playerY);
                final EnumChatFormatting displayColor = (closestEntity.isInvisible() && isInBedwars) ? EnumChatFormatting.LIGHT_PURPLE : EnumChatFormatting.GRAY;
                String posDiffText;
                if (posYDifference > 0) {
                    posDiffText = displayColor + "+" + posYDifference;
                } else if (posYDifference < 0) {
                    posDiffText = displayColor + "" + posYDifference;
                } else {
                    posDiffText = displayColor + "0";
                }
                DecimalFormat df = new DecimalFormat("#.0");
                String hpText = ColorUtil.getColoredHP(EnumChatFormatting.WHITE,(int) closestEntity.getHealth()) + df.format(closestEntity.getHealth());

                final int textXOffsetForCalc = iconSize + 4;
                final int arrowXForCalc = textXOffsetForCalc + mc.fontRendererObj.getStringWidth(info) + 4;
                final int rowWidth = arrowXForCalc + arrowSize + 4 + mc.fontRendererObj.getStringWidth(posDiffText);
                if (rowWidth > maxWidth) {
                    maxWidth = rowWidth;
                }

                final double deltaX = closestEntity.posX - playerX;
                final double deltaZ = closestEntity.posZ - playerZ;
                final double angle = Math.atan2(deltaZ, deltaX);
                double degrees = Math.toDegrees(angle);
                degrees = (degrees + 360.0) % 360.0;
                degrees -= thePlayer.rotationYaw;
                if (degrees < 0.0) {
                    degrees += 360.0;
                }

                rowsToRender.add(new RenderRow(teamColor, closestEntity, info, posDiffText, hpText ,degrees, rowWidth));
            }
        }

        if (rowsToRender.isEmpty()) {
            return;
        }

        final int xOffset = this.guiPosition.getAbsoluteRenderX();
        int yOffset = this.guiPosition.getAbsoluteRenderY();
        final int totalHeight = rowsToRender.size() * 15;

        Gui.drawRect(xOffset - 2, yOffset - 2, xOffset + maxWidth + 2, yOffset + totalHeight, new Color(0, 0, 0, 120).getRGB());

        for (final RenderRow row : rowsToRender) {
            final int iconSize = 10;
            final int textXOffset = xOffset + iconSize + 4;

            // Draw player head
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            final ResourceLocation skinLocation = ((AbstractClientPlayer)row.player).getLocationSkin();
            mc.getTextureManager().bindTexture(skinLocation);
            Gui.drawScaledCustomSizeModalRect(xOffset, yOffset, 8.0f, 8.0f, 8, 8, iconSize, iconSize, 64.0f, 64.0f);

            // Draw info text
            mc.fontRendererObj.drawStringWithShadow(row.info, (float)textXOffset, yOffset + 1.5f, -1);

            // Draw arrow
            final int arrowSize = 10;
            final int arrowX = textXOffset + mc.fontRendererObj.getStringWidth(row.info) + 4;
            final float arrowY = (float)(yOffset + mc.fontRendererObj.FONT_HEIGHT / 2 - arrowSize / 2 + 1);
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            ColorUtil.color(this.getTracerColor(row.teamColor).getRGB());
            GL11.glTranslatef((float)(arrowX + arrowSize / 2), arrowY + (float) arrowSize / 2, 0.0f);
            GL11.glRotatef((float)row.degrees - 90.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef((float)(-arrowSize / 2), (float)(-arrowSize / 2), 0.0f);
            mc.getTextureManager().bindTexture(ClosestPlayerHUD.texture);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, arrowSize, arrowSize, (float)arrowSize, (float)arrowSize);
            GlStateManager.resetColor();
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GL11.glPopMatrix();

            // Draw display text (Y-diff and health)
            mc.fontRendererObj.drawStringWithShadow(row.posDiffText, (float)(arrowX + 14), yOffset + 1.5f, -1);
            yOffset += 15;
        }
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }


    public static final List<EntityPlayer> getPlayerEntityInRadiusList(final EntityPlayer targetEntity, final double radius) {
        if (targetEntity == null) {
            return null;
        }
        if (!targetEntity.isEntityAlive()) {
            return null;
        }
        final World world = targetEntity.getEntityWorld();
        final AxisAlignedBB searchBox = targetEntity.getEntityBoundingBox().expand(radius, radius, radius);
        final List<EntityPlayer> entityList = world.getEntitiesWithinAABB(EntityPlayer.class, searchBox).stream().filter(player -> isVaildEntity(targetEntity, player)).collect(Collectors.toList());
        return entityList;
    }

    private static final boolean isVaildEntity(final EntityPlayer nearbyEntity, final EntityPlayer targetEntity) {
        if (nearbyEntity == targetEntity) {
            return false;
        }
        //        if (AntiBot.instance.isBlackListed(nearbyEntity)) {
        //            return false;
        //        }
        final String targetName = NameUtil.getFormattedNameWithoutIcons(targetEntity.getDisplayName().getUnformattedText());
        final String nearbyEntityName = NameUtil.getFormattedNameWithoutIcons(nearbyEntity.getDisplayName().getUnformattedText());
        return targetName.substring(0, 2).equals(nearbyEntityName.substring(0, 2));
    }

    public void renderDummy() {
        final EntityPlayerSP player = mc.thePlayer;
        final int xOffset = this.guiPosition.getAbsoluteRenderX();
        int yOffset2 = this.guiPosition.getAbsoluteRenderY();
        final String[] teamColors = ClosestPlayerHUD.dummyColorArray;

        final int iconSize = 10;
        final int arrowSize = 10;
        final FontRenderer fr = mc.fontRendererObj;
        final String dummyInfo = "§c" + "0";
        final String dummyDisplayText = "0   " + EnumChatFormatting.GREEN + "20.0";
        final int textXOffsetForCalc = xOffset + iconSize + 4;
        final int arrowXForCalc = textXOffsetForCalc + fr.getStringWidth(dummyInfo) + 4;
        final int endX = arrowXForCalc + 14 + fr.getStringWidth(dummyDisplayText);
        final int maxWidth = endX - xOffset;

        final int totalHeight = teamColors.length * 15;
        Gui.drawRect(xOffset - 2, yOffset2 - 2, xOffset + maxWidth + 2, yOffset2 + totalHeight - 2, new Color(0, 0, 0, 120).getRGB());

        for (final String teamColor : teamColors) {
            final double closestDistance = 0.0;
            final String closestPlayerName = teamColor + player.getDisplayName().getFormattedText();
            final Entity closestEntity = player;
            if (closestPlayerName != null) {
                final int textXOffset = xOffset + iconSize + 4;
                final String teamColorCode = teamColor;
                final String info = ((teamColorCode != null) ? teamColorCode : "") + String.format("%.0f", closestDistance) + EnumChatFormatting.RESET;
                mc.fontRendererObj.drawStringWithShadow(info, (float)textXOffset, yOffset2 + 1.5f, -1);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                final ResourceLocation skinLocation = player.getLocationSkin();
                mc.getTextureManager().bindTexture(skinLocation);
                Gui.drawScaledCustomSizeModalRect(xOffset, yOffset2, 8.0f, 8.0f, 8, 8, iconSize, iconSize, 64.0f, 64.0f);
                final double deltaX = closestEntity.posX - player.posX;
                final double deltaZ = closestEntity.posZ - player.posZ;
                final double angle = Math.atan2(deltaZ, deltaX);
                double degrees = Math.toDegrees(angle);
                degrees = (degrees + 360.0) % 360.0;
                degrees -= mc.thePlayer.rotationYaw;
                if (degrees < 0.0) {
                    degrees += 360.0;
                }
                final int arrowX = textXOffset + mc.fontRendererObj.getStringWidth(info) + 4;
                final float arrowY = (float)(yOffset2 + mc.fontRendererObj.FONT_HEIGHT / 2 - arrowSize / 2 + 1);
                GL11.glPushMatrix();
                GL11.glDisable(2929);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                ColorUtil.color(this.getTracerColor(teamColor).getRGB());
                GL11.glTranslatef((float)(arrowX + arrowSize / 2), arrowY + arrowSize / 2, 0.0f);
                GL11.glRotatef((float)degrees - 90.0f, 0.0f, 0.0f, 1.0f);
                GL11.glTranslatef((float)(-arrowSize / 2), (float)(-arrowSize / 2), 0.0f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(ClosestPlayerHUD.texture);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, arrowSize, arrowSize, (float)arrowSize, (float)arrowSize);
                GlStateManager.resetColor();
                GL11.glDisable(3042);
                GL11.glEnable(2929);
                GL11.glPopMatrix();
                mc.fontRendererObj.drawStringWithShadow("0   " + EnumChatFormatting.GREEN + "20.0", (float)(arrowX + 14), yOffset2 + 1.5f, -1);
                yOffset2 += 15;
            }
        }
    }

    private Color getTracerColor(final String str) {
        final int[] colorArray = { 255, 255, 255 };
        switch (str.charAt(1)) {
            case '0': {
                colorArray[0] = 0;
                colorArray[2] = (colorArray[1] = 0);
                break;
            }
            case '1': {
                colorArray[1] = (colorArray[0] = 0);
                colorArray[2] = 170;
                break;
            }
            case '2': {
                colorArray[0] = 0;
                colorArray[1] = 170;
                colorArray[2] = 0;
                break;
            }
            case '3': {
                colorArray[0] = 0;
                colorArray[2] = (colorArray[1] = 170);
                break;
            }
            case '4': {
                colorArray[0] = 170;
                colorArray[2] = (colorArray[1] = 0);
                break;
            }
            case '5': {
                colorArray[0] = 170;
                colorArray[1] = 0;
                colorArray[2] = 170;
                break;
            }
            case '6': {
                colorArray[0] = 255;
                colorArray[1] = 170;
                colorArray[2] = 0;
                break;
            }
            case '7': {
                colorArray[0] = 170;
                colorArray[2] = (colorArray[1] = 170);
                break;
            }
            case '8': {
                colorArray[0] = 85;
                colorArray[2] = (colorArray[1] = 85);
                break;
            }
            case '9': {
                colorArray[1] = (colorArray[0] = 85);
                colorArray[2] = 255;
                break;
            }
            case 'a': {
                colorArray[0] = 85;
                colorArray[1] = 255;
                colorArray[2] = 85;
                break;
            }
            case 'b': {
                colorArray[0] = 85;
                colorArray[2] = (colorArray[1] = 255);
                break;
            }
            case 'c': {
                colorArray[0] = 255;
                colorArray[2] = (colorArray[1] = 85);
                break;
            }
            case 'd': {
                colorArray[0] = 255;
                colorArray[1] = 85;
                colorArray[2] = 255;
                break;
            }
            case 'e': {
                colorArray[1] = (colorArray[0] = 255);
                colorArray[2] = 85;
                break;
            }
            case 'f': {
                colorArray[0] = 255;
                colorArray[2] = (colorArray[1] = 255);
                break;
            }
            case 'r': {
                colorArray[0] = 255;
                colorArray[2] = (colorArray[1] = 255);
                break;
            }
        }
        return new Color(colorArray[0], colorArray[1], colorArray[2]);
    }

    public boolean isEnabled(final long currentTimeMillis) {
        return mc.thePlayer != null && mc.theWorld != null && MWEConfig.compassHUDPosition.isEnabled() && (ScoreboardTracker.isInMwGame || ScoreboardTracker.isInBedwars);
    }

    static {
        texture = new ResourceLocation("squadhud:whitearrow.png");
        bwColorArray = new String[] { EnumChatFormatting.RED.toString(), EnumChatFormatting.GREEN.toString(), EnumChatFormatting.YELLOW.toString(), EnumChatFormatting.BLUE.toString(), EnumChatFormatting.DARK_GRAY.toString(), EnumChatFormatting.LIGHT_PURPLE.toString(), EnumChatFormatting.WHITE.toString(), EnumChatFormatting.AQUA.toString() };
        dummyColorArray = new String[] { EnumChatFormatting.RED.toString(), EnumChatFormatting.GREEN.toString(), EnumChatFormatting.YELLOW.toString(), EnumChatFormatting.BLUE.toString() };
    }

    public class ClosestEntity
    {
        public String teamColorCode;
        public double distance;
        public EntityPlayer playerEntity;

        public ClosestEntity(final String teamColorCode, final double distance, final EntityPlayer playerEntity) {
            this.teamColorCode = teamColorCode;
            this.distance = distance;
            this.playerEntity = playerEntity;
        }
    }
}