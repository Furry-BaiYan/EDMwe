package fr.alexdoru.mwe.utils;

import fr.alexdoru.mwe.Wrapper;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.utils.TeamUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class RenderUtil implements Wrapper {
    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }

    public static void drawRect(int x,int y,int width,int height,int color) {
        Gui.drawRect(x,y,x + width,y + height,color);
    }

    public static void drawRect2(double left, double top, double right, double bottom, int color) {
        drawRect(left,top,right,bottom,color);
    }

    public static void drawRect2(double left, double top, double right, double bottom, Color color) {
        drawRect2(left,top,right,bottom,color.getRGB());
    }

    public static void drawRect(double x, double y, double width, double height, int color) {
        GlStateManager.resetColor();
        RenderUtil.setAlphaLimit(0);
        GLUtil.setup2DRendering(true);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(x, y, 0.0D).color(color >> 16 & 255, color >> 8 & 255, color & 255, color >> 24 & 255).endVertex();
        worldrenderer.pos(x, y + height, 0.0D).color(color >> 16 & 255, color >> 8 & 255, color & 255, color >> 24 & 255).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).color(color >> 16 & 255, color >> 8 & 255, color & 255, color >> 24 & 255).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).color(color >> 16 & 255, color >> 8 & 255, color & 255, color >> 24 & 255).endVertex();
        tessellator.draw();

        GLUtil.end2DRendering();
    }

    public static void renderItemTag(final Entity entity, final String tag, final int color, final float partialTicks, final float addScale) {
        final float x = (float)(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX);
        final float y = (float)(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY);
        final float z = (float)(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ);
        float size = mc.thePlayer.getDistanceToEntity(entity) / 10.0f;
        if (size < 1.1f) {
            size = 1.1f;
        }
        float scale = size * 1.8f;
        scale /= 100.0f;
        scale += addScale / 50.0f;
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y + 0.5f, z);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        if (mc.gameSettings.thirdPersonView == 2) {
            GL11.glRotatef(-mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        }
        else {
            GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        }
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(2929);
        //GL11.glEnable(3042);
        final float left = (float) -mc.fontRendererObj.getStringWidth(tag) / 2 - 4.6f;
        mc.fontRendererObj.drawString(tag, left + 4.0f, -12.5f, color, true);
        GL11.glEnable(2929);
        //GL11.glDisable(3042);
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public static void drawRect(double x, double y, double width, double height, Color color) {
        GlStateManager.resetColor();
        RenderUtil.setAlphaLimit(0);
        GLUtil.setup2DRendering(true);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(x, y, 0.0D).color(color.getRGB() >> 16 & 255, color.getRGB() >> 8 & 255, color.getRGB() & 255, color.getRGB() >> 24 & 255).endVertex();
        worldrenderer.pos(x, y + height, 0.0D).color(color.getRGB() >> 16 & 255, color.getRGB() >> 8 & 255, color.getRGB() & 255, color.getRGB() >> 24 & 255).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).color(color.getRGB() >> 16 & 255, color.getRGB() >> 8 & 255, color.getRGB() & 255, color.getRGB() >> 24 & 255).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).color(color.getRGB() >> 16 & 255, color.getRGB() >> 8 & 255, color.getRGB() & 255, color.getRGB() >> 24 & 255).endVertex();
        tessellator.draw();

        GLUtil.end2DRendering();
    }

    public static void drawRect(int x, int y, int width, int height, Color color) {
        drawRect(x,y,width,height,color.getRGB());
    }

    public static void drawRect(float x, float y, float x1, float y1, int color) {
        if (x < x1) { float tmp = x; x = x1; x1 = tmp; }
        if (y < y1) { float tmp = y; y = y1; y1 = tmp; }
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer wr = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        wr.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        wr.pos(x, y1, 0).endVertex();
        wr.pos(x1, y1, 0).endVertex();
        wr.pos(x1, y, 0).endVertex();
        wr.pos(x, y, 0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int colorBorder, int colorIn) {
        drawRect(x, y, x1, y1, colorIn);
        final float r = (colorBorder >> 16 & 0xFF) / 255.0f;
        final float g = (colorBorder >> 8 & 0xFF) / 255.0f;
        final float b = (colorBorder & 0xFF) / 255.0f;
        final float a = (colorBorder >> 24 & 0xFF) / 255.0f;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glPushMatrix();
        GL11.glColor4f(r, g, b, a);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y);
        GL11.glVertex2d(x, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void renderNameTag(EntityPlayer entity, String tag, int color, float partialTicks, float addScale) {
        final float x = (float)(entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks - mc.getRenderManager().viewerPosX);
        float y = (float)(entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks - mc.getRenderManager().viewerPosY);
        final float z = (float)(entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks - mc.getRenderManager().viewerPosZ);
        float size = mc.thePlayer.getDistanceToEntity(entity) / 10.0f;
        if (size < 1.1f) {
            size = 1.1f;
        }
        final double d = entity.isSneaking() ? 0.5 : 0.7;
        float scale = size * 1.8f;
        scale /= 100.0f;
        scale += addScale / 50.0f;
        GL11.glPushMatrix();
        GL11.glTranslatef(x, (float)((y += (float) d) + 1.3f), z);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        if (mc.gameSettings.thirdPersonView == 2) {
            GL11.glRotatef(-mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        } else {
            GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        }
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.disableTexture2D();
        final float x2 = (float)(-mc.fontRendererObj.getStringWidth(tag) / 2) - 4.6f;
        final float x3 = x2 - 2.0f * x2;
        if (TeamUtils.isOnSameTeam(entity)) {
            drawBorderedRect(x2 + 1.0f, -15.0f, x3 - 1.0f, -3.0f,
                    new Color(127, 255, 0, MWEConfig.nameTagBackground ? 111 : 10).getRGB(),
                    new Color(0, 0, 0, MWEConfig.nameTagBackground ? 111 : 10).getRGB());
        } else {
            drawRect(x2 + 1.0f, -15.0f, x3 - 1.0f, -3.0f, new Color(0, 0, 0, MWEConfig.nameTagBackground ? 111 : 10).getRGB());
        }
        GlStateManager.enableTexture2D();
        mc.fontRendererObj.drawString(tag, x2 + 4.0f, -12.5f, color, MWEConfig.nameTagShadow);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

}
