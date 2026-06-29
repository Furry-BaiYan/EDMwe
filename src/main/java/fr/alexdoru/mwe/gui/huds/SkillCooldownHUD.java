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
        return removeVisibleSkillSegment(text, stripped);
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

    private static String removeVisibleSkillSegment(String formattedText, String strippedText) {
        final int skillVisibleStart = strippedText.indexOf(LABEL);
        if (skillVisibleStart < 0) {
            return formattedText;
        }
        int visibleEnd = skillVisibleStart + LABEL.length();
        while (visibleEnd < strippedText.length() && Character.isWhitespace(strippedText.charAt(visibleEnd))) {
            visibleEnd++;
        }
        while (visibleEnd < strippedText.length() && !Character.isWhitespace(strippedText.charAt(visibleEnd))) {
            visibleEnd++;
        }
        while (visibleEnd < strippedText.length() && Character.isWhitespace(strippedText.charAt(visibleEnd))) {
            visibleEnd++;
        }
        int rawStart = getRawIndexForVisibleIndex(formattedText, skillVisibleStart);
        while (rawStart >= 2 && formattedText.charAt(rawStart - 2) == '\u00a7') {
            rawStart -= 2;
        }
        final int rawEnd = getRawIndexForVisibleIndex(formattedText, visibleEnd);
        return (formattedText.substring(0, rawStart) + formattedText.substring(rawEnd)).trim();
    }

    private static int getRawIndexForVisibleIndex(String formattedText, int visibleIndex) {
        int visibleCount = 0;
        for (int rawIndex = 0; rawIndex < formattedText.length(); rawIndex++) {
            if (formattedText.charAt(rawIndex) == '\u00a7' && rawIndex + 1 < formattedText.length()) {
                rawIndex++;
                continue;
            }
            if (visibleCount == visibleIndex) {
                return rawIndex;
            }
            visibleCount++;
        }
        return formattedText.length();
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
