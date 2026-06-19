package fr.alexdoru.mwe.asm.transformers;

import fr.alexdoru.mwe.asm.loader.InjectionStatus;
import fr.alexdoru.mwe.asm.loader.MWETransformer;
import fr.alexdoru.mwe.asm.mappings.ClassMapping;
import fr.alexdoru.mwe.asm.mappings.MethodMapping;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BlockBarrierTransformer_VisibleBarriers implements MWETransformer {

    @Override
    public String[] getTargetClassName() {
        return new String[]{"net.minecraft.block.BlockBarrier"};
    }

    @Override
    public void transform(ClassNode classNode, InjectionStatus status) {
        status.setInjectionPoints(3);
        boolean hasBlockLayerOverride = false;
        boolean hasSideRenderOverride = false;

        for (final MethodNode methodNode : classNode.methods) {
            if (checkMethodNode(methodNode, MethodMapping.BLOCKBARRIER$GETRENDERTYPE)) {
                rewriteGetRenderType(methodNode);
                status.addInjection();
            } else if (checkMethodNode(methodNode, MethodMapping.BLOCKBARRIER$GETBLOCKLAYER)) {
                rewriteGetBlockLayer(methodNode);
                hasBlockLayerOverride = true;
                status.addInjection();
            } else if (checkMethodNode(methodNode, MethodMapping.BLOCKBARRIER$SHOULDSIDEBERENDERED)) {
                rewriteShouldSideBeRendered(methodNode);
                hasSideRenderOverride = true;
                status.addInjection();
            }
        }

        if (!hasBlockLayerOverride) {
            final MethodNode methodNode = new MethodNode(
                    ACC_PUBLIC,
                    MethodMapping.BLOCKBARRIER$GETBLOCKLAYER.name,
                    MethodMapping.BLOCKBARRIER$GETBLOCKLAYER.desc,
                    null,
                    null
            );
            rewriteGetBlockLayer(methodNode);
            classNode.methods.add(methodNode);
            status.addInjection();
        }

        if (!hasSideRenderOverride) {
            final MethodNode methodNode = new MethodNode(
                    ACC_PUBLIC,
                    MethodMapping.BLOCKBARRIER$SHOULDSIDEBERENDERED.name,
                    MethodMapping.BLOCKBARRIER$SHOULDSIDEBERENDERED.desc,
                    null,
                    null
            );
            rewriteShouldSideBeRendered(methodNode);
            classNode.methods.add(methodNode);
            status.addInjection();
        }
    }

    private void rewriteGetRenderType(MethodNode methodNode) {
        clearMethod(methodNode);
        methodNode.instructions.add(new MethodInsnNode(
                INVOKESTATIC,
                getHookClass("VisibleBarriersHook"),
                "getRenderType",
                "()I",
                false
        ));
        methodNode.instructions.add(new InsnNode(IRETURN));
    }

    private void rewriteGetBlockLayer(MethodNode methodNode) {
        clearMethod(methodNode);
        methodNode.instructions.add(new MethodInsnNode(
                INVOKESTATIC,
                getHookClass("VisibleBarriersHook"),
                "getBlockLayer",
                "()L" + ClassMapping.ENUMWORLDBLOCKLAYER + ";",
                false
        ));
        methodNode.instructions.add(new InsnNode(ARETURN));
    }

    private void rewriteShouldSideBeRendered(MethodNode methodNode) {
        clearMethod(methodNode);
        methodNode.instructions.add(new VarInsnNode(ALOAD, 0));
        methodNode.instructions.add(new VarInsnNode(ALOAD, 1));
        methodNode.instructions.add(new VarInsnNode(ALOAD, 2));
        methodNode.instructions.add(new VarInsnNode(ALOAD, 3));
        methodNode.instructions.add(new MethodInsnNode(
                INVOKESTATIC,
                getHookClass("VisibleBarriersHook"),
                "shouldSideBeRendered",
                "(L" + ClassMapping.BLOCK + ";L" + ClassMapping.IBLOCKACCESS + ";L" + ClassMapping.BLOCKPOS + ";L" + ClassMapping.ENUMFACING + ";)Z",
                false
        ));
        methodNode.instructions.add(new InsnNode(IRETURN));
    }

    private void clearMethod(MethodNode methodNode) {
        methodNode.instructions.clear();
        methodNode.tryCatchBlocks.clear();
        if (methodNode.localVariables != null) {
            methodNode.localVariables.clear();
        }
    }
}
