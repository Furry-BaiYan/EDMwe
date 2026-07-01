package fr.alexdoru.mwe.asm.hooks;

import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.features.TornadoCheck;
import fr.alexdoru.mwe.hackerdetector.AttackDetector;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2APacketParticles;

@SuppressWarnings("unused")
public class NetworkManagerHook_PacketListener {

    // Careful, this code isn't called from the main thread
    // Only listens packets that throw a ThreadQuickExitException
    public static void listen(Packet<?> packet) {
        if (MWEConfig.hackerDetector) {
            try {
                AttackDetector.lookForAttacks(packet);
            } catch (Throwable ignored) {}
        }
        if (packet instanceof S2APacketParticles) {
            if (MWEConfig.tornadoCheck) {
                try {
                    TornadoCheck.handleParticlePacket((S2APacketParticles) packet);
                } catch (Throwable ignored) {}
            }
        }
    }

    public static void listenSentPacket(Packet<?> packet) {}

}
