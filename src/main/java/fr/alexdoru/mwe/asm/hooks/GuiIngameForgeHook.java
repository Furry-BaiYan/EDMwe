package fr.alexdoru.mwe.asm.hooks;

import fr.alexdoru.mwe.config.MWEConfig;

@SuppressWarnings("unused")
public class GuiIngameForgeHook {
    public static int adjustActionBarHeight(int y, int leftHeight) {
        return MWEConfig.fixActionbarTextOverlap && 68 < leftHeight ? y + 68 - leftHeight : y;
    }
}
