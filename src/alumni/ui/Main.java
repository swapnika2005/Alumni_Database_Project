package alumni.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Main.java — Entry point for Alumni Management System.
 * Shows a splash screen before launching the login page.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SplashScreen(); // Show splash screen first
        });
    }
}

/**
 * Simple splash screen that displays a logo and title
 * for a few seconds before launching the LoginFrame.
 */
class SplashScreen extends JWindow {

    public SplashScreen() {
        // === Splash Panel ===
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(20, 30, 50));

        // === Logo ===
        JLabel logoLabel;
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resource/logo.png"));
            Image scaled = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            logoLabel = new JLabel("ALUMNI", SwingConstants.CENTER);
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            logoLabel.setForeground(Color.WHITE);
        }

        // === Title ===
        JLabel title = new JLabel("Alumni Management System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        // === Loading Label ===
        JLabel loading = new JLabel("Loading...", SwingConstants.CENTER);
        loading.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loading.setForeground(new Color(180, 180, 180));

        // === Add components ===
        content.add(logoLabel, BorderLayout.CENTER);
        content.add(title, BorderLayout.NORTH);
        content.add(loading, BorderLayout.SOUTH);

        // === Frame Settings ===
        setContentPane(content);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // Simulate loading
        new Timer(2500, e -> {
            dispose();
            new LoginFrame().setVisible(true);
        }).start();
    }
}
