package alumni.ui;

import alumni.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.sql.*;

public class RegisterFrame extends JFrame {

    private JTextField userIdField, usernameField, emailField;
    private JPasswordField passwordField;
    private JRadioButton adminRadio, userRadio;
    private JButton registerBtn, backBtn;
    private JLabel backgroundLabel, logoLabel;

    public RegisterFrame() {
        setTitle("Alumni Registration");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        // ✅ New background image (more professional & clear)
        String bgUrl = "https://plus.unsplash.com/premium_photo-1682765673139-40958c79e647?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8YWx1bW5pfGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=600";
        setBackgroundImage(bgUrl);

        // === Add logo from resources ===
        addLogo();

        // === Title ===
        JLabel title = new JLabel("USER REGISTRATION", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(200, 70, 400, 40);
        backgroundLabel.add(title);

        // === Transparent panel for readability ===
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(0, 0, 0, 130)); // semi-transparent black
        formPanel.setBounds(160, 130, 480, 380);
        backgroundLabel.add(formPanel);

        // === Labels ===
        JLabel userIdLbl = createLabel("User ID:", 70, 50);
        JLabel usernameLbl = createLabel("Username:", 70, 100);
        JLabel emailLbl = createLabel("Email:", 70, 150);
        JLabel passwordLbl = createLabel("Password:", 70, 200);

        formPanel.add(userIdLbl);
        formPanel.add(usernameLbl);
        formPanel.add(emailLbl);
        formPanel.add(passwordLbl);

        // === Input fields ===
        userIdField = createTextField(200, 50);
        usernameField = createTextField(200, 100);
        emailField = createTextField(200, 150);
        passwordField = new JPasswordField();
        passwordField.setBounds(200, 200, 200, 30);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        formPanel.add(userIdField);
        formPanel.add(usernameField);
        formPanel.add(emailField);
        formPanel.add(passwordField);

        // === Role radio buttons ===
        adminRadio = new JRadioButton("Admin");
        userRadio = new JRadioButton("User");
        ButtonGroup group = new ButtonGroup();
        group.add(adminRadio);
        group.add(userRadio);
        userRadio.setSelected(true);

        adminRadio.setOpaque(false);
        userRadio.setOpaque(false);
        adminRadio.setForeground(Color.WHITE);
        userRadio.setForeground(Color.WHITE);
        adminRadio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userRadio.setFont(new Font("Segoe UI", Font.BOLD, 14));

        adminRadio.setBounds(130, 250, 100, 30);
        userRadio.setBounds(270, 250, 100, 30);
        formPanel.add(adminRadio);
        formPanel.add(userRadio);

        // === Buttons ===
        registerBtn = createButton("Register", new Color(0, 153, 76));
        backBtn = createButton("Back", new Color(0, 102, 204));

        registerBtn.setBounds(110, 310, 120, 40);
        backBtn.setBounds(250, 310, 120, 40);

        formPanel.add(registerBtn);
        formPanel.add(backBtn);

        // === Button actions ===
        registerBtn.addActionListener(e -> registerUser());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    // ✅ Load and set background image
    private void setBackgroundImage(String imageUrl) {
        try {
            BufferedImage img = ImageIO.read(new URL(imageUrl));
            Image scaled = img.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
            backgroundLabel = new JLabel(new ImageIcon(scaled));
            backgroundLabel.setBounds(0, 0, 800, 600);
            backgroundLabel.setLayout(null);
            setContentPane(backgroundLabel);
        } catch (Exception e) {
            System.out.println("⚠️ Could not load background: " + e.getMessage());
            backgroundLabel = new JLabel();
            backgroundLabel.setOpaque(true);
            backgroundLabel.setBackground(Color.DARK_GRAY);
            backgroundLabel.setBounds(0, 0, 800, 600);
            backgroundLabel.setLayout(null);
            setContentPane(backgroundLabel);
        }
    }

    // ✅ Add logo from local resources
    private void addLogo() {
        try {
            // Path: src/resources/logo.png
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resource/logo.png"));
            Image scaledLogo = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setBounds(30, 20, 100, 100);
            backgroundLabel.add(logoLabel);
        } catch (Exception e) {
            System.out.println("⚠️ Logo not found: " + e.getMessage());
        }
    }

    // === Create label helper ===
    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        lbl.setBounds(x, y, 120, 30);
        return lbl;
    }

    // === Create text field helper ===
    private JTextField createTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 200, 30);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    // === Create button helper ===
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

    // === Database registration ===
    private void registerUser() {
        String userId = userIdField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = adminRadio.isSelected() ? "admin" : "user";

        if (userId.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (user_id, username, email, password, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setString(5, role);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Registration successful!");
            dispose();
            new LoginFrame().setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
    }
}
