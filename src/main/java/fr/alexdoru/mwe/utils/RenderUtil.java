package fr.alexdoru.mwe.utils;

import fr.alexdoru.mwe.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

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
