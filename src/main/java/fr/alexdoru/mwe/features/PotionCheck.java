package fr.alexdoru.mwe.features;

import fr.alexdoru.mwe.chat.ChatUtil;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.enums.MWClass;
import fr.alexdoru.mwe.enums.MWClassPotion;
import fr.alexdoru.mwe.events.MegaWallsGameEvent;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import fr.alexdoru.mwe.utils.NameUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PotionCheck {

    private static final int POTION_DETECT_COOLDOWN_TICKS = 30;
    private static final int SOFT_CONFIRM_WINDOW_TICKS = 8;

    private final Minecraft mc = Minecraft.getMinecraft();

    private static final Map<UUID, PotionTrackState> stateByUuid = new HashMap<>();
    private static final Map<String, UUID> latestUuidByName = new HashMap<>();
    private static String gameId;

    public static int getUsedPotsCount(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            return 0;
        }
        UUID uuid = latestUuidByName.get(playerName);
        if (uuid == null) {
            uuid = latestUuidByName.get(playerName.toLowerCase());
        }
        if (uuid == null && (uuid = populateUuidCacheFromEntity(playerName)) == null) {
            return 0;
        }
        final PotionTrackState state = stateByUuid.get(uuid);
        return state == null ? 0 : state.usedPots;
    }

    private static UUID populateUuidCacheFromEntity(String playerName) {
        final Minecraft mc = Minecraft.getMinecraft();
        final NetworkPlayerInfo netInfo = mc.getNetHandler() != null ? mc.getNetHandler().getPlayerInfo(playerName) : null;
        if (netInfo != null && netInfo.getGameProfile().getId() != null) {
            final UUID uuid = netInfo.getGameProfile().getId();
            latestUuidByName.put(playerName, uuid);
            latestUuidByName.put(playerName.toLowerCase(), uuid);
            return uuid;
        }
        return null;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !MWEConfig.potionDetector || this.mc.theWorld == null) {
            return;
        }
        if (!ScoreboardTracker.isInMwGame()) {
            return;
        }
        if (!ScoreboardTracker.isInDeathMatch()) {
            return;
        }
        this.detectPotionUsage();
    }

    @SubscribeEvent
    public void onMwGame(MegaWallsGameEvent event) {
        if (event.getType() == MegaWallsGameEvent.EventType.GAME_START || event.getType() == MegaWallsGameEvent.EventType.GAME_END) {
            this.resetPotionData();
            return;
        }
        if (event.getType() == MegaWallsGameEvent.EventType.CONNECT) {
            final String currentGameId = ScoreboardTracker.getParser().getGameId();
            if (gameId == null || !gameId.equals(currentGameId)) {
                this.resetPotionData();
                gameId = currentGameId;
            }
        }
    }

    private void detectPotionUsage() {
        if (this.mc.theWorld == null || this.mc.getNetHandler() == null) {
            return;
        }
        final int currentTick = (int) (this.mc.theWorld.getTotalWorldTime() & Integer.MAX_VALUE);
        final HashSet<UUID> seenUuids = new HashSet<>();
        final HashMap<UUID, String> uuidToName = new HashMap<>();
        for (final NetworkPlayerInfo netInfo : this.mc.getNetHandler().getPlayerInfoMap()) {
            if (netInfo == null || netInfo.getGameProfile().getId() == null) {
                continue;
            }
            final UUID uuid = netInfo.getGameProfile().getId();
            final String name = netInfo.getGameProfile().getName();
            latestUuidByName.put(name, uuid);
            latestUuidByName.put(name.toLowerCase(), uuid);
            uuidToName.put(uuid, name);
            if (netInfo.getGameType() == WorldSettings.GameType.SPECTATOR) {
                stateByUuid.remove(uuid);
                continue;
            }
            seenUuids.add(uuid);
            final MWClass mwClass = MWClass.ofPlayer(name);
            final MWClassPotion potData;
            if (mwClass == null || (potData = MWClassPotion.fromClass(mwClass)) == null || potData.potionCount <= 0) {
                continue;
            }
            final int currentHP = this.getHealthFromTablist(netInfo);
            final PotionTrackState state = stateByUuid.computeIfAbsent(uuid, key -> new PotionTrackState());
            final int hpGain;
            if (state.lastHp > 0 && currentHP > 0 && currentHP > state.lastHp && this.shouldCountPotionUse(state, hpGain = currentHP - state.lastHp, potData, currentTick)) {
                this.processUsage(name, state, potData, currentTick);
            }
            state.lastHp = currentHP;
        }
        final Iterator<Map.Entry<UUID, PotionTrackState>> it = stateByUuid.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<UUID, PotionTrackState> entry = it.next();
            final UUID uuid = entry.getKey();
            final String name;
            if (seenUuids.contains(uuid) || (name = uuidToName.get(uuid)) == null || !FinalKillCounter.isPlayerDead(name)) {
                continue;
            }
            it.remove();
        }
    }

    private boolean shouldCountPotionUse(PotionTrackState state, int hpGain, MWClassPotion potData, int tick) {
        if (state.usedPots >= potData.potionCount) {
            return false;
        }
        if (tick - state.lastDetectTick < POTION_DETECT_COOLDOWN_TICKS) {
            return false;
        }
        final int delta = Math.abs(hpGain - potData.hpPerPotion);
        if (delta <= 1) {
            return true;
        }
        if (delta <= 2) {
            if (state.pendingSoftDetectTick >= 0 && tick - state.pendingSoftDetectTick <= SOFT_CONFIRM_WINDOW_TICKS) {
                state.pendingSoftDetectTick = -1;
                return true;
            }
            state.pendingSoftDetectTick = tick;
        } else {
            state.pendingSoftDetectTick = -1;
        }
        return false;
    }

    private void processUsage(String name, PotionTrackState state, MWClassPotion potData, int tick) {
        final int previous = state.usedPots;
        final int updated = Math.min(potData.potionCount, previous + 1);
        if (updated == previous) {
            return;
        }
        state.usedPots = updated;
        state.lastDetectTick = tick;
        state.pendingSoftDetectTick = -1;
        NameUtil.updateMWPlayerDataAndEntityData(name, true);
        final int hearts = potData.hpPerPotion / 2;
        final int remaining = Math.max(0, potData.potionCount - updated);
        final EnumChatFormatting potColor = hearts >= 10 ? EnumChatFormatting.LIGHT_PURPLE : EnumChatFormatting.AQUA;
        ChatUtil.addChatMessage(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.LIGHT_PURPLE + "Potion Alert" + EnumChatFormatting.GRAY + "] " + NameUtil.getFormattedNameWithoutIcons(name) + EnumChatFormatting.YELLOW + " drank a " + potColor + hearts + " \u2764 " + EnumChatFormatting.YELLOW + "pot. " + EnumChatFormatting.GRAY + "(" + EnumChatFormatting.WHITE + remaining + EnumChatFormatting.GRAY + " left)");
    }

    private int getHealthFromTablist(NetworkPlayerInfo netInfo) {
        try {
            final Scoreboard sb = this.mc.theWorld.getScoreboard();
            final ScoreObjective obj = sb.getObjectiveInDisplaySlot(0);
            if (obj != null) {
                return sb.getValueFromObjective(netInfo.getGameProfile().getName(), obj).getScorePoints();
            }
        } catch (Exception ignored) {
        }
        try {
            final EntityPlayer player;
            if (this.mc.theWorld != null && (player = this.mc.theWorld.getPlayerEntityByName(netInfo.getGameProfile().getName())) != null) {
                return Math.max(0, (int) player.getHealth());
            }
        } catch (Exception ignored) {
        }
        return 0;
    }

    private void resetPotionData() {
        latestUuidByName.clear();
        if (!stateByUuid.isEmpty()) {
            stateByUuid.clear();
            if (this.mc.theWorld != null && this.mc.thePlayer != null) {
                try {
                    NameUtil.refreshAllNamesInWorld();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static class PotionTrackState {
        int lastHp = -1;
        int usedPots = 0;
        int lastDetectTick = -1073741824;
        int pendingSoftDetectTick = -1;
    }
}
