package fr.alexdoru.mwe.features;

import fr.alexdoru.mwe.Wrapper;
import fr.alexdoru.mwe.events.MegaWallsGameEvent;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import fr.alexdoru.mwe.utils.LogUtil;
import fr.alexdoru.mwe.utils.NameUtil;
import langyaobf.Native;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static fr.alexdoru.mwe.MWE.nullcheck;

@Native
public class PhxRespawnListener implements Wrapper {

    private final List<String> phxs = new ArrayList<>();
    private final List<PHXPlayer> PHX_NONE_RES_LIST = new ArrayList<>();  //没复活
    public static final List<String> PHX_ALR_RES_LIST = new ArrayList<>();   //已复活
    private boolean isTick1;
    int tick;


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (nullcheck()) return;

        tick++;

        if (tick > 20) {
            tick = 0;
            isTick1 = !isTick1;
        }

        if (mc.getNetHandler() != null && !mc.getNetHandler().getPlayerInfoMap().isEmpty()) {
            for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
                String displayname = info.getDisplayName() != null ? info.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(), info.getGameProfile().getName());
                String name = info.getGameProfile().getName();
                if (remove(displayname).contains("[PHX]") && !phxs.contains(name)) {
                    PHX_NONE_RES_LIST.add(new PHXPlayer(name,displayname,20,20));
                    phxs.add(name);
                    LogUtil.addMessage(EnumChatFormatting.GOLD + "Found PHX Player: " + displayname);
                }
            }
        }

        if (!PHX_NONE_RES_LIST.isEmpty()) {
            for (Iterator<PHXPlayer> iterator = PHX_NONE_RES_LIST.iterator(); iterator.hasNext();) {
                    PHXPlayer player = iterator.next();
                    Scoreboard scoreboard = mc.theWorld.getScoreboard();
                    ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(0);
                    if (scoreobjective == null) return;
                    int hp = scoreobjective.getScoreboard().getValueFromObjective(player.getName(),scoreobjective).getScorePoints();
                    if (isTick1) {
                        player.setTick1HP(hp);
                    } else {
                        player.setTick2HP(hp);
                    }
                    player.setDeltaHP(player.tick1HP - player.tick2HP);
                    if (Math.abs(player.deltaHP) > 32 && !PHX_ALR_RES_LIST.contains(player.getDisplayname())) {
                        PHX_ALR_RES_LIST.add(player.getDisplayname());
                        LogUtil.addMessage(EnumChatFormatting.RED + "Player " + player.getDisplayname() + EnumChatFormatting.RED + " JUST RESPAWNED!!!!!!!!");
                        iterator.remove();
                    }
            }
        }
    }

    private void reset() {
        phxs.clear();
        PHX_NONE_RES_LIST.clear();
        PHX_ALR_RES_LIST.clear();
    }

    @SubscribeEvent
    public void onMWGame(MegaWallsGameEvent event) {
        if (event.getType() == MegaWallsGameEvent.EventType.GAME_START || event.getType() == MegaWallsGameEvent.EventType.GAME_END) {
            reset();
        }
        if (event.getType() == MegaWallsGameEvent.EventType.DISCONNECT) {
            phxs.clear();
            PHX_NONE_RES_LIST.clear();
        }
    }


    private static String remove(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '§') {
                i++;
            } else {
                result.append(input.charAt(i));
            }
        }
        return NameUtil.deobfString(result.toString());
    }

    public static boolean isRespawned(String name) {
        return PHX_ALR_RES_LIST.contains(name);
    }

    private static class PHXPlayer {

        public String getName() {
            return name;
        }

        private String name;

        public String getDisplayname() {
            return displayname;
        }

        private String displayname;
        private int tick1HP;
        private int tick2HP;
        private int deltaHP;

        public void setTick1HP(int tick1HP) {
            this.tick1HP = tick1HP;
        }

        public void setTick2HP(int tick2HP) {
            this.tick2HP = tick2HP;
        }

        public void setDeltaHP(int deltaHP) {
            this.deltaHP = deltaHP;
        }

        private PHXPlayer(String name,String displayname,int tick1HP, int tick2HP) {
            this.name = name;
            this.displayname = displayname;
            this.tick1HP = tick1HP;
            this.tick2HP = tick2HP;
        }


    }

}
