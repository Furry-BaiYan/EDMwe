package fr.alexdoru.mwe.features;

import fr.alexdoru.mwe.Wrapper;
import fr.alexdoru.mwe.utils.NameUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static fr.alexdoru.mwe.MWE.nullcheck;

public class StrengthTracker implements Wrapper {
    private static final Map<String, Long> strengthUntil = new ConcurrentHashMap<>();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (nullcheck()) return;
        final long now = System.currentTimeMillis();
        strengthUntil.entrySet().removeIf(e -> e.getValue() <= now);
    }

    public static void onKill(String killerName) {
        if (killerName == null || killerName.isEmpty()) return;

        String displayName = getDisplayName(killerName);

        long duration = 5000L; // 默认 5 秒
        if (displayName.contains("[DRE]")) {
            duration = 5000L;
        } else if (displayName.contains("[HBR]")) {
            duration = 6000L;
        }

        final long now = System.currentTimeMillis();
        strengthUntil.put(normalize(killerName), now + duration);
    }

    public static List<String> getPlayersWithStrength() {
        final long now = System.currentTimeMillis();
        return strengthUntil.entrySet().stream()
                .filter(e -> e.getValue() > now)
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    public static long getRemainingMs(String playerName) {
        final Long until = strengthUntil.get(normalize(playerName));
        if (until == null) return 0L;
        final long now = System.currentTimeMillis();
        return Math.max(0L, until - now);
    }

    public static String getDisplayName(String realName) {
        if (realName == null) return "";

        if (mc.getNetHandler() == null) return realName;

        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            if (info.getGameProfile().getName().equalsIgnoreCase(realName)) {
                if (info.getDisplayName() != null) {
                    return info.getDisplayName().getFormattedText();
                } else {
                    return info.getGameProfile().getName();
                }
            }
        }
        return realName;
    }

    private static String normalize(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    private static String remove(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '§') {
                i++;
            } else {
                result.append(input.charAt(i));
            }
        }
        return NameUtil.deobfString(result.toString());
    }
}
