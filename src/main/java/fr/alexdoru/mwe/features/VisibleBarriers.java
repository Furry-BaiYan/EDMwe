package fr.alexdoru.mwe.features;

import fr.alexdoru.mwe.config.MWEConfig;
import net.minecraft.client.Minecraft;

public final class VisibleBarriers {

    private VisibleBarriers() {
    }

    public static boolean isVisible() {
        return MWEConfig.visibleBarriers;
    }

    public static void toggleVisibility() {
        MWEConfig.visibleBarriers = !MWEConfig.visibleBarriers;
        MWEConfig.saveConfig();
        refreshRenderers();
    }

    public static void refreshRenderers() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.renderGlobal != null) {
            mc.renderGlobal.loadRenderers();
        }
    }
}
