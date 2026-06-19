package fr.alexdoru.mwe.asm.transformers;

import fr.alexdoru.mwe.asm.loader.InjectionStatus;
import fr.alexdoru.mwe.asm.loader.MWETransformer;
import fr.alexdoru.mwe.asm.mappings.MethodMapping;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class BarrierTransformer_VisibleBarriers implements MWETransformer {

    @Override
    public String[] getTargetClassName() {
        return new String[]{"net.minecraft.client.particle.Barrier"};
    }

    @Override
    public void transform(ClassNode classNode, InjectionStatus status) {
        status.setInjectionPoints(1);
        for (final MethodNode methodNode : classNode.methods) {
            if (!checkMethodNode(methodNode, MethodMapping.BARRIER$RENDERPARTICLE)) {
                continue;
            }
            final InsnList list = new InsnList();
            final LabelNode renderParticle = new LabelNode();
            list.add(new MethodInsnNode(
                    INVOKESTATIC,
                    getHookClass("VisibleBarriersHook"),
                    "shouldCancelBarrierParticle",
                    "()Z",
                    false
            ));
            list.add(new JumpInsnNode(IFEQ, renderParticle));
            list.add(new InsnNode(RETURN));
            list.add(renderParticle);
            methodNode.instructions.insert(list);
            status.addInjection();
            return;
        }
    }
}
