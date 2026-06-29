package fr.alexdoru.mwe.asm.transformers;

import fr.alexdoru.mwe.asm.loader.InjectionStatus;
import fr.alexdoru.mwe.asm.loader.MWETransformer;
import fr.alexdoru.mwe.asm.mappings.ClassMapping;
import fr.alexdoru.mwe.asm.mappings.MethodMapping;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class RendererLivingEntityTransformer_Renegade implements MWETransformer {

    @Override
    public String[] getTargetClassName() {
        return new String[]{"net.minecraft.client.renderer.entity.RendererLivingEntity"};
    }

    @Override
    public void transform(ClassNode classNode, InjectionStatus status) {
        status.setInjectionPoints(1);
        for (final MethodNode methodNode : classNode.methods) {
            if (checkMethodNode(methodNode, MethodMapping.RENDERERLIVINGENTITY$RENDERNAME)) {
                for (final AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (checkMethodInsnNode(node, MethodMapping.ENTITYLIVINGBASE$GETDISPLAYNAME)) {
                        final AbstractInsnNode nextNode = node.getNext();
                        if (checkMethodInsnNode(nextNode, MethodMapping.ICHATCOMPONENT$GETFORMATTEDTEXT)) {
                            final InsnList list = new InsnList();
                            list.add(new VarInsnNode(ALOAD, 1));
                            list.add(new VarInsnNode(DLOAD, 8));
                            list.add(new MethodInsnNode(
                                    INVOKESTATIC,
                                    getHookClass("RenderHook_Renegade"),
                                    "appendToNametag",
                                    "(Ljava/lang/String;L" + ClassMapping.ENTITYLIVINGBASE + ";D)Ljava/lang/String;",
                                    false
                            ));
                            methodNode.instructions.insert(nextNode, list);
                            status.addInjection();
                        }
                    }
                }
            }
        }
    }

}
