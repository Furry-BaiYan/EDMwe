package fr.alexdoru.mwe.asm.transformers;

import fr.alexdoru.mwe.asm.loader.InjectionStatus;
import fr.alexdoru.mwe.asm.loader.MWETransformer;
import fr.alexdoru.mwe.asm.mappings.FieldMapping;
import fr.alexdoru.mwe.asm.mappings.MethodMapping;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BlockStateMapperTransformer_VisibleBarriers implements MWETransformer {

    @Override
    public String[] getTargetClassName() {
        return new String[]{"net.minecraft.client.renderer.block.statemap.BlockStateMapper"};
    }

    @Override
    public void transform(ClassNode classNode, InjectionStatus status) {
        status.setInjectionPoints(1);
        for (final MethodNode methodNode : classNode.methods) {
            if (!checkMethodNode(methodNode, MethodMapping.BLOCKSTATEMAPPER$REGISTERBUILTINBLOCKS)) {
                continue;
            }
            for (final AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
                if (!checkInsnNode(insnNode, RETURN)) {
                    continue;
                }
                final InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(getNewFieldInsnNode(GETFIELD, FieldMapping.BLOCKSTATEMAPPER$SETBUILTINBLOCKS));
                list.add(new MethodInsnNode(
                        INVOKESTATIC,
                        getHookClass("VisibleBarriersHook"),
                        "removeBarrierFromBuiltInBlocks",
                        "(Ljava/util/Set;)V",
                        false
                ));
                methodNode.instructions.insertBefore(insnNode, list);
                status.addInjection();
                return;
            }
        }
    }
}
