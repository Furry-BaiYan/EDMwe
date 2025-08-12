//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Deobfuscator3000\mappings"!

//Decompiled by Procyon!

package fr.alexdoru.mwe.enums;

import fr.alexdoru.mwe.features.FinalKillCounter;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import java.util.*;

public enum MegaWallsMap
{
    ANCHORED(TeamColor.RED, TeamColor.YELLOW, TeamColor.BLUE, TeamColor.GREEN, -53.0, 54.0, 54.0, -53.0, 65.0), 
    AZTEC(TeamColor.YELLOW, TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, 820.0, -902.0, 927.0, -1009.0, 36.0), 
    BARRAGE(TeamColor.YELLOW, TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, -189.0, -723.0, -87.0, -825.0, 38.0), 
    BLOODMOON(TeamColor.YELLOW, TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, -53.0, 54.0, 54.0, -53.0, 65.0), 
    CITY(TeamColor.GREEN, TeamColor.RED, TeamColor.BLUE, TeamColor.YELLOW, -189.0, -725.0, -89.0, -825.0, 34.0), 
    DRAGONKEEP(TeamColor.YELLOW, TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, -190.0, -724.0, -88.0, -826.0, 38.0), 
    DUSKFORGE(TeamColor.BLUE, TeamColor.YELLOW, TeamColor.RED, TeamColor.GREEN, -53.0, 54.0, 54.0, -53.0, 65.0), 
    DYNASTY(TeamColor.GREEN, TeamColor.RED, TeamColor.BLUE, TeamColor.YELLOW, -54.0, 54.0, 53.0, -53.0, 57.0), 
    EBONVEIL(TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, TeamColor.YELLOW, -53.0, 54.0, 54.0, -53.0, 71.0), 
    EGYPT(TeamColor.RED, TeamColor.YELLOW, TeamColor.BLUE, TeamColor.GREEN, -190.0, -724.0, -88.0, -826.0, 38.0), 
    FORSAKEN(TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, TeamColor.YELLOW, -190.0, -724.0, -88.0, -826.0, 38.0), 
    GOLDFORGE(TeamColor.YELLOW, TeamColor.RED, TeamColor.GREEN, TeamColor.BLUE, -53.0, 54.0, 54.0, -53.0, 57.0), 
    IMPERIAL(TeamColor.YELLOW, TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, -53.0, 54.0, 54.0, -53.0, 65.0), 
    KINGDOM(TeamColor.GREEN, TeamColor.RED, TeamColor.BLUE, TeamColor.YELLOW, -190.0, -724.0, -89.0, -825.0, 38.0), 
    KIROBIRO(TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, TeamColor.YELLOW, -52.0, 55.0, 55.0, -52.0, 57.0), 
    LAUNCHSITE(TeamColor.BLUE, TeamColor.YELLOW, TeamColor.GREEN, TeamColor.RED, -50.0, 51.0, 51.0, -50.0, 42.0), 
    MADPIXEL(TeamColor.YELLOW, TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, -51.0, 51.0, 51.0, -51.0, 29.0), 
    OASIS(TeamColor.GREEN, TeamColor.BLUE, TeamColor.YELLOW, TeamColor.RED, -50.0, 50.0, 52.0, -52.0, 60.0), 
    SERENITY(TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, TeamColor.YELLOW, -44.0, 51.0, 56.0, -49.0, 41.0), 
    SERPENTS(TeamColor.GREEN, TeamColor.RED, TeamColor.BLUE, TeamColor.YELLOW, -189.0, -722.0, -88.0, -823.0, 49.0), 
    SHADOWSTONE(TeamColor.BLUE, TeamColor.YELLOW, TeamColor.RED, TeamColor.GREEN, -53.0, 54.0, 54.0, -53.0, 65.0), 
    SHANTYBAY(TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, TeamColor.YELLOW, -53.0, 54.0, 54.0, -53.0, 71.0), 
    SOLACE(TeamColor.BLUE, TeamColor.YELLOW, TeamColor.GREEN, TeamColor.RED, -131.0, -696.0, -24.0, -803.0, 57.0), 
    STEPPES(TeamColor.YELLOW, TeamColor.GREEN, TeamColor.RED, TeamColor.BLUE, -838.0, -234.0, -736.0, -336.0, 60.0), 
    STONEHOLD(TeamColor.YELLOW, TeamColor.BLUE, TeamColor.RED, TeamColor.GREEN, -53.0, 54.0, 54.0, -53.0, 65.0), 
    WONDERLAND(TeamColor.GREEN, TeamColor.YELLOW, TeamColor.BLUE, TeamColor.RED, -53.0, 54.0, 54.0, -53.0, 58.0);
    
    private final TeamColor northBase;
    private final TeamColor eastBase;
    private final TeamColor southBase;
    private final TeamColor westBase;
    private final double northLimit;
    private final double eastLimit;
    private final double southLimit;
    private final double westLimit;
    private final double surfaceLevel;
    private static final Map<String, MegaWallsMap> fromNameMap;
    
    private MegaWallsMap(final TeamColor northBase, final TeamColor eastBase, final TeamColor southBase, final TeamColor westBase, final double northLimit, final double eastLimit, final double southLimit, final double westLimit, final double surfaceLevel) {
        this.northBase = northBase;
        this.eastBase = eastBase;
        this.southBase = southBase;
        this.westBase = westBase;
        this.northLimit = northLimit;
        this.eastLimit = eastLimit;
        this.southLimit = southLimit;
        this.westLimit = westLimit;
        this.surfaceLevel = surfaceLevel;
    }
    
    public String getPlayerBaseLocation(final EntityPlayer player) {
        if (player.posZ < this.northLimit && player.posX > this.westLimit) {
            return this.northBase.formattedName();
        }
        if (player.posX > this.eastLimit && player.posZ > this.northLimit) {
            return this.eastBase.formattedName();
        }
        if (player.posZ > this.southLimit && player.posX < this.eastLimit) {
            return this.southBase.formattedName();
        }
        if (player.posX < this.westLimit && player.posZ < this.southLimit) {
            return this.westBase.formattedName();
        }
        if (this.isPlayerAtMiddle(player)) {
            return "MIDDLE";
        }
        return "";
    }
    
    private boolean isPlayerAtMiddle(final EntityPlayer player) {
        return player.posX < this.eastLimit && player.posX > this.westLimit && player.posZ < this.southLimit && player.posZ > this.northLimit;
    }
    
    private double calculateDistanceToMiddle(final EntityPlayer player) {
        final double middleX = (this.eastLimit + this.westLimit) / 2.0;
        final double middleZ = (this.northLimit + this.southLimit) / 2.0;
        final double distanceToMiddle = Math.sqrt(Math.pow(player.posX - middleX, 2.0) + Math.pow(player.posZ - middleZ, 2.0));
        return distanceToMiddle;
    }
    
    public String getNearestBase(final EntityPlayer player) {
        final double distanceToMiddle = this.calculateDistanceToMiddle(player);
        final double middleX = (this.eastLimit + this.westLimit) / 2.0;
        final double middleZ = (this.northLimit + this.southLimit) / 2.0;
        final double playerX = player.posX;
        final double playerZ = player.posZ;
        final double distanceToNorthMiddle = Math.sqrt(Math.pow(playerX - middleX, 2.0) + Math.pow(playerZ - this.northLimit, 2.0));
        final double distanceToEastMiddle = Math.sqrt(Math.pow(playerX - this.eastLimit, 2.0) + Math.pow(playerZ - middleZ, 2.0));
        final double distanceToSouthMiddle = Math.sqrt(Math.pow(playerX - middleX, 2.0) + Math.pow(playerZ - this.southLimit, 2.0));
        final double distanceToWestMiddle = Math.sqrt(Math.pow(playerX - this.westLimit, 2.0) + Math.pow(playerZ - middleZ, 2.0));
        double minDistance = Double.MAX_VALUE;
        if (player.posZ < this.northLimit && player.posX > this.westLimit) {
            minDistance = Math.min(distanceToEastMiddle, Math.min(distanceToSouthMiddle, distanceToWestMiddle));
        }
        else if (player.posX > this.eastLimit && player.posZ > this.northLimit) {
            minDistance = Math.min(distanceToNorthMiddle, Math.min(distanceToSouthMiddle, distanceToWestMiddle));
        }
        else if (player.posZ > this.southLimit && player.posX < this.eastLimit) {
            minDistance = Math.min(distanceToNorthMiddle, Math.min(distanceToEastMiddle, distanceToWestMiddle));
        }
        else if (player.posX < this.westLimit && player.posZ < this.southLimit) {
            minDistance = Math.min(distanceToNorthMiddle, Math.min(distanceToSouthMiddle, distanceToEastMiddle));
        }
        else if (this.isPlayerAtMiddle(player)) {
            minDistance = Math.min(distanceToNorthMiddle, Math.min(distanceToEastMiddle, Math.min(distanceToSouthMiddle, distanceToWestMiddle)));
        }
        if (minDistance == distanceToNorthMiddle) {
            return EnumChatFormatting.RESET + "(" + this.northBase.formattedName() + EnumChatFormatting.RESET + ")";
        }
        if (minDistance == distanceToEastMiddle) {
            return EnumChatFormatting.RESET + "(" + this.eastBase.formattedName() + EnumChatFormatting.RESET + ")";
        }
        if (minDistance == distanceToSouthMiddle) {
            return EnumChatFormatting.RESET + "(" + this.southBase.formattedName() + EnumChatFormatting.RESET + ")";
        }
        if (minDistance == distanceToWestMiddle) {
            return EnumChatFormatting.RESET + "(" + this.westBase.formattedName() + EnumChatFormatting.RESET + ")";
        }
        return "";
    }
    
    public static MegaWallsMap fromName(final String name) {
        return MegaWallsMap.fromNameMap.get(name.replace(" ", "").toLowerCase());
    }
    
    static {
        fromNameMap = new HashMap<String, MegaWallsMap>();
        for (final MegaWallsMap map : values()) {
            MegaWallsMap.fromNameMap.put(map.name().toLowerCase(), map);
        }
    }
    
    enum TeamColor
    {
        BLUE, 
        GREEN, 
        RED, 
        YELLOW;
        
        public String formattedName() {
            switch (this) {
                case BLUE: {
                    return FinalKillCounter.getColorPrefixFromTeam(3) + "BLUE";
                }
                case GREEN: {
                    return FinalKillCounter.getColorPrefixFromTeam(1) + "GREEN";
                }
                case RED: {
                    return FinalKillCounter.getColorPrefixFromTeam(0) + "RED";
                }
                case YELLOW: {
                    return FinalKillCounter.getColorPrefixFromTeam(2) + "YELLOW";
                }
                default: {
                    throw new IllegalStateException(this.name() + " shoudn't exist in this enum");
                }
            }
        }
    }
}
