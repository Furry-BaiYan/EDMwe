package fr.alexdoru.mwe.features;

import fr.alexdoru.mwe.asm.hooks.NetHandlerPlayClientHook_PlayerMapTracker;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.enums.MWClass;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import fr.alexdoru.mwe.utils.NameUtil;
import fr.alexdoru.mwe.utils.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DiamondDetector {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Set<MWClass> ALERT_CLASSES = new HashSet<>();
    private static final Map<MWClass, Set<ArmorType>> CLASS_DIAMOND_ARMOR = new HashMap<>();
    private static final Set<String> alertedPlayers = new HashSet<>();
    private static final Set<String> alertedArmorPlayers = new HashSet<>();
    private static final Map<String, ItemStack> previousEquipment = new HashMap<>();
    private static final Map<String, ItemStack[]> previousArmor = new HashMap<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if ((!MWEConfig.diamondDetector && !MWEConfig.diamondArmorDetector) || !ScoreboardTracker.isInMwGame()) {
            return;
        }
        if (mc.theWorld == null) {
            return;
        }

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == null) {
                continue;
            }
            final String playerName = player.getName();
            if (MWEConfig.diamondDetector) {
                checkDiamondSword(player, playerName);
            }
            if (MWEConfig.diamondArmorDetector) {
                checkDiamondArmor(player, playerName);
            }
        }
    }

    private void checkDiamondSword(EntityPlayer player, String playerName) {
        final ItemStack currentItem = player.getHeldItem();
        final ItemStack previousItem = previousEquipment.get(playerName);
        final MWClass playerClass = MWClass.ofPlayer(playerName);
        if (currentItem != null
                && currentItem.getItem() == Items.diamond_sword
                && playerClass != null
                && ALERT_CLASSES.contains(playerClass)
                && !alertedPlayers.contains(playerName)) {
            alertedPlayers.add(playerName);
            alertDiamondSword(playerName, playerClass);
        }
        previousEquipment.put(playerName, currentItem != null ? currentItem.copy() : null);
    }

    private void checkDiamondArmor(EntityPlayer player, String playerName) {
        final ItemStack[] currentArmor = new ItemStack[4];
        for (int i = 0; i < currentArmor.length; i++) {
            currentArmor[i] = player.getEquipmentInSlot(i + 1);
        }

        final ItemStack[] previousArmorState = previousArmor.get(playerName);
        final MWClass playerClass = MWClass.ofPlayer(playerName);
        if (playerClass == null) {
            return;
        }

        Set<ArmorType> allowedArmor = CLASS_DIAMOND_ARMOR.get(playerClass);
        if (allowedArmor == null) {
            allowedArmor = new HashSet<>();
        }

        for (int i = 0; i < currentArmor.length; i++) {
            final ItemStack currentPiece = currentArmor[i];
            final ArmorType armorType = getArmorType(i);
            if (!isDiamondArmor(currentPiece) || allowedArmor.contains(armorType)) {
                continue;
            }

            final boolean wasAlerted = previousArmorState != null
                    && i < previousArmorState.length
                    && isDiamondArmor(previousArmorState[i])
                    && !allowedArmor.contains(armorType);
            if (wasAlerted || alertedArmorPlayers.contains(playerName + "_" + armorType)) {
                continue;
            }

            alertDiamondArmor(playerName, playerClass, armorType);
            alertedArmorPlayers.add(playerName + "_" + armorType);
        }

        previousArmor.put(playerName, currentArmor.clone());
    }

    private ArmorType getArmorType(int slotIndex) {
        switch (slotIndex) {
            case 0:
                return ArmorType.BOOTS;
            case 1:
                return ArmorType.LEGGINGS;
            case 2:
                return ArmorType.CHESTPLATE;
            case 3:
                return ArmorType.HELMET;
            default:
                return ArmorType.HELMET;
        }
    }

    private boolean isDiamondArmor(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        return itemStack.getItem() == Items.diamond_helmet
                || itemStack.getItem() == Items.diamond_chestplate
                || itemStack.getItem() == Items.diamond_leggings
                || itemStack.getItem() == Items.diamond_boots;
    }

    private void alertDiamondSword(String playerName, MWClass playerClass) {
        if (mc.thePlayer == null) {
            return;
        }
        final String message = EnumChatFormatting.DARK_AQUA + "[" + EnumChatFormatting.AQUA + "Diamond Detector"
                + EnumChatFormatting.DARK_AQUA + "] " + NameUtil.getFormattedNameWithoutIcons(playerName)
                + EnumChatFormatting.RESET + " is holding a " + EnumChatFormatting.RED + "Diamond Sword"
                + EnumChatFormatting.RESET + "!";
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
        NameUtil.updateMWPlayerDataAndEntityData(playerName, true);
    }

    private void alertDiamondArmor(String playerName, MWClass playerClass, ArmorType armorType) {
        if (mc.thePlayer == null) {
            return;
        }
        final String message = EnumChatFormatting.DARK_AQUA + "[" + EnumChatFormatting.AQUA + "Diamond Detector"
                + EnumChatFormatting.DARK_AQUA + "] " + NameUtil.getFormattedNameWithoutIcons(playerName)
                + EnumChatFormatting.RESET + " is wearing " + EnumChatFormatting.RED + getArmorTypeName(armorType)
                + EnumChatFormatting.RESET + "!";
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }

    private String getClassTagColor(String playerName, MWClass playerClass) {
        final NetworkPlayerInfo netInfo = NetHandlerPlayClientHook_PlayerMapTracker.getPlayerInfo(playerName);
        final String tag = "[" + playerClass.TAG + "]";
        if (netInfo != null && netInfo.getDisplayName() != null) {
            final String formattedText = netInfo.getDisplayName().getFormattedText();
            if (formattedText.indexOf(tag) != -1) {
                return StringUtil.getLastColorCodeBefore(formattedText, tag);
            }
        }
        return getPlayerTeamColor(playerName);
    }

    private String getArmorTypeName(ArmorType armorType) {
        switch (armorType) {
            case HELMET:
                return "DIAMOND HELMET";
            case CHESTPLATE:
                return "DIAMOND CHESTPLATE";
            case LEGGINGS:
                return "DIAMOND LEGGINGS";
            case BOOTS:
                return "DIAMOND BOOTS";
            default:
                return "DIAMOND ARMOR";
        }
    }

    private String getPlayerTeamColor(String playerName) {
        if (mc.theWorld == null) {
            return "";
        }
        ScorePlayerTeam team = mc.theWorld.getScoreboard().getPlayersTeam(playerName);
        if (team == null
                && mc.thePlayer != null
                && mc.thePlayer.getName().equals(playerName)
                && !MWEConfig.hypixelNick.isEmpty()) {
            team = mc.theWorld.getScoreboard().getPlayersTeam(MWEConfig.hypixelNick);
        }
        if (team != null && team.getColorPrefix() != null && !team.getColorPrefix().isEmpty()) {
            return team.getColorPrefix();
        }
        return "";
    }

    public static void resetAlerts() {
        alertedPlayers.clear();
        alertedArmorPlayers.clear();
        previousEquipment.clear();
        previousArmor.clear();
    }

    public static boolean hasBeenAlerted(String playerName) {
        return alertedPlayers.contains(playerName);
    }

    static {
        ALERT_CLASSES.add(MWClass.COW);
        ALERT_CLASSES.add(MWClass.GOLEM);
        ALERT_CLASSES.add(MWClass.ZOMBIE);
        ALERT_CLASSES.add(MWClass.ENDERMAN);
        ALERT_CLASSES.add(MWClass.SQUID);
        ALERT_CLASSES.add(MWClass.CREEPER);
        ALERT_CLASSES.add(MWClass.PIRATE);
        ALERT_CLASSES.add(MWClass.SHEEP);
        ALERT_CLASSES.add(MWClass.SKELETON);
        ALERT_CLASSES.add(MWClass.ANGEL);
        ALERT_CLASSES.add(MWClass.AUTOMATON);
        ALERT_CLASSES.add(MWClass.MOLEMAN);
        ALERT_CLASSES.add(MWClass.PHOENIX);
        ALERT_CLASSES.add(MWClass.DRAGON);

        CLASS_DIAMOND_ARMOR.put(MWClass.COW, new HashSet<>(Arrays.asList(ArmorType.CHESTPLATE)));
        CLASS_DIAMOND_ARMOR.put(MWClass.HUNTER, new HashSet<>(Arrays.asList(ArmorType.HELMET)));
        CLASS_DIAMOND_ARMOR.put(MWClass.SHARK, new HashSet<>(Arrays.asList(ArmorType.BOOTS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.ARCANIST, new HashSet<>(Arrays.asList(ArmorType.LEGGINGS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.HEROBRINE, new HashSet<ArmorType>());
        CLASS_DIAMOND_ARMOR.put(MWClass.DREADLORD, new HashSet<>(Arrays.asList(ArmorType.HELMET)));
        CLASS_DIAMOND_ARMOR.put(MWClass.GOLEM, new HashSet<>(Arrays.asList(ArmorType.CHESTPLATE, ArmorType.BOOTS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.PIGMAN, new HashSet<>(Arrays.asList(ArmorType.CHESTPLATE)));
        CLASS_DIAMOND_ARMOR.put(MWClass.ZOMBIE, new HashSet<>(Arrays.asList(ArmorType.CHESTPLATE)));
        CLASS_DIAMOND_ARMOR.put(MWClass.ENDERMAN, new HashSet<>(Arrays.asList(ArmorType.BOOTS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.SHAMAN, new HashSet<>(Arrays.asList(ArmorType.BOOTS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.SQUID, new HashSet<>(Arrays.asList(ArmorType.BOOTS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.CREEPER, new HashSet<>(Arrays.asList(ArmorType.LEGGINGS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.PIRATE, new HashSet<>(Arrays.asList(ArmorType.HELMET, ArmorType.BOOTS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.SHEEP, new HashSet<>(Arrays.asList(ArmorType.LEGGINGS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.SKELETON, new HashSet<>(Arrays.asList(ArmorType.HELMET)));
        CLASS_DIAMOND_ARMOR.put(MWClass.SPIDER, new HashSet<>(Arrays.asList(ArmorType.BOOTS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.WEREWOLF, new HashSet<>(Arrays.asList(ArmorType.CHESTPLATE)));
        CLASS_DIAMOND_ARMOR.put(MWClass.ANGEL, new HashSet<>(Arrays.asList(ArmorType.LEGGINGS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.ASSASSIN, new HashSet<ArmorType>());
        CLASS_DIAMOND_ARMOR.put(MWClass.AUTOMATON, new HashSet<>(Arrays.asList(ArmorType.LEGGINGS, ArmorType.BOOTS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.MOLEMAN, new HashSet<>(Arrays.asList(ArmorType.LEGGINGS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.PHOENIX, new HashSet<ArmorType>());
        CLASS_DIAMOND_ARMOR.put(MWClass.DRAGON, new HashSet<>(Arrays.asList(ArmorType.HELMET)));
        CLASS_DIAMOND_ARMOR.put(MWClass.RENEGADE, new HashSet<>(Arrays.asList(ArmorType.BOOTS)));
        CLASS_DIAMOND_ARMOR.put(MWClass.SNOWMAN, new HashSet<>(Arrays.asList(ArmorType.LEGGINGS)));
    }

    public enum ArmorType {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }
}
