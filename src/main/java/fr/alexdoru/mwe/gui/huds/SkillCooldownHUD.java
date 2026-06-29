package fr.alexdoru.mwe.gui.huds;

import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;

public class SkillCooldownHUD extends AbstractRenderer {

    private static final long DISPLAY_DURATION_MS = 2500L;
    private static final String LABEL = "SKILL";
    private static String currentStatus = "";
    private static long showUntil;

    public SkillCooldownHUD() {
        super(MWEConfig.skillCooldownHUDPosition);
    }

    public static String captureActionbar(String text) {
        if (text == null) {
            return null;
        }
        final String stripped = StringUtils.stripControlCodes(text);
        if (stripped == null || !stripped.contains("SKILL")) {
            return text;
        }
        final String skillStatus = getSkillStatus(stripped);
        if (!skillStatus.isEmpty()) {
            currentStatus = skillStatus;
            showUntil = System.currentTimeMillis() + DISPLAY_DURATION_MS;
        }
        return text;
    }

    private static String getSkillStatus(String strippedText) {
        final int skillIndex = strippedText.indexOf(LABEL);
        if (skillIndex < 0) {
            return "";
        }
        int statusStart = skillIndex + LABEL.length();
        while (statusStart < strippedText.length() && Character.isWhitespace(strippedText.charAt(statusStart))) {
            statusStart++;
        }
        int statusEnd = statusStart;
        while (statusEnd < strippedText.length() && !Character.isWhitespace(strippedText.charAt(statusEnd))) {
            statusEnd++;
        }
        return statusStart < statusEnd ? strippedText.substring(statusStart, statusEnd) : "";
    }

    @Override
    public void render(ScaledResolution resolution) {
        this.guiPosition.updateAbsolutePosition(resolution);
        drawSkillText(LABEL, currentStatus, this.guiPosition.getAbsoluteRenderX(), this.guiPosition.getAbsoluteRenderY());
    }

    @Override
    public void renderDummy() {
        drawSkillText(LABEL, "\u2714", this.guiPosition.getAbsoluteRenderX(), this.guiPosition.getAbsoluteRenderY());
    }

    private void drawSkillText(String label, String status, int centerX, int y) {
        final String labelText = label + " ";
        final int labelWidth = mc.fontRendererObj.getStringWidth(labelText);
        final int statusWidth = mc.fontRendererObj.getStringWidth(status);
        final int x = centerX - (labelWidth + statusWidth) / 2;
        mc.fontRendererObj.drawStringWithShadow(labelText, x, y, MWEConfig.skillCooldownHUDTextColor);
        mc.fontRendererObj.drawStringWithShadow(status, x + labelWidth, y, MWEConfig.skillCooldownHUDStatusColor);
    }

    @Override
    public boolean isEnabled(long currentTimeMillis) {
        return this.guiPosition.isEnabled() && ScoreboardTracker.isInMwGame() && currentTimeMillis < showUntil && !currentStatus.isEmpty();
    }

}
