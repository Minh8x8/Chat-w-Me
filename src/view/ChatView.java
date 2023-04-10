package view;

import com.formdev.flatlaf.FlatLightLaf;
import server.ChatServer;
import server.Server;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class ChatView extends JFrame {

    private JPanel contentPane;
    private JTextField inputField;
    private JTable historyTable;
    public JTextArea chatBox;
    private ChatServer server;
    private HistoryView hView;
    /**
     * Create the frame.
     */
    public ChatView(ChatServer server) {
        this.server = server;
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        hView = new HistoryView();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit the program?", "Exit Program Confirmation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmed == JOptionPane.YES_OPTION) {
                    try {
                        hView.dispose();
                        server.sendMessage("/quit");
                        server.shutdown();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        // Set location to the center
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
        this.setVisible(true);

        setBounds(100, 100, 343, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 336, 363);
        contentPane.add(tabbedPane);

        JPanel chatPanel = new JPanel();
        tabbedPane.addTab("Chat", null, chatPanel, null);
        chatPanel.setLayout(null);

        chatBox = new JTextArea();
        chatBox.setBounds(10, 21, 311, 230);
        chatBox.append("Server started");
        chatBox.setEditable(false); // to make it not editable
        chatBox.setLineWrap(true); // to wrap lines
        chatBox.setWrapStyleWord(true); // to wrap at word boundaries
        chatBox.setBackground(Color.WHITE); // set background color to white
        chatPanel.add(chatBox);

        inputField = new JTextField();
        inputField.setBounds(10, 286, 238, 38);
        chatPanel.add(inputField);
        inputField.setColumns(10);

        JButton btnSendButton = new JButton("Send");
        btnSendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Send message
                String message = inputField.getText();
                if (!message.equals("")) {
                    server.sendMessage(message);
                    inputField.setText("");
                }
            }
        });
        btnSendButton.setBounds(251, 286, 70, 38);
        chatPanel.add(btnSendButton);

        JPanel historyPanel = new JPanel();
        tabbedPane.addTab("History", null, historyPanel, null);
        historyPanel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 311, 243);
        historyPanel.add(scrollPane);

        historyTable = new JTable();
        scrollPane.setViewportView(historyTable);
        historyTable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        "ID", "Time"
                }
        ));
        historyTable.setRowHeight(30);
        TableColumnModel colModel = historyTable.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(100);
        colModel.getColumn(1).setPreferredWidth(100);
        historyTable.setRowSelectionAllowed(true);
        historyTable.setColumnSelectionAllowed(false);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // or MULTIPLE_SELECTION


        JButton btnShowButton = new JButton("Show");
        btnShowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: show history
                showHistory();
            }
        });
        btnShowButton.setBounds(121, 277, 89, 32);
        historyPanel.add(btnShowButton);

        JPanel Aboutpanel = new JPanel();
        tabbedPane.addTab("About", null, Aboutpanel, null);
        Aboutpanel.setLayout(null);

        JLabel lbAbout = new JLabel("<html>Chat w Me is a chatting application. It is designed for my Data Structure and Algorithm assignment.\r\n<br>Version: v1.0\r\n<br>Developer: Dang Van Tri Minh</html>");
        lbAbout.setFont(new Font("Calibri", Font.PLAIN, 13));
        lbAbout.setBounds(10, 61, 294, 132);
        Aboutpanel.add(lbAbout);
    }

    public void printMessage(String message) {
        chatBox.append("\n" + message);
    }
    public void setHistoryTable(List<String[]> tableChat){
        DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
        for(String[] row : tableChat) {
            model.addRow(new Object[]{row[0], row[1]});
        }
    }
    public void showHistory() {
        int row = historyTable.getSelectedRow();
        if (row != -1) {
            hView.setChatHistory(server.getMessages(++row));
            hView.setTitle("Chat History [" + row + "]");
            hView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            hView.setVisible(true);
        }
    }
}

