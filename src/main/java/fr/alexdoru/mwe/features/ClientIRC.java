package fr.alexdoru.mwe.features;

import fr.alexdoru.mwe.MWE;
import fr.alexdoru.mwe.Wrapper;
import fr.alexdoru.mwe.mark.Marker;
import fr.alexdoru.mwe.utils.LogUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.alexdoru.mwe.MWE.nullcheck;

/**
 * IRC 客户端模块
 * @author LangYa466
 * @since 2025/1/3
 */
public class ClientIRC implements Wrapper {
    private static final String SERVER_ADDRESS = "113.45.185.125";
    private static final int SERVER_PORT = 2233;
    private static BufferedReader bufferedReader;
    private static PrintWriter out;
    private static final Map<String, String> ignMap = new ConcurrentHashMap<>(); //用户名 -> ign
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();


    public static final List<Marker> markList = new ArrayList<>();

    public ClientIRC() {
        connectToServer();
    }

    public static void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
            out = new PrintWriter(socket.getOutputStream());
            LogUtil.log(EnumChatFormatting.GREEN + "IRC连接成功!");
            if (!nullcheck()) {
                out.println(".addIGN@" + MWE.userName + "@" + mc.thePlayer.getName());
                out.flush();
            }
            startMessageListener();
        } catch (IOException e) {
            e.printStackTrace();
            scheduleReconnect();
        }
    }

    private static void startMessageListener() {
        executorService.submit(() -> {
            String message;
            try {
                while ((message = bufferedReader.readLine()) != null) {
                    processMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ignMap.clear();
                scheduleReconnect();
            }
        });
    }

    private static void scheduleReconnect() {
        LogUtil.log(EnumChatFormatting.RED + "IRC连接断开 重连中...");
        executorService.submit(() -> {
            try {
                Thread.sleep(30000);
                connectToServer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void processMessage(String message) {
        String[] args = message.split("@",3);
        if (args.length < 2) return;

        switch (args[0]) {
            case ".message" : {
                if (args.length == 3) {
                    LogUtil.sendIRCMessage(String.format("<%s> %s", args[1], args[2]));
                }
            }
            case ".addIGN" : {
                ignMap.putIfAbsent(args[1], args.length > 3 || args.length == 3 ? args[2] : "");
            }
            case ".removeIGN" : ignMap.remove(args[1]);
        }
    }

    public static void sendMessage(String message) {
        if (message == null || out == null) return;
        out.println(String.format(".message@%s@%s",
                MWE.userName,
                message)
        );
        out.flush();
    }

//    @EventTarget
//    public void onWorldChange(EventWorldChange event) {
//        // anti crasher
//        if (event.getNewWorld() == null || mc.thePlayer == null) return;
//        if (out != null) {
//            out.println(".addIGN@" + Client.instance.userName + "@" + mc.thePlayer.getName());
//        }
//    }

    @SubscribeEvent
    public void onRenderTickEvent(final TickEvent.RenderTickEvent event) {
        MWE.ticks = event.renderTickTime;
    }

    @SubscribeEvent
    public void idk(PlayerEvent.PlayerLoggedInEvent event) {
        if (nullcheck()) return;
        if (out != null) {
            out.println(".addIGN@" + MWE.userName + "@" + mc.thePlayer.getName());
            out.flush();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        markList.removeIf(marker -> System.currentTimeMillis() - marker.getTimestamp().getTime() > 15000L);
    }

    @SubscribeEvent
    public void onMessage(ClientChatReceivedEvent event) {
        Pattern pattern = Pattern.compile("X:([-\\d]+) Y:([-\\d]+) Z:([-\\d]+)");
        Matcher matcher = pattern.matcher(event.message.getUnformattedText());

        if (matcher.find()) {
            LogUtil.addMessage("1");
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            int z = Integer.parseInt(matcher.group(3));

            markList.add(Marker.fromData(x,y,z,"x"));
        }
    }


//    public static String filterTab(String text) {
//        if (ignMap.isEmpty()) return text;
//        if (out == null) return text;
//        StringBuilder textBuilder = new StringBuilder(text);
//        for (String key : ignMap.keySet()) {
//            if (textBuilder.toString().contains(ignMap.get(key))) {
//                textBuilder.insert(0, EnumChatFormatting.AQUA + "[User]" + "[" + key + "]" + EnumChatFormatting.RESET);
//            }
//        }
//        text = textBuilder.toString();
//        return text;
//    }

    public static boolean isIRCUser(EntityPlayer entityPlayer) {
        return ignMap.containsValue(entityPlayer.getName());
    }

    public static boolean isIRCUser(String name) {
        return ignMap.containsValue(name);
    }
}
