package fr.alexdoru.mwe.utils;

import fr.alexdoru.mwe.Wrapper;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.StringUtils;

public class TeamUtils {

    public static boolean isOnSameTeam(Entity entity) {
        if (entity == null || entity.getDisplayName().getUnformattedText().length() < 4 || !(entity instanceof EntityPlayer)) {
            return false;
        }
        int otherColor = 32;
        int selfColor = 32;
        String other = entity.getDisplayName().getFormattedText();
        String self = Wrapper.mc.thePlayer.getDisplayName().getFormattedText();
        other = StringUtil.removePrefix(other);
        self = StringUtil.removePrefix(self);
        final ScoreObjective objective = Wrapper.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
        final String title = objective == null ? " " : objective.getDisplayName();
        if (other.charAt(0) == '§') {
            otherColor = other.charAt(1);
        }
        if (ScoreboardTracker.isInMwGame()) {
            if (title.charAt(0) == '§') {
                selfColor = title.charAt(1);
            }
        } else if (self.charAt(0) == '§') {
            selfColor = self.charAt(1);
        }
        return selfColor != 32 && otherColor == selfColor;
    }

    public static boolean isTeamWither(Entity entity) {
        int teamColor = 32;
        int witherColor = 32;
        if (!(entity instanceof EntityWither)) {
            return false;
        }
        if (ScoreboardTracker.isInMwGame()) {
            final ScoreObjective objective = Wrapper.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            final String title = objective == null ? " " : objective.getDisplayName();
            if (title.charAt(0) == '§') {
                teamColor = title.charAt(1);
            }
            final String witherName = StringUtils.stripControlCodes(entity.getDisplayName().getFormattedText());
            if (witherName.contains("Red")) witherColor = 'c';
            if (witherName.contains("Yellow")) witherColor = 'e';
            if (witherName.contains("Blue")) witherColor = '9';
            if (witherName.contains("Green")) witherColor = 'a';
        }
        return teamColor != 32 && teamColor == witherColor;
    }
}
