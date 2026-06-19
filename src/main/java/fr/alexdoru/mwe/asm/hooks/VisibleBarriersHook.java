package fr.alexdoru.mwe.asm.hooks;

import fr.alexdoru.mwe.features.VisibleBarriers;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;

import java.util.Set;

public final class VisibleBarriersHook {

    private VisibleBarriersHook() {
    }

    public static void removeBarrierFromBuiltInBlocks(Set<Block> builtInBlocks) {
        builtInBlocks.remove(Blocks.barrier);
    }

    public static int getRenderType() {
        return VisibleBarriers.isVisible() ? 3 : -1;
    }

    public static EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    public static boolean shouldSideBeRendered(Block barrier, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return world.getBlockState(pos).getBlock() != barrier;
    }

    public static boolean shouldCancelBarrierParticle() {
        return VisibleBarriers.isVisible();
    }
}
