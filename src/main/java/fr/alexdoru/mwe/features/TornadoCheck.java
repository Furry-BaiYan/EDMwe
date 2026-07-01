package fr.alexdoru.mwe.features;

import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.data.MWPlayerData;
import fr.alexdoru.mwe.enums.MWClass;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import fr.alexdoru.mwe.utils.NameUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TornadoCheck {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Map<String, Long> lastAlertMillisByPlayer = new HashMap<>();
    private static final long ALERT_COOLDOWN_MS = 5000L;
    private static final ConcurrentLinkedQueue<S2APacketParticles> particleQueue = new ConcurrentLinkedQueue<>();
    private static final Map<String, Long> chargedCache = new HashMap<>();
    private static final long CHARGED_STATE_DURATION_MS = 3000L;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        while (!particleQueue.isEmpty()) {
            final S2APacketParticles packet = particleQueue.poll();
            if (packet == null || mc.theWorld == null) {
                continue;
            }
            EntityPlayer closest = null;
            double minDistanceSq = 36.0D;
            for (final EntityPlayer player : mc.theWorld.playerEntities) {
                final double dSq = player.getDistanceSq(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate());
                if (dSq < minDistanceSq) {
                    minDistanceSq = dSq;
                    closest = player;
                }
            }
            if (closest != null) {
                chargedCache.put(closest.getName(), System.currentTimeMillis());
            }
        }
        if (!MWEConfig.tornadoCheck || mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        if (!ScoreboardTracker.isInMwGame()) {
            return;
        }
        final long now = System.currentTimeMillis();
        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            final MWPlayerData.PlayerData playerData;
            final double distance;
            final String playerName;
            final Long lastMillis;
            if (player == mc.thePlayer
                    || (playerData = NameUtil.getMWPlayerData(player.getGameProfile(), false)) == null
                    || playerData.mwClass != MWClass.SHAMAN
                    || this.isPlayerInSameTeam(player)
                    || (distance = mc.thePlayer.getDistanceToEntity(player)) > 30.0D
                    || !this.isShamanAbilityCharged(player, now)
                    || ((lastMillis = lastAlertMillisByPlayer.get(playerName = player.getName())) != null && now - lastMillis < ALERT_COOLDOWN_MS)) {
                continue;
            }
            lastAlertMillisByPlayer.put(playerName, now);
            this.alertTornadoThreat(player, distance);
        }
        chargedCache.entrySet().removeIf(entry -> now - entry.getValue() > CHARGED_STATE_DURATION_MS);
    }

    public static void handleParticlePacket(S2APacketParticles packet) {
        if (isSkillAuraParticle(packet)) {
            particleQueue.add(packet);
        }
    }

    private static boolean isSkillAuraParticle(S2APacketParticles packet) {
        final EnumParticleTypes type = packet.getParticleType();
        switch (type) {
            case REDSTONE:
            case PORTAL:
            case SPELL_INSTANT:
            case SMOKE_LARGE:
            case DRIP_LAVA:
            case HEART:
            case NOTE:
            case SLIME:
            case CRIT:
            case CRIT_MAGIC:
            case VILLAGER_HAPPY:
            case FLAME:
            case FIREWORKS_SPARK:
            case SPELL_MOB:
            case SPELL:
            case CLOUD:
                return true;
            default:
                return false;
        }
    }

    private boolean isPlayerInSameTeam(EntityPlayer player) {
        final MWPlayerData.PlayerData ourData = NameUtil.getMWPlayerData(mc.thePlayer.getGameProfile(), false);
        final MWPlayerData.PlayerData theirData = NameUtil.getMWPlayerData(player.getGameProfile(), false);
        if (ourData == null || theirData == null) {
            return false;
        }
        return ourData.teamColor == theirData.teamColor;
    }

    private boolean isShamanAbilityCharged(EntityPlayer player, long now) {
        final Long particleTime = chargedCache.get(player.getName());
        return particleTime != null && now - particleTime < CHARGED_STATE_DURATION_MS;
    }

    private void alertTornadoThreat(EntityPlayer shamanPlayer, double distance) {
        final MWPlayerData.PlayerData playerData = NameUtil.getMWPlayerData(shamanPlayer.getGameProfile(), false);
        final String teamColor = playerData != null ? "\u00a7" + playerData.teamColor : "";
        final String message = EnumChatFormatting.DARK_AQUA + "[" + EnumChatFormatting.AQUA + "Tornado Alert" + EnumChatFormatting.DARK_AQUA + "] "
                + teamColor + shamanPlayer.getName()
                + EnumChatFormatting.WHITE + " has their "
                + EnumChatFormatting.GREEN + "TORNADO"
                + EnumChatFormatting.WHITE + " fully charged! ("
                + EnumChatFormatting.LIGHT_PURPLE + String.format("%.1f", distance) + "m"
                + EnumChatFormatting.WHITE + ")";
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void resetAlerts() {
        lastAlertMillisByPlayer.clear();
        chargedCache.clear();
        particleQueue.clear();
    }

}
