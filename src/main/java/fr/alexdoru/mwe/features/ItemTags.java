//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Deobfuscator3000\mappings"!

//Decompiled by Procyon!

package fr.alexdoru.mwe.features;

import by.radioegor146.nativeobfuscator.Native;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.utils.RenderUtil;
import net.minecraftforge.client.event.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Native
public class ItemTags
{
    @SubscribeEvent
    public void render(final RenderWorldLastEvent event) {
        if (!MWEConfig.itemTags) {
            return;
        }
        for (final Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity instanceof EntityItem) {
                final EntityItem entityItem = (EntityItem)entity;
                final ItemStack itemStack = entityItem.getEntityItem();
                final Item item = itemStack.getItem();
                final String itemName = itemStack.getDisplayName();
                final String stripItemName = StringUtils.stripControlCodes(itemStack.getDisplayName());
                final int stackSize = itemStack.stackSize;
                if (stripItemName.startsWith("Phoenix's Tears of Regen") && MWEConfig.phxLoot) {
                    renderItemTag(entity, "§6Phoenix's Tears of Regen §fx" + stackSize, -1, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (stripItemName.startsWith("Squid's Absorption") && MWEConfig.squLoot) {
                    renderItemTag(entity, "§9Squid's Absorption §fx" + stackSize, -1, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (stripItemName.startsWith("Matey") && MWEConfig.pirLoot) {
                    renderItemTag(entity, itemName + " §fx" + stackSize, -1, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (stripItemName.startsWith("Regen-Ade") && MWEConfig.renLoot) {
                    renderItemTag(entity, "§bRegen-ades §fx" + stackSize, -1, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (stripItemName.startsWith("Ultra Pasteurized Milk Bucket") && MWEConfig.cowLoot) {
                    renderItemTag(entity, "§fMilk Bucket §fx" + stackSize, -1, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (stripItemName.startsWith("Junk Apple") && MWEConfig.molLoot) {
                    renderItemTag(entity, itemName + " §fx" + stackSize, -1, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (item == Items.pumpkin_pie && MWEConfig.molLoot1) {
                    renderItemTag(entity, itemName + " §fx" + stackSize, 16711610, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (item == Items.golden_apple && MWEConfig.hunLoot) {
                    renderItemTag(entity, itemName + " §fx" + stackSize, 16776260, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (item == Items.diamond && MWEConfig.diamond) {
                    renderItemTag(entity, itemName + " §fx" + stackSize, 3132391, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (item == Items.diamond_sword && MWEConfig.diamondSword) {
                    renderItemTag(entity, itemName + " §fx" + stackSize, 3132391, event.partialTicks, MWEConfig.itemTagScale / 100f);
                }
                if (!isDiamondArmor(item) || !MWEConfig.diamondArmor) {
                    continue;
                }
                renderItemTag(entity, itemName + " §fx" + stackSize, 3132391, event.partialTicks, MWEConfig.itemTagScale / 100f);
            }
        }
    }
    
    private boolean isDiamondArmor(final Item item) {
        return item == Items.diamond_boots || item == Items.diamond_chestplate || item == Items.diamond_helmet || item == Items.diamond_leggings;
    }
    
    private void renderItemTag(final Entity entity, final String text, final int color, final float partialTicks, final float scale) {
        RenderUtil.renderItemTag(entity, text, color, partialTicks, scale);
    }
}
