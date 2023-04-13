package view;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoadingChatView extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoadingChatView frame = new LoadingChatView();
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
    public LoadingChatView() {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 304, 126);
        setTitle("Connect to Chat w Me");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(10, 33, 270, 41);
        progressBar.setIndeterminate(true);
        progressBar.setString("Trying to connect to server...");
        progressBar.setStringPainted(true);

        contentPane.add(progressBar);
    }

}

