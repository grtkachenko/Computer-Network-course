package gui;

import command_queue.*;
import common.TCPServer;
import model.ServerInfo;
import model.ServerInfos;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import utils.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * User: gtkachenko
 */
public class WindowManager implements ServerInfos.ServerListChangeListener, CommandQueueCallback {
    private MainPanel panel;

    public void initGui() {
        JFrame frame = new JFrame("List Model Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new MainPanel();
        frame.setContentPane(panel);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    @Override
    public void onChanged(Set<ServerInfo> serverInfoList) {
        if (panel != null) {
            panel.onChanged(serverInfoList);
        }
    }

    @Override
    public void onCommandProcessed(Command command) {
        if (command.getCommandId() == CommandQueueCallback.CMD_ID_LIST) {
            try {
                panel.updateServerFilesModel((List<String>) command.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    private class MainPanel extends JPanel {

        private JList serverList;
        private JList myFileList;
        private JList serverFileList;


        private JTextField serverInfoField;
        private List<ServerInfo> lastServerInfoList = new ArrayList<ServerInfo>();
        private List<String> lastServerFilesList = new ArrayList<String>();


        private DefaultListModel serverFilesModel;
        private DefaultListModel myFilesModel;

        private DefaultListModel serversModel;


        public void onChanged(Set<ServerInfo> serverInfoList) {
            if (lastServerInfoList != null && lastServerInfoList.containsAll(serverInfoList) && serverInfoList.containsAll(lastServerInfoList)) {
                return;
            }
            lastServerInfoList.clear();
            serversModel.clear();
            for (ServerInfo serverInfo : serverInfoList) {
                lastServerInfoList.add(serverInfo);
                serversModel.addElement(serverInfo.getServerName());
            }
            serverList.setModel(serversModel);
        }

        private void updateMyFilesModel() {
            myFilesModel.clear();
            for (File file : Utils.getRoot().listFiles()) {
                myFilesModel.addElement(file.getName());
            }
            myFileList.setModel(myFilesModel);
        }

        public void updateServerFilesModel(List<String> serverFiles) {
            lastServerFilesList = new ArrayList<String>(serverFiles);
            serverFilesModel.clear();
            for (String cur : lastServerFilesList) {
                serverFilesModel.addElement(cur);
            }
            serverFileList.setModel(serverFilesModel);

        }

        public MainPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            serversModel = new DefaultListModel();
            serverList = new JList(serversModel);
            serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            serverFilesModel = new DefaultListModel();
            serverFileList = new JList(serverFilesModel);
            serverFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            myFilesModel = new DefaultListModel();
            myFileList = new JList(myFilesModel);
            myFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            serverInfoField = new JTextField();
            serverList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    if (serverList.isSelectionEmpty()) {
                        return;
                    }
                    serverInfoField.setText(lastServerInfoList.get(serverList.getSelectedIndex()).toString());
                }
            });
            JPanel filesPanel = new JPanel();
            filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.X_AXIS));
            JScrollPane pane = new JScrollPane(serverList);
            JScrollPane serverFilesPane = new JScrollPane(serverFileList);
            JScrollPane myFilesPane = new JScrollPane(myFileList);

            filesPanel.add(serverFilesPane);
            filesPanel.add(myFilesPane);


            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
            JButton listButton = new JButton("LIST");
            listButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (!serverList.isSelectionEmpty()) {
                        final ServerInfo serverInfo = lastServerInfoList.get(serverList.getSelectedIndex());

                        CommandQueue.getInstance().execute(new ListCommand(new Callable<List<String>>() {
                            @Override
                            public List<String> call() throws Exception {
                                Socket clientSocket = new Socket(serverInfo.getIp(), TCPServer.TCP_PORT);
                                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                                DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
                                outToServer.writeByte(CommandQueueCallback.CMD_ID_LIST);
                                byte type = inFromServer.readByte();
                                int countFiles = inFromServer.readInt();
                                List<String> res = new ArrayList<String>();
                                for (int i = 0; i < countFiles; i++) {
                                    for (int j = 0; j < 16; j++) {
                                        inFromServer.read();
                                    }
                                    res.add(Utils.getNullTermString(inFromServer));
                                }

                                clientSocket.close();
                                return res;
                            }
                        }));
                    }
                }
            });
            JButton getButton = new JButton("GET");
            getButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (!serverList.isSelectionEmpty() && !serverFileList.isSelectionEmpty()) {
                        final String targetFile = lastServerFilesList.get(serverFileList.getSelectedIndex());
                        final ServerInfo serverInfo = lastServerInfoList.get(serverList.getSelectedIndex());

                        CommandQueue.getInstance().execute(new GetCommand(new Callable<File>() {
                            @Override
                            public File call() throws Exception {
                                Socket clientSocket = new Socket(serverInfo.getIp(), TCPServer.TCP_PORT);
                                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                                DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
                                outToServer.writeByte(CommandQueueCallback.CMD_ID_GET);
                                outToServer.write(targetFile.getBytes());
                                outToServer.write(0);

                                int type = inFromServer.read();
                                long size = inFromServer.readLong();
                                for (int i = 0; i < 16; i++) {
                                    inFromServer.read();
                                }
                                FileOutputStream fileOuputStream = new FileOutputStream(Utils.getRoot().getAbsolutePath() + "/" + targetFile);
                                byte[] bytes = new byte[(int) size];
                                for (int i = 0; i < size; i++) {
                                    bytes[i] = (byte) inFromServer.read();
                                }
                                fileOuputStream.write(bytes);
                                fileOuputStream.close();
                                clientSocket.close();
                                return new File(Utils.getRoot().getAbsolutePath() + "/" + targetFile);
                            }
                        }));
                    }
                }
            });

            JButton putButton = new JButton("PUT");
            buttonsPanel.add(listButton);
            buttonsPanel.add(getButton);
            buttonsPanel.add(putButton);

            add(pane);
            add(filesPanel);
            add(buttonsPanel);

            add(serverInfoField);
            FileAlterationObserver observer = new FileAlterationObserver(Utils.getRoot());
            observer.addListener(new FileAlterationListener() {
                @Override
                public void onStart(FileAlterationObserver fileAlterationObserver) {
                }

                @Override
                public void onDirectoryCreate(File file) {
                    updateMyFilesModel();
                }

                @Override
                public void onDirectoryChange(File file) {
                    updateMyFilesModel();
                }

                @Override
                public void onDirectoryDelete(File file) {
                    updateMyFilesModel();
                }

                @Override
                public void onFileCreate(File file) {
                    updateMyFilesModel();

                }

                @Override
                public void onFileChange(File file) {
                    updateMyFilesModel();
                }

                @Override
                public void onFileDelete(File file) {
                    updateMyFilesModel();
                }

                @Override
                public void onStop(FileAlterationObserver fileAlterationObserver) {
                }
            });
            FileAlterationMonitor monitor = new FileAlterationMonitor(100);
            monitor.addObserver(observer);
            try {
                monitor.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateMyFilesModel();
        }
    }

}
