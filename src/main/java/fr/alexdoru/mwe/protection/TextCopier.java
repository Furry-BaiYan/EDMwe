package fr.alexdoru.mwe.protection;

import by.radioegor146.nativeobfuscator.Native;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static fr.alexdoru.mwe.protection.VerificationUtils.getHwid;

@Native
public class TextCopier extends JFrame {

    private JTextArea textArea;
    private JButton copyButton;

    public TextCopier() {
        // 设置窗口标题
        setTitle("FAIL: FAILED VERIFY YOUR IDENTITY");

        // 设置窗口大小
        setSize(400, 60);

        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建文本区域，并设置样式
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
        textArea.setBackground(Color.WHITE);
        textArea.setEditable(false); // 设置文本区域不可编辑
        textArea.setText(getHwid());

        // 创建滚动窗格，将文本区域放入其中
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // 创建复制按钮，并设置样式
        copyButton = new JButton("Copy");
        copyButton.setFont(new Font("微软雅黑", Font.BOLD, 12)); // 减小字体大小
        copyButton.setBackground(new Color(204, 232, 204));
        copyButton.setForeground(Color.BLACK);
        copyButton.setBorder(BorderFactory.createRaisedBevelBorder());

        // 为按钮添加点击事件监听器
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyTextToClipboard();
            }
        });

        // 使用BorderLayout布局管理器
        setLayout(new BorderLayout(10, 10)); // 设置水平和垂直间距为10

        // 将滚动窗格添加到窗口中心
        add(scrollPane, BorderLayout.CENTER);

        // 将复制按钮添加到窗口右侧
        add(copyButton, BorderLayout.EAST);

        // 设置窗口居中显示
        setLocationRelativeTo(null);
    }

    private void copyTextToClipboard() {
        // 获取文本区域的内容
        String text = textArea.getText();

        // 创建StringSelection对象
        StringSelection selection = new StringSelection(text);

        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 将文本内容设置到剪贴板
        clipboard.setContents(selection, selection);
    }

    public static void main(String[] args) {
        // 创建并显示窗口
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TextCopier textCopier = new TextCopier();
                textCopier.setVisible(true);
            }
        });
    }
}
