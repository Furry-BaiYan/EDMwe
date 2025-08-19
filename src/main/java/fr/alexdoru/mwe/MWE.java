package fr.alexdoru.mwe;

import fr.alexdoru.mwe.asm.hooks.RenderPlayerHook_RenegadeArrowCount;
import fr.alexdoru.mwe.chat.ChatListener;
import fr.alexdoru.mwe.commands.*;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.events.KeybindingListener;
import fr.alexdoru.mwe.features.*;
import fr.alexdoru.mwe.gui.guiapi.GuiManager;
import fr.alexdoru.mwe.gui.huds.PlayerStrengthHUD;
import fr.alexdoru.mwe.hackerdetector.HackerDetector;
import fr.alexdoru.mwe.nocheaters.PlayerJoinListener;
import fr.alexdoru.mwe.nocheaters.ReportQueue;
import fr.alexdoru.mwe.nocheaters.WdrData;
import fr.alexdoru.mwe.scoreboard.ScoreboardTracker;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static fr.alexdoru.mwe.Wrapper.mc;

@Mod(
        modid = MWE.modid,
        name = MWE.modName,
        version = MWE.version,
        acceptedMinecraftVersions = "[1.8.9]",
        clientSideOnly = true)
public class MWE {

    public static final String modid = "mwenhancements";
    public static final String modName = "EDMwe";
    public static final String version = "4.1";
    public static final Logger logger = LogManager.getLogger(modName);
    public static File jarFile;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MWEConfig.loadConfig(new File(event.getModConfigurationDirectory(), "mwe.cfg"));
        jarFile = event.getSourceFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new PhxRespawnListener());
        MinecraftForge.EVENT_BUS.register(new WdrData());
        MinecraftForge.EVENT_BUS.register(new GuiManager());
        MinecraftForge.EVENT_BUS.register(new ReportQueue());
        MinecraftForge.EVENT_BUS.register(new ChatListener());
        MinecraftForge.EVENT_BUS.register(new SquadHandler());
        MinecraftForge.EVENT_BUS.register(new HackerDetector());
        MinecraftForge.EVENT_BUS.register(new LowHPIndicator());
        MinecraftForge.EVENT_BUS.register(new FinalKillCounter());
        MinecraftForge.EVENT_BUS.register(new StrengthTracker());
        MinecraftForge.EVENT_BUS.register(new ScoreboardTracker());
        MinecraftForge.EVENT_BUS.register(new PlayerJoinListener());
        MinecraftForge.EVENT_BUS.register(new KeybindingListener());
        MinecraftForge.EVENT_BUS.register(new MegaWallsEndGameStats());
        MinecraftForge.EVENT_BUS.register(new RenderPlayerHook_RenegadeArrowCount());

        ClientCommandHandler.instance.registerCommand(new CommandMWE());
        ClientCommandHandler.instance.registerCommand(new CommandWDR());
        ClientCommandHandler.instance.registerCommand(new CommandName());
        ClientCommandHandler.instance.registerCommand(new CommandUnWDR());
        ClientCommandHandler.instance.registerCommand(new CommandSquad());
        ClientCommandHandler.instance.registerCommand(new CommandStalk());
        ClientCommandHandler.instance.registerCommand(new CommandReport());
        ClientCommandHandler.instance.registerCommand(new CommandPlancke());
        ClientCommandHandler.instance.registerCommand(new CommandAddAlias());
        ClientCommandHandler.instance.registerCommand(new CommandScanGame());
        ClientCommandHandler.instance.registerCommand(new CommandFKCounter());
        ClientCommandHandler.instance.registerCommand(new CommandNocheaters());
        ClientCommandHandler.instance.registerCommand(new CommandHypixelShout());

    }

    public static boolean nullcheck() {
        return mc.thePlayer == null || mc.theWorld == null;
    }

}