package fr.alexdoru.mwe.utils;

import fr.alexdoru.mwe.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
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


}
