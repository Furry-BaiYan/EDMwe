package fr.alexdoru.mwe.features;

import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Pattern;

public class EndermanTeleportRange {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final double TELEPORT_RANGE = 25.0D;

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!MWEConfig.endermanTeleportRange) {
            return;
        }
        if (!ScoreboardTracker.isInMwGame()) {
            return;
        }
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        if (!this.isPlayerEnderman()) {
            return;
        }
        if (mc.thePlayer.experienceLevel < 100) {
            return;
        }
        if (!this.isHoldingSwordOrBow()) {
            return;
        }
        this.renderEnemyHitboxes();
    }

    private boolean isPlayerEnderman() {
        final String className = this.getClientClassName();
        return "END".equals(className) || "ENDERMAN".equals(className);
    }

    private boolean isHoldingSwordOrBow() {
        final ItemStack heldItem = mc.thePlayer.getHeldItem();
        if (heldItem == null) {
            return false;
        }
        if (heldItem.getItem() == Items.iron_sword
                || heldItem.getItem() == Items.diamond_sword
                || heldItem.getItem() == Items.stone_sword
                || heldItem.getItem() == Items.wooden_sword
                || heldItem.getItem() == Items.golden_sword) {
            return true;
        }
        return heldItem.getItem() == Items.bow;
    }

    private void renderEnemyHitboxes() {
        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            final double distance;
            if (player == null || player == mc.thePlayer || (distance = mc.thePlayer.getDistanceToEntity(player)) > TELEPORT_RANGE || this.isSameTeam(player)) {
                continue;
            }
            this.renderRedHitbox(player);
        }
    }

    private boolean isSameTeam(EntityPlayer player) {
        if (mc.theWorld == null) {
            return false;
        }
        final ScorePlayerTeam playerTeam = mc.theWorld.getScoreboard().getPlayersTeam(player.getName());
        final ScorePlayerTeam myTeam = mc.theWorld.getScoreboard().getPlayersTeam(mc.thePlayer.getName());
        if (playerTeam == null || myTeam == null) {
            return false;
        }
        return playerTeam.getRegisteredName().equals(myTeam.getRegisteredName());
    }

    private void renderRedHitbox(EntityPlayer player) {
        final double x = player.posX - mc.getRenderManager().viewerPosX;
        final double y = player.posY - mc.getRenderManager().viewerPosY;
        final double z = player.posZ - mc.getRenderManager().viewerPosZ;
        final float width = player.width;
        final float height = player.height;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F);
        this.renderHitboxOutline(width, height);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private void renderHitboxOutline(float width, float height) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        final float halfWidth = width / 2.0F;

        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(-halfWidth, 0.0D, -halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, 0.0D, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, 0.0D, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, 0.0D, -halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, 0.0D, -halfWidth).endVertex();
        tessellator.draw();

        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(-halfWidth, height, -halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, height, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, height, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, height, -halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, height, -halfWidth).endVertex();
        tessellator.draw();

        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(-halfWidth, 0.0D, -halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, height, -halfWidth).endVertex();
        tessellator.draw();

        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(-halfWidth, 0.0D, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, height, halfWidth).endVertex();
        tessellator.draw();

        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(halfWidth, 0.0D, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, height, halfWidth).endVertex();
        tessellator.draw();

        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(halfWidth, 0.0D, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, height, -halfWidth).endVertex();
        tessellator.draw();
    }

    private String getClientClassName() {
        if (mc.thePlayer == null || mc.getNetHandler() == null) {
            return "";
        }
        final NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        if (playerInfo == null) {
            return "";
        }
        final ScorePlayerTeam playerTeam = playerInfo.getPlayerTeam();
        if (playerTeam == null) {
            return "";
        }
        final String raw = EnumChatFormatting.getTextWithoutFormattingCodes(playerTeam.getColorSuffix());
        final Pattern classNameSuffixPattern = Pattern.compile(".*\\[[A-Z]{3}].*");
        if (classNameSuffixPattern.matcher(raw).matches()) {
            return raw.substring(raw.indexOf('[') + 1, raw.indexOf(']'));
        }
        return "";
    }
}
