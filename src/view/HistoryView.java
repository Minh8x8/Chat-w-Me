package view;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HistoryView extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HistoryView frame = new HistoryView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public HistoryView() {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 343, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTextArea textAreaChatHistory = new JTextArea();
        textAreaChatHistory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textAreaChatHistory.setBounds(10, 11, 309, 341);
        contentPane.add(textAreaChatHistory);
    }

}

