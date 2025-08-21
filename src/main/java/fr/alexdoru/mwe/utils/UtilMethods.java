//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Deobfuscator3000\mappings"!

//Decompiled by Procyon!

package fr.alexdoru.mwe.utils;

import fr.alexdoru.mwe.MWE;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class UtilMethods
{
    public static double distanceTo3D(final Entity e, final BlockPos bp) {
        return Math.sqrt(Math.pow(e.getPositionEyes(MWE.ticks).xCoord - bp.getX(), 2.0) + Math.pow(e.getPositionEyes(MWE.ticks).yCoord - bp.getY(), 2.0) + Math.pow(e.getPositionEyes(MWE.ticks).zCoord - bp.getZ(), 2.0));
    }
    
    public static double distanceTo2D(final Entity e, final BlockPos bp) {
        return Math.sqrt(Math.pow(e.getPositionEyes(MWE.ticks).xCoord - bp.getX(), 2.0) + Math.pow(e.getPositionEyes(MWE.ticks).zCoord - bp.getZ(), 2.0));
    }
}
