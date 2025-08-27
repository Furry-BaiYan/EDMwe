package fr.alexdoru.mwe.gui.huds;

import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.features.StrengthTracker;
import by.radioegor146.nativeobfuscator.Native;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

import static fr.alexdoru.mwe.MWE.nullcheck;

@Native
public class PlayerStrengthHUD extends AbstractRenderer {

    public PlayerStrengthHUD() {
        super(MWEConfig.playerStrengthHUD);
    }

    @Override
    public void render(ScaledResolution resolution) {
        int y = this.guiPosition.getAbsoluteRenderY();
        int x = this.guiPosition.getAbsoluteRenderX();
        for (String killer : StrengthTracker.getPlayersWithStrength()) {
            String displayName = StrengthTracker.getDisplayName(killer);
            long buffMS = StrengthTracker.getRemainingMs(killer);
            int time = Math.round((float) buffMS / 1000);
            mc.fontRendererObj.drawStringWithShadow(displayName,x,y,-1);
            mc.fontRendererObj.drawStringWithShadow(getColor(time) + "" + time + "s",x + 155,y,-1);
            y += 11;
        }
    }

    @Override
    public void renderDummy() {
        if (nullcheck()) return;
        mc.fontRendererObj.drawStringWithShadow(mc.thePlayer.getDisplayName().getFormattedText() + "     " + EnumChatFormatting.GREEN + "5s",guiPosition.getAbsoluteRenderX(),guiPosition.getAbsoluteRenderY(),-1);
    }

    @Override
    public boolean isEnabled(long currentTimeMillis) {
        return guiPosition.isEnabled();
    }

    private EnumChatFormatting getColor(int time) {
        if (time > 3) return EnumChatFormatting.GREEN;
        if (time == 3) return EnumChatFormatting.YELLOW;
        return EnumChatFormatting.RED;
    }
}
