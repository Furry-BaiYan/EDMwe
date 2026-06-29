package fr.alexdoru.mwe.asm.hooks;

import fr.alexdoru.mwe.MWE;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class RenderHook_Renegade {

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
        if (distSq >= 100.0D && MWEConfig.renegadeArrowCount && ScoreboardTracker.isInMwGame()) {
            final List<Long> list = MWE.INSTANCE().getRenegadeTracker().getArrowsForPlayer(entity.getName());
            if (list == null || list.isEmpty()) {
                return original;
            }
            return format(new StringBuilder().append(original), list.size(), entity.getHealth()).toString();
        }
        return original;
    }

    private static StringBuilder format(StringBuilder str, int size, float health) {
        str.append(EnumChatFormatting.RESET);
        if (health <= size * 2) {
            str.append(EnumChatFormatting.GOLD);
        }
        return str.append("  ").append(size).append(EnumChatFormatting.GREEN).append(" ➜");
    }

}
