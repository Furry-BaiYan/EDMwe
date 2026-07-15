package fr.alexdoru.mwe.gui.huds;

import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.enums.MWClass;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;

public class SkillCooldownHUD extends AbstractRenderer {

    private static final long DISPLAY_DURATION_MS = 2500L;
    private static final String LABEL = "SKILL";
    private static String currentNumber = "";
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
        final String skillNumber = getSkillNumber(stripped);
        if (!skillNumber.isEmpty()) {
            currentNumber = skillNumber;
            showUntil = System.currentTimeMillis() + DISPLAY_DURATION_MS;
        } else {
            currentNumber = "";
            showUntil = 0L;
        }
        return text;
    }

    private static String getSkillNumber(String strippedText) {
        final int skillIndex = strippedText.indexOf(LABEL);
        if (skillIndex < 0) {
            return "";
        }
        int numberStart = -1;
        for (int i = skillIndex + LABEL.length(); i < strippedText.length(); i++) {
            if (Character.isDigit(strippedText.charAt(i))) {
                numberStart = i;
                break;
            }
            if (!Character.isWhitespace(strippedText.charAt(i))) {
                return "";
            }
        }
        if (numberStart < 0) {
            return "";
        }
        int numberEnd = numberStart;
        while (numberEnd < strippedText.length() && Character.isDigit(strippedText.charAt(numberEnd))) {
            numberEnd++;
        }
        return strippedText.substring(numberStart, numberEnd);
    }

    @Override
    public void render(ScaledResolution resolution) {
        this.guiPosition.updateAbsolutePosition(resolution);
        drawSkillNumber(currentNumber, this.guiPosition.getAbsoluteRenderX(), this.guiPosition.getAbsoluteRenderY());
    }

    @Override
    public void renderDummy() {
        drawSkillNumber("2", this.guiPosition.getAbsoluteRenderX(), this.guiPosition.getAbsoluteRenderY());
    }

    private void drawSkillNumber(String number, int centerX, int y) {
        final int width = mc.fontRendererObj.getStringWidth(number);
        mc.fontRendererObj.drawStringWithShadow(number, centerX - width / 2, y, MWEConfig.skillCooldownHUDStatusColor);
    }

    @Override
    public boolean isEnabled(long currentTimeMillis) {
        return this.guiPosition.isEnabled()
                && ScoreboardTracker.isInMwGame()
                && !shouldHideForClass()
                && currentTimeMillis < showUntil
                && !currentNumber.isEmpty();
    }

    private boolean shouldHideForClass() {
        if (mc.thePlayer == null) {
            return false;
        }
        final MWClass mwClass = MWClass.ofPlayer(mc.thePlayer.getName());
        return mwClass == MWClass.PHOENIX || mwClass == MWClass.SNOWMAN;
    }

}
