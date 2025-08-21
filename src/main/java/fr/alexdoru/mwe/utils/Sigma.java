package fr.alexdoru.mwe.utils;//


import javax.swing.*;
import java.io.*;

public class Sigma {
    public static String user = null;


public static void initUser() throws IOException {
    File folder = new File("C:\\.EDMwe");
    if (!folder.exists()) {
        boolean folderCreated = folder.mkdirs();
        if (!folderCreated) {
            throw new IOException("无法创建文件夹");
        }
    }
    File file = new File(folder, "user.json");
    if (!file.exists()) {
        String user = null;
        while (user == null) {
            user = JOptionPane.showInputDialog("请输入你的用户名(不可少于四位):", null);
        }
        while (user.length() < 4) {
            user = JOptionPane.showInputDialog("请输入你的用户名(不可少于四位):", null);
        }
        writeToFile(file.toString(), user);
    } else {
        user = readFromFile(file.toString());
    }
}

    public static String readFromFile(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line); // 将读取的每一行加到内容中
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString(); // 返回完整文件内容
    }
    public static void writeToFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public void Load(String[] p_main_0_) {
//        // 新建线程启动程序
//        new Thread(() -> {
//            try {
//                initUser();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            while (user != null){
//                LaunchWrapper.doMain(p_main_0_);
//            }
//        }).start();
//    }
}
