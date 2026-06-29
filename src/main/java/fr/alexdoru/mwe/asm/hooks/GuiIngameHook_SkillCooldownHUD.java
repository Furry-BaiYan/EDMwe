package fr.alexdoru.mwe.asm.hooks;

import fr.alexdoru.mwe.gui.huds.SkillCooldownHUD;

@SuppressWarnings("unused")
public class GuiIngameHook_SkillCooldownHUD {

    public static String captureActionbar(String text) {
        return SkillCooldownHUD.captureActionbar(text);
    }

}
