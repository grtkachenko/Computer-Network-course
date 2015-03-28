package dev.utils;

import dev.threads.TCPReceiverHandler;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogGUI {
    JFrame frame;
    static public JTextArea logArea = newTextArea("log"),
            curNetwork = newTextArea("network"),
            state = newTextArea("state"),
            error = newTextArea("error"),
            entries = newTextArea("entries"),
            files = newTextArea("files");

    public LogGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Log");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        GridLayout grid = new GridLayout(3, 2);
        frame.setLayout(grid);

        DefaultCaret caret = (DefaultCaret) logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollLog = new JScrollPane(logArea);

        JPanel addEntry = new JPanel();
        addEntry.setLayout(new FlowLayout());
        final JTextField keyField = new JTextField();
        final JTextField valueField = new JTextField();
        keyField.setPreferredSize(new Dimension(180, 24));
        valueField.setPreferredSize(new Dimension(180, 24));
        addEntry.add(keyField);
        addEntry.add(valueField);
        JButton addEntryBtn = new JButton("add");
        addEntryBtn.setPreferredSize(new Dimension(100, 25));
        addEntryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                String value = valueField.getText();
                TCPReceiverHandler.addEntry(key, value);
            }
        });
        addEntry.add(addEntryBtn);
        frame.add(addEntry);


        frame.add(scrollLog);
        frame.add(curNetwork);
        frame.add(state);
        frame.add(error);
        frame.add(entries);
        frame.add(files);
    }

    static public void log(String s) {
        logArea.append(s + "\n");
        // logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    static public void error(String s) {
        error.append(s + "\n");
        error.setCaretPosition(error.getDocument().getLength());
    }

    static public JTextArea newTextArea(String name) {
        JTextArea res = new JTextArea();
        res.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        res.setEditable(false);

        return res;
    }

    public void run() {
//        try {
//            while (true) {
//                ArrayList<InetAddress> addrs = Utils.getNetwork();
//                curNetwork.setText("");
//                for (InetAddress addr : addrs) {
//                    curNetwork.append(addr + "\n");
//                }
//
//                state.setText("");
//                state.append("pred " + NetworkManager.pred + "\n");
//                state.append("cur " + NetworkManager.me + "\n");
//                state.append("succ " + NetworkManager.succ + "\n");
//                state.append("succ2 " + NetworkManager.succ2 + "\n");
//
//                entries.setText("Entries\n");
//                for (Map.Entry<Integer, InetAddress> entry : NetworkManager.keepNode.entrySet()) {
//                    int key = entry.getKey();
//                    InetAddress addr = entry.getValue();
//                    entries.append(key + " :: " + addr + "\n");
//                }
//
//                files.setText("Strings\n");
//                for (Map.Entry<String, String> entry : FileManager.files.entrySet()) {
//                    String key = entry.getKey();
//                    String value = entry.getValue();
//                    files.append("key: " + key + ", value: " + value + "\n");
//                }
//                Thread.sleep(REFRESH_TIME);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}