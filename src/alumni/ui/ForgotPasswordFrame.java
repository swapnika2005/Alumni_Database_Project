package alumni.ui;

import alumni.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ForgotPasswordFrame extends JFrame {

    private JTextField userIdField, emailField;
    private JPasswordField newPasswordField;
    private JButton resetBtn, backBtn;
    private JLabel background;

    public ForgotPasswordFrame() {
        setTitle("Forgot Password");
        setSize(600, 420);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // ✅ Working JPG version of your graduation image
        String bgUrl = "https://plus.unsplash.com/premium_photo-1682765673139-40958c79e647?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8YWx1bW5pfGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=600"; // Graduation hats photo
        setBackgroundFromURL(bgUrl);

        // === Transparent Overlay Panel ===
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, 600, 420);
        background.add(panel);

        // === Title ===
        JLabel title = new JLabel("RESET PASSWORD", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBounds(150, 40, 300, 40);
        panel.add(title);

        // === Labels ===
        JLabel userIdLbl = new JLabel("User ID:");
        JLabel emailLbl = new JLabel("Email:");
        JLabel newPassLbl = new JLabel("New Password:");

        JLabel[] labels = {userIdLbl, emailLbl, newPassLbl};
        int y = 120;
        for (JLabel lbl : labels) {
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lbl.setForeground(Color.WHITE);
            lbl.setBounds(100, y, 150, 30);
            panel.add(lbl);
            y += 50;
        }

        // === Text Fields ===
        userIdField = new JTextField();
        emailField = new JTextField();
        newPasswordField = new JPasswordField();

        JComponent[] fields = {userIdField, emailField, newPasswordField};
        y = 120;
        for (JComponent field : fields) {
            field.setBounds(260, y, 220, 30);
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            field.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            panel.add(field);
            y += 50;
        }

        // === Buttons ===
        resetBtn = createButton("Reset Password", new Color(0, 153, 76));
        resetBtn.setBounds(130, 300, 160, 40);
        panel.add(resetBtn);

        backBtn = createButton("Back to Login", new Color(0, 102, 204));
        backBtn.setBounds(320, 300, 160, 40);
        panel.add(backBtn);

        // === Button Actions ===
        resetBtn.addActionListener(e -> resetPassword());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    // ✅ Load image from URL and display properly
    private void setBackgroundFromURL(String imageUrl) {
        try {
            BufferedImage img = ImageIO.read(new URL(imageUrl));
            Image scaled = img.getScaledInstance(600, 420, Image.SCALE_SMOOTH);

            background = new JLabel(new ImageIcon(scaled)) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Add soft overlay for better contrast
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(new Color(0, 0, 0, 100)); // transparent black
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            };
            background.setBounds(0, 0, 600, 420);
            background.setLayout(null);
            setContentPane(background);
        } catch (Exception e) {
            background = new JLabel();
            background.setOpaque(true);
            background.setBackground(new Color(40, 60, 90));
            background.setLayout(null);
            setContentPane(background);
            System.out.println("⚠️ Failed to load background: " + e.getMessage());
        }
    }

    // ✅ Stylish button creator
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color hover = color.darker();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }

    // ✅ Reset password logic
    private void resetPassword() {
        String userId = userIdField.getText().trim();
        String email = emailField.getText().trim();
        String newPass = String.valueOf(newPasswordField.getPassword()).trim();

        if (userId.isEmpty() || email.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String checkSql = "SELECT * FROM users WHERE user_id=? AND email=?";
            PreparedStatement ps = conn.prepareStatement(checkSql);
            ps.setString(1, userId);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String updateSql = "UPDATE users SET password=? WHERE user_id=?";
                PreparedStatement updatePs = conn.prepareStatement(updateSql);
                updatePs.setString(1, newPass);
                updatePs.setString(2, userId);
                updatePs.executeUpdate();

                JOptionPane.showMessageDialog(this, "✅ Password reset successful!");
                dispose();
                new LoginFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid User ID or Email!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ForgotPasswordFrame().setVisible(true));
    }
}
