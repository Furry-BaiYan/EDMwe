//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Deobfuscator3000\mappings"!

//Decompiled by Procyon!

package fr.alexdoru.mwe.mark;

import fr.alexdoru.mwe.features.ClientIRC;
import fr.alexdoru.mwe.utils.LogUtil;
import fr.alexdoru.mwe.utils.UtilMethods;
import net.minecraftforge.fml.relauncher.*;
import java.awt.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.particle.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.util.*;

import static fr.alexdoru.mwe.Wrapper.mc;

@SideOnly(Side.CLIENT)
public class MarkerRenderer
{
    private static final double OUTLINE_OPACITY = 0.75;
    private static final double BOX_OPACITY = 0.15;
    private static int MARKER_TIMEOUT_MS;
    private static int MARKER_FADE_MS;
    private static final int HIDE_PROXIMITY = 3;
    private static final int DIM_PROXIMITY = 6;
    private static final Color BLOCK_COLOR;
    private static final ResourceLocation markerTexture;
    private static final Minecraft minecraft;
    private static Tessellator tessellator;
    private static WorldRenderer wRenderer;
    private static Entity viewEntity;
    private static double oldy;
    private static double newy;
    private static final int[] BLOCK_RGB;
    
    public static int getMarkerRenderDistanceChunks() {
        return Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
    }
    
    public static boolean isRenderDistanceExpanded() {
        return false;
    }
    
    public static void onRenderWorldLast(final float pticks) {
        tessellator = Tessellator.getInstance();
        wRenderer = tessellator.getWorldRenderer();
        viewEntity = mc.getRenderViewEntity();
        try {
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            oldy = viewEntity.prevPosY;
            newy = viewEntity.posY;
            minecraft.renderEngine.bindTexture(markerTexture);
            if (!ClientIRC.markList.isEmpty()) {
                for (final Marker marker : ClientIRC.markList) {
                    final BlockPos markerBlockPos = marker.getBlockPos();
                    final int markerAge = (int)(System.currentTimeMillis() - marker.getTimestamp().getTime());
                    final double proximity3D = UtilMethods.distanceTo3D(viewEntity, markerBlockPos);
                    final double proximity2D = UtilMethods.distanceTo2D(viewEntity, markerBlockPos);
                    if (isRenderDistanceExpanded()) {
                        GlStateManager.disableFog();
                    }
                    if (proximity3D > 3.0 && proximity2D < getMarkerRenderDistanceChunks() * 16) {
                        wRenderer.setTranslation(-EntityFX.interpPosX, -EntityFX.interpPosY, -EntityFX.interpPosZ);
                        GL11.glLineWidth((float)(2.0 / proximity3D));
                        final AxisAlignedBB aabb = new AxisAlignedBB(markerBlockPos, markerBlockPos.add(1, 1, 1));
                        final int lifefade = markerAge / 2;
                        final int lifefadeback = MARKER_FADE_MS - lifefade;
                        int alphaValue = (markerAge > MARKER_TIMEOUT_MS) ? 0 : Math.min(255, (lifefade > 255) ? lifefadeback : lifefade);
                        if (proximity2D < 6.0) {
                            alphaValue /= 2;
                        }
                        drawOutline(aabb, BLOCK_RGB[0], BLOCK_RGB[1], BLOCK_RGB[2], (int)(alphaValue * 0.75));
                        drawBox(aabb, BLOCK_RGB[0], BLOCK_RGB[2], BLOCK_RGB[2], (int)(alphaValue * 0.15));
                        final float bx = markerBlockPos.getX() + 0.5f;
                        final float by = markerBlockPos.getY() + 0.5f;
                        final float bz = markerBlockPos.getZ() + 0.5f;
                        wRenderer.setTranslation(-EntityFX.interpPosX + bx, -EntityFX.interpPosY, -EntityFX.interpPosZ + bz);
                        renderMarker(marker, alphaValue, pticks);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                Tessellator.getInstance().getWorldRenderer().finishDrawing();
            }
            catch (IllegalStateException ignored) {}
        }
        finally {
            wRenderer.setTranslation(0.0, 0.0, 0.0);
            GL11.glLineWidth(1.0f);
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.enableFog();
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }
    
    private static void drawOutline(final AxisAlignedBB boundingBox, final int red, final int green, final int blue, final int alpha) {
        wRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        wRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        wRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }
    
    private static void drawBox(final AxisAlignedBB boundingBox, final int red, final int green, final int blue, final int alpha) {
        wRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        wRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        wRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        wRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        wRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        wRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        wRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }
    
    private static void renderMarker(final Marker marker, final int alpha, final float pticks) {
        final BlockPos bp = marker.getBlockPos();
        final Color color = marker.getColor();
        final int red = color.getRed();
        final int blue = color.getBlue();
        final int green = color.getGreen();
        final float bx = bp.getX() + 0.5f;
        final float by = bp.getY() + 0.5f;
        final float bz = bp.getZ() + 0.5f;
        final Vec3 player = new Vec3(viewEntity.posX - bx, viewEntity.posY - by + viewEntity.getEyeHeight(), viewEntity.posZ - bz);
        final double ypos = 1.0 + oldy + (newy - oldy) * pticks;
        final double yaw = -Math.atan2(player.zCoord, player.xCoord) - 1.5707963267948966;
        final double sinyaw = Math.sin(yaw);
        final double cosyaw = Math.cos(yaw);
        GL11.glLineWidth(4.0f);
        wRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        wRenderer.pos(0.0, bp.getY() + 0.5, 0.0).color(red, green, blue, alpha / 2).endVertex();
        wRenderer.pos(0.0, ypos + 1.5, 0.0).color(red, green, blue, alpha / 2).endVertex();
        tessellator.draw();
        final double minU = 0.125 * marker.getTextureIndex();
        final double maxU = minU + 0.125;
        GlStateManager.enableTexture2D();
        wRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        if (ypos - bp.getY() >= -1.0) {
            wRenderer.pos(cosyaw, ypos + 0.5, -sinyaw).tex(minU, 1.0).color(red, green, blue, alpha).endVertex();
            wRenderer.pos(cosyaw, ypos + 2.5, -sinyaw).tex(minU, 0.0).color(red, green, blue, alpha).endVertex();
            wRenderer.pos(-cosyaw, ypos + 2.5, sinyaw).tex(maxU, 0.0).color(red, green, blue, alpha).endVertex();
            wRenderer.pos(-cosyaw, ypos + 0.5, sinyaw).tex(maxU, 1.0).color(red, green, blue, alpha).endVertex();
        }
        else {
            wRenderer.pos(cosyaw, ypos + 0.5, -sinyaw).tex(minU, 0.0).color(red, green, blue, alpha).endVertex();
            wRenderer.pos(cosyaw, ypos + 2.5, -sinyaw).tex(minU, 1.0).color(red, green, blue, alpha).endVertex();
            wRenderer.pos(-cosyaw, ypos + 2.5, sinyaw).tex(maxU, 1.0).color(red, green, blue, alpha).endVertex();
            wRenderer.pos(-cosyaw, ypos + 0.5, sinyaw).tex(maxU, 0.0).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        GlStateManager.disableTexture2D();
    }
    
    static {
        MARKER_TIMEOUT_MS = 15000;
        MARKER_FADE_MS = MARKER_TIMEOUT_MS / 1;
        BLOCK_COLOR = new Color(255, 255, 255);
        markerTexture = new ResourceLocation("mwe", "textures/gui/markers.png");
        minecraft = Minecraft.getMinecraft();
        tessellator = Tessellator.getInstance();
        wRenderer = tessellator.getWorldRenderer();
        viewEntity = minecraft.getRenderViewEntity();
        oldy = 0.0;
        newy = 0.0;
        BLOCK_RGB = new int[] { BLOCK_COLOR.getRed(), BLOCK_COLOR.getGreen(), BLOCK_COLOR.getBlue() };
    }
}
