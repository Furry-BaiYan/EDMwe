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

public class GuiPlayerTabOverlayTransformer_PhxRespawnIcons implements MWETransformer {

    @Override
    public String[] getTargetClassName() {
        return new String[]{"net.minecraft.client.gui.GuiPlayerTabOverlay"};
    }

    @Override
    public void transform(ClassNode classNode, InjectionStatus status) {
        status.setInjectionPoints(4);
        for (final MethodNode methodNode : classNode.methods) {
            if (checkMethodNode(methodNode, MethodMapping.GUIPLAYERTABOVERLAY$GETPLAYERNAME)) {
                injectNameSpacing(methodNode, status);
            } else if (checkMethodNode(methodNode, MethodMapping.GUIPLAYERTABOVERLAY$RENDERPLAYERLIST)) {
                injectIconRendering(methodNode, status);
            }
        }
    }

    private void injectNameSpacing(MethodNode methodNode, InjectionStatus status) {
        for (final AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
            if (!checkInsnNode(insnNode, ARETURN)) {
                continue;
            }
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new MethodInsnNode(
                    INVOKESTATIC,
                    getHookClass("GuiPlayerTabOverlayHook_PhxRespawnIcons"),
                    "addIconSpacing",
                    "(Ljava/lang/String;L" + ClassMapping.NETWORKPLAYERINFO + ";)Ljava/lang/String;",
                    false
            ));
            methodNode.instructions.insertBefore(insnNode, list);
            status.addInjection();
        }
    }

    private void injectIconRendering(MethodNode methodNode, InjectionStatus status) {
        int playerInfoIndex = -1;
        int playerNameIndex = -1;

        for (final AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
            if (checkMethodInsnNode(insnNode, MethodMapping.GUIPLAYERTABOVERLAY$GETPLAYERNAME)) {
                final AbstractInsnNode previousNode = insnNode.getPrevious();
                final AbstractInsnNode nextNode = insnNode.getNext();
                if (checkVarInsnNode(previousNode, ALOAD) && checkVarInsnNode(nextNode, ASTORE)) {
                    playerInfoIndex = ((VarInsnNode) previousNode).var;
                    playerNameIndex = ((VarInsnNode) nextNode).var;
                }
                continue;
            }

            if (playerInfoIndex < 0 || playerNameIndex < 0 || !checkMethodInsnNode(insnNode, MethodMapping.FONTRENDERER$DRAWSTRINGWITHSHADOW)) {
                continue;
            }

            final int[] coordinates = findNameCoordinates(insnNode, playerNameIndex);
            if (coordinates == null) {
                continue;
            }

            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, playerInfoIndex));
            list.add(new VarInsnNode(ALOAD, playerNameIndex));
            list.add(new VarInsnNode(ILOAD, coordinates[0]));
            list.add(new VarInsnNode(ILOAD, coordinates[1]));
            list.add(new MethodInsnNode(
                    INVOKESTATIC,
                    getHookClass("GuiPlayerTabOverlayHook_PhxRespawnIcons"),
                    "renderIcon",
                    "(L" + ClassMapping.NETWORKPLAYERINFO + ";Ljava/lang/String;II)V",
                    false
            ));
            methodNode.instructions.insert(insnNode, list);
            status.addInjection();
        }
    }

    private int[] findNameCoordinates(AbstractInsnNode drawStringNode, int playerNameIndex) {
        int yIndex = -1;
        int xIndex = -1;
        boolean foundPlayerName = false;
        AbstractInsnNode cursor = drawStringNode.getPrevious();

        for (int inspected = 0; cursor != null && inspected < 12; inspected++, cursor = cursor.getPrevious()) {
            if (checkVarInsnNode(cursor, ILOAD)) {
                if (yIndex < 0) {
                    yIndex = ((VarInsnNode) cursor).var;
                } else if (xIndex < 0) {
                    xIndex = ((VarInsnNode) cursor).var;
                }
            } else if (checkVarInsnNode(cursor, ALOAD, playerNameIndex)) {
                foundPlayerName = true;
                break;
            }
        }

        return foundPlayerName && xIndex >= 0 && yIndex >= 0 ? new int[]{xIndex, yIndex} : null;
    }
}
