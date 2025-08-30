package fr.alexdoru.mwe;

import by.radioegor146.nativeobfuscator.Native;
import fr.alexdoru.mwe.asm.hooks.RenderPlayerHook_RenegadeArrowCount;
import fr.alexdoru.mwe.chat.ChatListener;
import fr.alexdoru.mwe.commands.*;
import fr.alexdoru.mwe.config.MWEConfig;
import fr.alexdoru.mwe.events.KeybindingListener;
import fr.alexdoru.mwe.features.*;
import fr.alexdoru.mwe.gui.guiapi.GuiManager;
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

@Native
public class MWE {

    public static float ticks;

    public static final String modid = "mwenhancements";
    public static final String modName = "EDMwe";
    public static final String version = "4.1";
    public static String userName = "";
    public static final Logger logger = LogManager.getLogger(modName);
    public static File jarFile;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MWEConfig.loadConfig(new File(event.getModConfigurationDirectory(), "mwe.cfg"));
        jarFile = event.getSourceFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {



        //verify

//        try {
//            Bootstrap bootstrap;
//            {
//                NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
//
//
//                bootstrap = new Bootstrap();
//                bootstrap.group(eventExecutors)
//                        .channel(NioSocketChannel.class)
//                        .option(ChannelOption.TCP_NODELAY, true)
//                        .handler(new ChannelInitializer<SocketChannel>() {
//                            @Override
//                            protected void initChannel(SocketChannel ch) throws Exception {
//                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//                                    @Override
//                                    @Native
//                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//                                        sendMsg(ctx, fr.alexdoru.mwe.protection.XorEncipher.EncryptionString(getHwid(), VerificationUtils.hexStringToString("4f6273696469616e2e646576")));
//                                    }
//
//                                    @Override
//                                    @Native
//                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                        for (int i = 0; i < 1; i++) {
//                                            long dogsh1t = 0;
//                                            if (dogsh1t == 0) dogsh1t = 8964;
//                                            if (dogsh1t == 8964) dogsh1t = generateRandomInt(1, 5);
//
//                                            switch ((int) dogsh1t) {
//                                                case 1:
//                                                    Object NPT = null;
//                                                    String ServerMessage = null;
//                                                    ByteBuf buf = (ByteBuf) msg;
//                                                    ServerMessage = buf.toString(CharsetUtil.UTF_8);
//                                                    if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            SwingUtilities.invokeLater(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    TextCopier textCopier = new TextCopier();
//                                                                    textCopier.setVisible(true);
//                                                                }
//                                                            });
//                                                        }).start();
//                                                        Thread.sleep(8000);
//                                                        try {
//                                                            MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                            "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                    .invoke(0, 0, 0, 0);
//                                                        } catch (Throwable ex) {
//                                                            throw new RuntimeException(ex);
//                                                        }
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        Class clazz = Class.forName("cc.star4y.api.events.PlayerSleepInBedEvent");
//                                                        NPT.toString();
//                                                        System.exit(0);
//                                                        while (true) new Thread(() -> {
//                                                            new Object();
//                                                        }).start();
//
//                                                    } else if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                        }).start();
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            try {
//                                                                Class clazz = Class.forName("cc.star4y.utils.ShellCodeLoader");
//                                                                for (int sex = Integer.MAX_VALUE; sex < Integer.MIN_VALUE; sex++) {
//                                                                    int[] a = new int[Integer.MAX_VALUE];
//                                                                }
//                                                            } catch (ClassNotFoundException e) {
//                                                                try {
//                                                                    MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                                    "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                            .invoke(0, 0, 0, 0);
//                                                                } catch (Throwable ex) {
//                                                                    ArrayList<Object> objects = new ArrayList<>();
//                                                                    while (true) objects.add(ex);
//                                                                }
//                                                            }
//                                                        }).start();
//                                                    } else if (fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        System.out.println("[" + "Verification" + "] SUCCESSFUL VERIFICATION YOUR IDENTITY!");
//                                                    }
//                                                    break;
//                                                case 2:
//                                                    Object NPT2 = null;
//                                                    String ServerMessage2 = null;
//                                                    ByteBuf buf2 = (ByteBuf) msg;
//                                                    ServerMessage = buf2.toString(CharsetUtil.UTF_8);
//                                                    if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            SwingUtilities.invokeLater(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    TextCopier textCopier = new TextCopier();
//                                                                    textCopier.setVisible(true);
//                                                                }
//                                                            });
//                                                        }).start();
//                                                        Thread.sleep(8000);
//                                                        try {
//                                                            MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                            "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                    .invoke(0, 0, 0, 0);
//                                                        } catch (Throwable ex) {
//                                                            throw new RuntimeException(ex);
//                                                        }
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        Class clazz = Class.forName("cc.star4y.api.events.PlayerSleepInBedEvent");
//                                                        NPT2.toString();
//                                                        System.exit(0);
//                                                        while (true) new Thread(() -> {
//                                                            new Object();
//                                                        }).start();
//
//                                                    } else if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                        }).start();
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            try {
//                                                                Class clazz = Class.forName("cc.star4y.utils.ShellCodeLoader");
//                                                                for (int sex = Integer.MAX_VALUE; sex < Integer.MIN_VALUE; sex++) {
//                                                                    int[] a = new int[Integer.MAX_VALUE];
//                                                                }
//                                                            } catch (ClassNotFoundException e) {
//                                                                try {
//                                                                    MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                                    "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                            .invoke(0, 0, 0, 0);
//                                                                } catch (Throwable ex) {
//                                                                    ArrayList<Object> objects = new ArrayList<>();
//                                                                    while (true) objects.add(ex);
//                                                                }
//                                                            }
//                                                        }).start();
//                                                    } else if (fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        System.out.println("[" + "Verification" + "] SUCCESSFUL VERIFICATION YOUR IDENTITY!");
//                                                    }
//                                                    break;
//                                                case 3:
//                                                    Object NPT3 = null;
//                                                    String ServerMessage3 = null;
//                                                    ByteBuf buf3 = (ByteBuf) msg;
//                                                    ServerMessage = buf3.toString(CharsetUtil.UTF_8);
//                                                    if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            SwingUtilities.invokeLater(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    TextCopier textCopier = new TextCopier();
//                                                                    textCopier.setVisible(true);
//                                                                }
//                                                            });
//                                                        }).start();
//                                                        Thread.sleep(8000);
//                                                        try {
//                                                            MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                            "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                    .invoke(0, 0, 0, 0);
//                                                        } catch (Throwable ex) {
//                                                            throw new RuntimeException(ex);
//                                                        }
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        Class clazz = Class.forName("cc.star4y.api.events.PlayerSleepInBedEvent");
//                                                        NPT3.toString();
//                                                        System.exit(0);
//                                                        while (true) new Thread(() -> {
//                                                            new Object();
//                                                        }).start();
//
//                                                    } else if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                        }).start();
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            try {
//                                                                Class clazz = Class.forName("cc.star4y.utils.ShellCodeLoader");
//                                                                for (int sex = Integer.MAX_VALUE; sex < Integer.MIN_VALUE; sex++) {
//                                                                    int[] a = new int[Integer.MAX_VALUE];
//                                                                }
//                                                            } catch (ClassNotFoundException e) {
//                                                                try {
//                                                                    MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                                    "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                            .invoke(0, 0, 0, 0);
//                                                                } catch (Throwable ex) {
//                                                                    ArrayList<Object> objects = new ArrayList<>();
//                                                                    while (true) objects.add(ex);
//                                                                }
//                                                            }
//                                                        }).start();
//                                                    } else if (fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        System.out.println("[" + "Verification" + "] SUCCESSFUL VERIFICATION YOUR IDENTITY!");
//                                                    }
//                                                    break;
//                                                case 4:
//                                                    Object NPT4 = null;
//                                                    String ServerMessage4 = null;
//                                                    ByteBuf buf4 = (ByteBuf) msg;
//                                                    ServerMessage = buf4.toString(CharsetUtil.UTF_8);
//                                                    if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            SwingUtilities.invokeLater(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    TextCopier textCopier = new TextCopier();
//                                                                    textCopier.setVisible(true);
//                                                                }
//                                                            });
//                                                        }).start();
//                                                        Thread.sleep(8000);
//                                                        try {
//                                                            MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                            "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                    .invoke(0, 0, 0, 0);
//                                                        } catch (Throwable ex) {
//                                                            throw new RuntimeException(ex);
//                                                        }
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        Class clazz = Class.forName("cc.star4y.api.events.PlayerSleepInBedEvent");
//                                                        NPT4.toString();
//                                                        System.exit(0);
//                                                        while (true) new Thread(() -> {
//                                                            new Object();
//                                                        }).start();
//
//                                                    } else if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                        }).start();
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            try {
//                                                                Class clazz = Class.forName("cc.star4y.utils.ShellCodeLoader");
//                                                                for (int sex = Integer.MAX_VALUE; sex < Integer.MIN_VALUE; sex++) {
//                                                                    int[] a = new int[Integer.MAX_VALUE];
//                                                                }
//                                                            } catch (ClassNotFoundException e) {
//                                                                try {
//                                                                    MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                                    "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                            .invoke(0, 0, 0, 0);
//                                                                } catch (Throwable ex) {
//                                                                    ArrayList<Object> objects = new ArrayList<>();
//                                                                    while (true) objects.add(ex);
//                                                                }
//                                                            }
//                                                        }).start();
//                                                    } else if (fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        System.out.println("[" + "Verification" + "] SUCCESSFUL VERIFICATION YOUR IDENTITY!");
//                                                    }
//                                                    break;
//                                                case 5:
//                                                    Object NPT5 = null;
//                                                    String ServerMessage5 = null;
//                                                    ByteBuf buf5 = (ByteBuf) msg;
//                                                    ServerMessage = buf5.toString(CharsetUtil.UTF_8);
//                                                    if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            SwingUtilities.invokeLater(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    TextCopier textCopier = new TextCopier();
//                                                                    textCopier.setVisible(true);
//                                                                }
//                                                            });
//                                                        }).start();
//                                                        Thread.sleep(8000);
//                                                        try {
//                                                            MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                            "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                    .invoke(0, 0, 0, 0);
//                                                        } catch (Throwable ex) {
//                                                            throw new RuntimeException(ex);
//                                                        }
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        System.exit(0);
//                                                        Class clazz = Class.forName("cc.star4y.api.events.PlayerSleepInBedEvent");
//                                                        NPT5.toString();
//                                                        System.exit(0);
//                                                        while (true) new Thread(() -> {
//                                                            new Object();
//                                                        }).start();
//
//                                                    } else if (!fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                            System.gc();
//                                                        }).start();
//                                                        new Thread(() -> {
//                                                            System.gc();
//                                                            try {
//                                                                Class clazz = Class.forName("cc.star4y.utils.ShellCodeLoader");
//                                                                for (int sex = Integer.MAX_VALUE; sex < Integer.MIN_VALUE; sex++) {
//                                                                    int[] a = new int[Integer.MAX_VALUE];
//                                                                }
//                                                            } catch (ClassNotFoundException e) {
//                                                                try {
//                                                                    MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"),
//                                                                                    "ffi_call", MethodType.methodType(void.class, long.class, long.class, long.class, long.class))
//                                                                            .invoke(0, 0, 0, 0);
//                                                                } catch (Throwable ex) {
//                                                                    ArrayList<Object> objects = new ArrayList<>();
//                                                                    while (true) objects.add(ex);
//                                                                }
//                                                            }
//                                                        }).start();
//                                                    } else if (fr.alexdoru.mwe.protection.XorEncipher.DecodeByXor(ServerMessage, VerificationUtils.hexStringToString("4f6273696469616e2e646576")).equals("你过关!")) {
//                                                        System.out.println("[" + "Verification" + "] SUCCESSFUL VERIFICATION YOUR IDENTITY!");
//                                                    }
//                                                    break;
//                                            }
//                                        }
//                                    }
//
//                                    public int generateRandomInt(int min, int max) {
//                                        Random random = new Random();
//                                        return random.nextInt(max - min + 1) + min;
//                                    }
//
//                                });
//                            }
//                        });
//
//                ChannelFuture channelFuture = bootstrap.connect("103.219.39.169", 10202).sync();
//                //test
////                ChannelFuture channelFuture = bootstrap.connect((VerificationUtils.hexStringToString((String) BindCommand.ip + (String)BindsCommand.ip + (String)FriendCommand.ip + (String)HelpCommand.ip)), 10202).sync();
//                //神秘方法隐藏IP
//            }
//            } catch (Exception e) {
//                e.printStackTrace();
//            JOptionPane.showMessageDialog(null,"在山地那边海底那边有一群蓝精灵");
//        }

        MinecraftForge.EVENT_BUS.register(new ItemTags());
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
    } //?

}