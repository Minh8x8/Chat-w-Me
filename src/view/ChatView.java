package view;

import com.formdev.flatlaf.FlatLightLaf;
import server.ChatServer;
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
                Object[] options = {"Save", "Don't save", "Cancel"};
                int choice = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to exit the program?", "Exit Program Confirmation",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);
                switch (choice) {
                    case JOptionPane.YES_NO_OPTION:
                        hView.dispose();
                        server.saveChat();
                        server.shutdown();
                        break;
                    case JOptionPane.NO_OPTION:
                        hView.dispose();
                        server.shutdown();
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        break;
                }
            }
        });
        // Set location to the center
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
        this.setVisible(true);

        setBounds(100, 100, 343, 455);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 336, 425);
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
        inputField.setBounds(10, 286, 311, 38);
        chatPanel.add(inputField);
        inputField.setColumns(10);

        JButton btnChatButton = new JButton("Chat");
        btnChatButton.setForeground(new Color(0, 0, 0));
        btnChatButton.setBounds(251, 335, 70, 38);
        chatPanel.add(btnChatButton);

        JButton btnUndoButton = new JButton("Undo");
        btnUndoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Undo method
                server.undoSendMessage();
            }
        });
        btnUndoButton.setBounds(171, 335, 70, 38);
        chatPanel.add(btnUndoButton);
        btnChatButton.addActionListener(e -> {
            // TODO: Send message
            String message = inputField.getText();
            if (!message.equals("")) {
                server.sendMessage(message);
                inputField.setText("");
            }
        });

        JPanel historyPanel = new JPanel();
        tabbedPane.addTab("History", null, historyPanel, null);
        historyPanel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 311, 288);
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
        btnShowButton.addActionListener(e -> {
            // TODO: show history
            showHistory();
        });
        btnShowButton.setBounds(114, 336, 89, 38);
        historyPanel.add(btnShowButton);

        JPanel aboutPanel = new JPanel();
        tabbedPane.addTab("About", null, aboutPanel, null);
        aboutPanel.setLayout(null);

        JLabel lbAbout = new JLabel("<html>Chat w Me is a chatting application. It is designed for my Data Structure and Algorithm assignment.\r\n<br>Version: v1.0\r\n<br>Developer: Dang Van Tri Minh</html>");
        lbAbout.setFont(new Font("Calibri", Font.PLAIN, 13));
        lbAbout.setBounds(10, 61, 294, 132);
        aboutPanel.add(lbAbout);
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
    public void undoMessage(String message) {
        chatBox.setText(chatBox.getText().replace(message, "Message was removed"));
    }
}

