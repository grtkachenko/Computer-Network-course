package dev.utils;

import dev.command_queue.CommandQueue;
import dev.command_queue.GetPredecessorCommand;
import dev.threads.TCPReceiverHandler;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

public class LogGUI {
    private final int REFRESH_TIME = 500;
    JFrame frame;
    static public JTextArea logArea = newTextArea("log"),
            curNetwork = newTextArea("network"),
            state = newTextArea("state"),
            error = newTextArea("error"),
            entries = newTextArea("entries"),
            files = newTextArea("files");

    static JPanel buildStringForIp() {
        JPanel stringForIp = new JPanel();
        stringForIp.setLayout(new FlowLayout());
        final JTextField ipField = new JTextField();
        ipField.setPreferredSize(new Dimension(180, 24));
        final JTextField genedString = new JTextField();
        genedString.setPreferredSize(new Dimension(180, 24));
        genedString.setEditable(false);
        stringForIp.add(new JLabel("Gen key for given IP"));
        stringForIp.add(ipField);
        JButton genBtn = new JButton("gen");
        genBtn.setPreferredSize(new Dimension(100, 24));
        stringForIp.add(genBtn);
        stringForIp.add(genedString);
        genBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    InetAddress cur = InetAddress.getByName(ipField.getText());
                    InetAddress pred = null;
                    try {
                        pred = CommandQueue.getInstance().execute(new GetPredecessorCommand(cur)).get();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    }
                    int l = Utils.sha1(pred);
                    int r = Utils.sha1(cur);
                    String res = Utils.getStringInRange(l, r);
                    if (res == null)
                        res = "can't generate";
                    genedString.setText(res);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        return stringForIp;
    }

    JPanel buildAddEntry() {
        JPanel addEntry = new JPanel();
        addEntry.setLayout(new FlowLayout());
        final JTextField keyField = new JTextField();
        final JTextField valueField = new JTextField();
        keyField.setPreferredSize(new Dimension(180, 24));
        valueField.setPreferredSize(new Dimension(180, 24));
        addEntry.add(new JLabel("Add new entry"));
        addEntry.add(keyField);
        addEntry.add(valueField);
        JButton addEntryBtn = new JButton("add");
        addEntryBtn.setPreferredSize(new Dimension(100, 24));
        addEntryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                String value = valueField.getText();
                TCPReceiverHandler.addEntry(key, value);
            }
        });
        addEntry.add(addEntryBtn);
        return addEntry;
    }

    JPanel buildGetData() {
        JPanel getData = new JPanel();
        getData.setLayout(new FlowLayout());
        final JTextField keyField = new JTextField();
        keyField.setPreferredSize(new Dimension(180, 24));
        getData.add(new JLabel("Get data by key"));
        getData.add(keyField);

        JButton getDataBtn = new JButton("get");
        getDataBtn.setPreferredSize(new Dimension(100, 24));
        getData.add(getDataBtn);

        final JTextField dataField = new JTextField();
        dataField.setPreferredSize(new Dimension(180, 24));
        dataField.setEditable(false);
        getData.add(dataField);

        getDataBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                dataField.setText(TCPReceiverHandler.getEntry(key));
            }
        });

        return getData;
    }

    public LogGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Log");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        GridLayout grid = new GridLayout(3, 1);
        frame.setLayout(grid);

        DefaultCaret caret = (DefaultCaret) logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollLog = new JScrollPane(logArea);


        frame.add(buildAddEntry());
        frame.add(buildStringForIp());
        frame.add(buildGetData());
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
}
