package fr.alexdoru.mwe.asm.hooks;

import fr.alexdoru.mwe.MWE;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.enums.MWClass;
import fr.alexdoru.mwe.features.PhxRespawnListener;
import fr.alexdoru.mwe.features.PotionCheck;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class RenderHook_Renegade {

    private static final String PHX_TAG = "[PHX]";
    private static final String REN_ARROW = "➜";
    private static final String POTION_TAG_PREFIX = " (";
    private static final String RESPAWNED_NAMETAG_ICON = " " + EnumChatFormatting.GREEN + "√" + EnumChatFormatting.RESET;
    private static final String NOT_RESPAWNED_NAMETAG_ICON = " " + EnumChatFormatting.RED + "×" + EnumChatFormatting.RESET;

    public static StringBuilder appendToScore(StringBuilder str, AbstractClientPlayer entity) {
        if (MWEConfig.renegadeArrowCount && ScoreboardTracker.isInMwGame()) {
            final List<Long> list = MWE.INSTANCE().getRenegadeTracker().getArrowsForPlayer(entity.getName());
            if (list == null || list.isEmpty()) {
                return str;
            }
            return format(str, list.size(), entity.getHealth());
        }
        return str;
    }

    public static String appendToNametag(String original, EntityLivingBase entity, double distSq) {
        original = appendMegaWallsNametagTags(original, entity);
        if (distSq >= 100.0D && MWEConfig.renegadeArrowCount && MWEConfig.renegadeArrowCountOnNametags && ScoreboardTracker.isInMwGame()) {
            return appendRenegadeArrowTag(original, entity);
        }
        return original;
    }

    public static IChatComponent appendToDisplayName(IChatComponent original, EntityPlayer entity) {
        if (original == null || entity == null || !ScoreboardTracker.isInMwGame()) {
            return original;
        }
        String formattedText = appendMegaWallsNametagTags(original.getFormattedText(), entity);
        if (MWEConfig.renegadeArrowCount && MWEConfig.renegadeArrowCountOnNametags) {
            formattedText = appendRenegadeArrowTag(formattedText, entity);
        }
        if (formattedText.equals(original.getFormattedText())) {
            return original;
        }
        return new ChatComponentText(formattedText);
    }

    private static String appendMegaWallsNametagTags(String original, EntityLivingBase entity) {
        if (MWEConfig.phxRespawnIconsOnNametags && ScoreboardTracker.isInDeathMatch()) {
            original = appendPhxRespawnIcon(original, entity);
        }
        if (MWEConfig.potionDetector && MWEConfig.potionCountsOnNametags && ScoreboardTracker.isInDeathMatch()) {
            original = appendPotionCountTag(original, entity);
        }
        return original;
    }

    private static String appendRenegadeArrowTag(String original, EntityLivingBase entity) {
        if (original == null || entity == null || hasRenegadeArrowTag(original)) {
            return original;
        }
        final List<Long> list = MWE.INSTANCE().getRenegadeTracker().getArrowsForPlayer(entity.getName());
        if (list == null || list.isEmpty()) {
            return original;
        }
        return format(new StringBuilder().append(original), list.size(), entity.getHealth()).toString();
    }

    private static String appendPhxRespawnIcon(String original, EntityLivingBase entity) {
        if (original == null || entity == null || !ScoreboardTracker.isInMwGame() || !(entity instanceof EntityPlayer)) {
            return original;
        }

        final boolean isPhoenix = PhxRespawnListener.isPhoenix(entity.getName()) || containsPhxTag(original) || isPhoenixByTeam((EntityPlayer) entity);
        if (!isPhoenix) {
            return original;
        }

        final int phxTagEnd = getPhxTagEnd(original);
        if (phxTagEnd < 0) {
            return original + getPhxRespawnIcon(entity.getName());
        }
        if (hasPhxRespawnIcon(original, phxTagEnd)) {
            return original;
        }
        return original.substring(0, phxTagEnd) + getPhxRespawnIcon(entity.getName()) + original.substring(phxTagEnd);
    }

    private static String appendPotionCountTag(String original, EntityLivingBase entity) {
        if (original == null || entity == null || !ScoreboardTracker.isInMwGame() || !(entity instanceof EntityPlayer) || hasPotionCountTag(original)) {
            return original;
        }
        final EntityPlayer player = (EntityPlayer) entity;
        final MWClass mwClass = getPlayerClass(player);
        if (mwClass == null) {
            return original;
        }
        final int usedPots = PotionCheck.getUsedPotsCount(player.getName());
        if (usedPots <= 0) {
            return original;
        }
        return original + EnumChatFormatting.GRAY + POTION_TAG_PREFIX + EnumChatFormatting.LIGHT_PURPLE + usedPots + EnumChatFormatting.GRAY + ")" + EnumChatFormatting.RESET;
    }

    private static boolean isPhoenixByTeam(EntityPlayer player) {
        return getPlayerClass(player) == MWClass.PHOENIX;
    }

    private static MWClass getPlayerClass(EntityPlayer player) {
        MWClass mwClass = MWClass.ofPlayer(player.getName());
        if (mwClass == null && player.getTeam() instanceof ScorePlayerTeam) {
            final ScorePlayerTeam team = (ScorePlayerTeam) player.getTeam();
            mwClass = MWClass.fromTeamTag(ScoreboardTracker.isMWReplay() ? team.getColorPrefix() : team.getColorSuffix());
        }
        return mwClass;
    }

    private static String getPhxRespawnIcon(String playerName) {
        return PhxRespawnListener.isRespawned(playerName) ? NOT_RESPAWNED_NAMETAG_ICON : RESPAWNED_NAMETAG_ICON;
    }

    private static boolean containsPhxTag(String playerName) {
        return getPhxTagEnd(playerName) >= 0;
    }

    private static boolean hasPhxRespawnIcon(String playerName, int phxTagEnd) {
        final String afterTag = playerName.substring(phxTagEnd);
        return afterTag.startsWith(RESPAWNED_NAMETAG_ICON) || afterTag.startsWith(NOT_RESPAWNED_NAMETAG_ICON);
    }

    private static int getPhxTagEnd(String playerName) {
        int matchedCharacters = 0;
        for (int i = 0; i < playerName.length(); i++) {
            final char current = playerName.charAt(i);
            if ((current == '\u00A7' || current == '搂') && i + 1 < playerName.length()) {
                i++;
                continue;
            }

            final char normalized = Character.toUpperCase(current);
            if (normalized == PHX_TAG.charAt(matchedCharacters)) {
                matchedCharacters++;
                if (matchedCharacters == PHX_TAG.length()) {
                    return i + 1;
                }
            } else {
                matchedCharacters = normalized == PHX_TAG.charAt(0) ? 1 : 0;
            }
        }
        return -1;
    }

    private static StringBuilder format(StringBuilder str, int size, float health) {
        str.append(EnumChatFormatting.RESET);
        if (health <= size * 2) {
            str.append(EnumChatFormatting.GOLD);
        }
        return str.append("  ").append(size).append(EnumChatFormatting.GREEN).append(" ").append(REN_ARROW);
    }

    private static boolean hasRenegadeArrowTag(String text) {
        return text.indexOf(REN_ARROW) >= 0;
    }

    private static boolean hasPotionCountTag(String text) {
        return text.contains(POTION_TAG_PREFIX + EnumChatFormatting.LIGHT_PURPLE)
                || text.matches(".* \\(\\d+\\)(?:§r)?$");
    }

}
