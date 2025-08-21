package fr.alexdoru.mwe.events;

import fr.alexdoru.mwe.chat.ChatUtil;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.features.ClientIRC;
import fr.alexdoru.mwe.features.PhxRespawnListener;
import fr.alexdoru.mwe.gui.huds.BaseLocationHUD;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import fr.alexdoru.mwe.utils.LogUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeybindingListener {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final KeyBinding killKey = new KeyBinding("/Kill", 0, "MWE");
    private static final KeyBinding surfaceKey = new KeyBinding("/Surface", 0, "MWE");
    private static final KeyBinding echestKey = new KeyBinding("/enderchest", 0, "MWE");
    private static final KeyBinding teamchestKey = new KeyBinding("/teamchest", 0, "MWE");
    private static final KeyBinding toggleSprintKey = new KeyBinding("Toggle Sprint", 0, "MWE");
    private static final KeyBinding warcryKey = new KeyBinding("/warcry", 0, "MWE");
    private static final KeyBinding phxreslist = new KeyBinding("Show PHX list",0,"MWE");
    private static final KeyBinding selflocationKey = new KeyBinding("Send Your location to the irc",0,"MWE");
    private static final KeyBinding newNickKey = new KeyBinding("New Random Nick", 0, "MWE");
    private static final KeyBinding playerHitboxes = new KeyBinding("Toggle player hitboxes", 0, "Hitboxes");
    private static final KeyBinding toggleDroppedItemLimit = new KeyBinding("Toggle dropped item limit", 0, "MWE");

    public KeybindingListener() {
        ClientRegistry.registerKeyBinding(killKey);
        ClientRegistry.registerKeyBinding(surfaceKey);
        ClientRegistry.registerKeyBinding(echestKey);
        ClientRegistry.registerKeyBinding(teamchestKey);
        ClientRegistry.registerKeyBinding(toggleSprintKey);
        ClientRegistry.registerKeyBinding(warcryKey);
        ClientRegistry.registerKeyBinding(phxreslist);
        ClientRegistry.registerKeyBinding(selflocationKey);
        ClientRegistry.registerKeyBinding(newNickKey);
        ClientRegistry.registerKeyBinding(playerHitboxes);
        ClientRegistry.registerKeyBinding(toggleDroppedItemLimit);
    }

    @SubscribeEvent
    public void key(KeyInputEvent e) {

        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        if (ScoreboardTracker.isInMwGame() && !ScoreboardTracker.isPrepPhase() && killKey.isPressed() && !(mc.currentScreen instanceof GuiGameOver)) {
            mc.thePlayer.sendChatMessage("/kill");
        } else if (ScoreboardTracker.isPrepPhase() && surfaceKey.isPressed()) {
            mc.thePlayer.sendChatMessage("/surface");
        } else if (ScoreboardTracker.isInMwGame() && echestKey.isPressed()) {
            mc.thePlayer.sendChatMessage("/enderchest");
        } else if (ScoreboardTracker.isInMwGame() && teamchestKey.isPressed()) {
            mc.thePlayer.sendChatMessage("/teamchest");
        } else if (toggleSprintKey.isPressed()) {
            MWEConfig.toggleSprint = !MWEConfig.toggleSprint;
            if (MWEConfig.toggleSprint) ChatUtil.addChatMessage(EnumChatFormatting.GREEN + "Toggle Sprint On");
            else ChatUtil.addChatMessage(EnumChatFormatting.RED + "Toggle Sprint Off");
            MWEConfig.saveConfig();
        } else if (ScoreboardTracker.isInMwGame() && !ScoreboardTracker.isPrepPhase() && warcryKey.isPressed()) {
            mc.thePlayer.sendChatMessage("/warcry");
        } else if (toggleDroppedItemLimit.isPressed()) {
            MWEConfig.limitDroppedEntityRendered = !MWEConfig.limitDroppedEntityRendered;
            MWEConfig.saveConfig();
            if (MWEConfig.limitDroppedEntityRendered) {
                ChatUtil.addChatMessage(ChatUtil.getTagMW() + EnumChatFormatting.YELLOW + "Limit dropped items rendered: " + EnumChatFormatting.GREEN + "On");
            } else {
                ChatUtil.addChatMessage(ChatUtil.getTagMW() + EnumChatFormatting.YELLOW + "Limit dropped items rendered: " + EnumChatFormatting.RED + "Off");
            }
        } else if (playerHitboxes.isPressed()) {
            MWEConfig.drawHitboxForPlayers = !MWEConfig.drawHitboxForPlayers;
            MWEConfig.saveConfig();
            if (MWEConfig.drawHitboxForPlayers) {
                ChatUtil.addChatMessage(ChatUtil.getTagHitboxes() + EnumChatFormatting.WHITE + "Draw hitboxes for players: " + EnumChatFormatting.GREEN + "On");
            } else {
                ChatUtil.addChatMessage(ChatUtil.getTagHitboxes() + EnumChatFormatting.WHITE + "Draw hitboxes for players: " + EnumChatFormatting.RED + "Off");
            }
        } else if (phxreslist.isKeyDown()) {
            if (!PhxRespawnListener.PHX_ALR_RES_LIST.isEmpty()) {
                LogUtil.addMessage("List of Revived Phoenixes: ");
                PhxRespawnListener.PHX_ALR_RES_LIST.forEach(LogUtil::addcfgmessage);
            } else {
                LogUtil.addMessage("No Revived Phoenix");
            }
        } else if (selflocationKey.isKeyDown()) {
             //ClientIRC.sendMessage( "Im in the " + BaseLocationHUD.text.replaceAll("§.","") + String.format(" X:%s Y:%s Z:%s",(int)mc.thePlayer.posX,(int)mc.thePlayer.posY,(int)mc.thePlayer.posZ));
            mc.thePlayer.sendChatMessage( "Im in the " +  String.format("X:%s Y:%s Z:%s",(int)mc.thePlayer.posX,(int)mc.thePlayer.posY,(int)mc.thePlayer.posZ));
        }

    }

    public static KeyBinding getNewNickKey() {
        return newNickKey;
    }

}
