package alumni.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class UserDashboardFrame extends JFrame {

    private String username;
    private String role;

    public UserDashboardFrame(String username, String role) {
        this.username = username;
        this.role = role;

        // Frame setup
        setTitle("User Dashboard - " + username);
        setSize(940, 627);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // ==================== Background Image from URL ====================
        JLabel background;
        try {
            URL bgUrl = new URL("https://plus.unsplash.com/premium_photo-1714265042273-9664e4537f7d?ixlib=rb-4.1.0&auto=format&fit=crop&q=60&w=600"
        );;
            Image bgImage = new ImageIcon(bgUrl).getImage().getScaledInstance(940, 627, Image.SCALE_SMOOTH);
            background = new JLabel(new ImageIcon(bgImage));
        } catch (Exception e) {
            background = new JLabel();
            background.setBackground(Color.GRAY);
            background.setOpaque(true);
        }
        background.setBounds(0, 0, 940, 627);
        background.setLayout(null);
        add(background);

        // ==================== University Logo ====================
        try {
            URL logoUrl = new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Roundel_of_India.svg/512px-Roundel_of_India.svg.png");
            Image logoImage = new ImageIcon(logoUrl).getImage().getScaledInstance(140, 90, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
            logoLabel.setBounds(20, 20, 140, 90);
            background.add(logoLabel);
        } catch (Exception ignored) {}

        // ==================== Welcome Label ====================
        JLabel welcomeLabel = new JLabel("Welcome, " + username + " (" + role + ")");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.black);
        welcomeLabel.setBounds(280, 50, 600, 40);
        background.add(welcomeLabel);

        // ==================== Button Panel (2×2 Grid) ====================
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 40, 40));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(300, 200, 400, 250);
        background.add(buttonPanel);

        Font btnFont = new Font("Segoe UI", Font.BOLD, 16);
        Color btnColor = new Color(0, 102, 204);
        Color hoverColor = new Color(0, 153, 255);

        JButton viewEventsBtn = createAnimatedButton("📅 View Events", btnColor, hoverColor, btnFont);
        JButton viewAchievementsBtn = createAnimatedButton("🏆 View Achievements", btnColor, hoverColor, btnFont);
        JButton updateProfileBtn = createAnimatedButton("✏️ Update Profile", btnColor, hoverColor, btnFont);
        JButton logoutBtn = createAnimatedButton("🚪 Logout", btnColor, hoverColor, btnFont);

        buttonPanel.add(viewEventsBtn);
        buttonPanel.add(viewAchievementsBtn);
        buttonPanel.add(updateProfileBtn);
        buttonPanel.add(logoutBtn);

        // ==================== Button Actions ====================

        // 1️⃣ View Events
        viewEventsBtn.addActionListener(e -> {
            try {
                new ViewEventsFrame(username).setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Error opening Events: " + ex.getMessage());
            }
        });

        // 2️⃣ View Achievements
        viewAchievementsBtn.addActionListener(e -> {
            try {
                new ViewAchievementsFrame(username).setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Error opening Achievements: " + ex.getMessage());
            }
        });

        // 3️⃣ Update Profile
        updateProfileBtn.addActionListener(e -> {
            try {
                new UpdateProfileFrame(username).setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Error updating profile: " + ex.getMessage());
            }
        });

        // 4️⃣ Logout
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        // ==================== Fade-in Overlay Animation ====================
        addFadeOverlay(background);
    }

    // ---------- Helper: Create Button with Hover Animation ----------
    private JButton createAnimatedButton(String text, Color baseColor, Color hoverColor, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }

    // ---------- Helper: Fade-In Animation Without setOpacity ----------
    private void addFadeOverlay(JLabel background) {
        JPanel fadePanel = new JPanel();
        fadePanel.setBackground(new Color(0, 0, 0, 255));
        fadePanel.setBounds(0, 0, 940, 627);
        fadePanel.setOpaque(true);
        background.add(fadePanel, 0);

        Timer timer = new Timer(25, null);
        timer.addActionListener(new ActionListener() {
            int alpha = 255;

            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 10;
                if (alpha <= 0) {
                    fadePanel.setVisible(false);
                    timer.stop();
                } else {
                    fadePanel.setBackground(new Color(0, 0, 0, alpha));
                }
            }
        });
        timer.start();
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserDashboardFrame("Swapnika", "User").setVisible(true));
    }
}
