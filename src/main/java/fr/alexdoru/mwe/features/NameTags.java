package fr.alexdoru.mwe.features;

import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import fr.alexdoru.mwe.utils.ColorUtil;
import fr.alexdoru.mwe.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NameTags {

    @SubscribeEvent
    public void removeNameTag(RenderLivingEvent.Specials.Pre<? extends EntityLivingBase> player) {
        if (player.entity instanceof EntityPlayer && MWEConfig.nametags && player.entity != Minecraft.getMinecraft().thePlayer) {
            player.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        if (!MWEConfig.nametags) {
            return;
        }
        final Minecraft mc = Minecraft.getMinecraft();
        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) {
                continue;
            }
            final String distance = "§7[§f" + (int) mc.thePlayer.getDistanceToEntity(player) + "§7] ";
            String tag = distance + "§r" + player.getDisplayName().getFormattedText() + " §r" + String.format("%.1f", player.getHealth());
            final String name = player.getName();
            if (ScoreboardTracker.isInMwGame()) {
                if (MWEConfig.nameTagRes && ScoreboardTracker.isInDeathMatch() && PhxRespawnListener.phxs.contains(name)) {
                    tag = !PhxRespawnListener.isRespawned(name)
                            ? distance + "§r" + player.getDisplayName().getFormattedText() + EnumChatFormatting.GREEN + " ✔ §r" + String.format("%.1f", player.getHealth())
                            : distance + "§r" + player.getDisplayName().getFormattedText() + EnumChatFormatting.RED + " ⚔ §r" + String.format("%.1f", player.getHealth());
                }
                if (MWEConfig.nameTagfinal && FinalKillCounter.getPlayersFinals(name) > 0) {
                    tag = tag + " " + EnumChatFormatting.YELLOW + FinalKillCounter.getPlayersFinals(name);
                }
                if (MWEConfig.showPotionUsedOnNametags && PotionCheck.getUsedPotsCount(name) > 0) {
                    tag = tag + " " + EnumChatFormatting.LIGHT_PURPLE + PotionCheck.getUsedPotsCount(name);
                }
            }
            if (player.getUniqueID().version() == 2 && ScoreboardTracker.isInMwGame()) {
                RenderUtil.renderNameTag(player, distance + "§r" + player.getDisplayName().getFormattedText() + " §rLogout", 0xFFFFFF, event.partialTicks, (float) (MWEConfig.nametagScale / 10.0));
                continue;
            }
            RenderUtil.renderNameTag(player, tag, ColorUtil.getHPColorInt(player.getMaxHealth(), player.getHealth()), event.partialTicks, (float) (MWEConfig.nametagScale / 10.0));
        }
    }
}
