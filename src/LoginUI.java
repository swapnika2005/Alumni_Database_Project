package alumni.ui;

import alumni.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn, registerBtn, exitBtn;

    public LoginUI() {
        setTitle("Alumni Login");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // === Background ===
        JLabel background = new JLabel();
        try {
            ImageIcon bgIcon = new ImageIcon(getClass().getResource("/resource/background.png"));
            Image bgImage = bgIcon.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH);
            background.setIcon(new ImageIcon(bgImage));
        } catch (Exception e) {
            System.out.println("⚠️ Background not found");
        }
        background.setBounds(0, 0, 500, 400);
        setContentPane(background);
        background.setLayout(null);

        // === Title ===
        JLabel title = new JLabel("Alumni Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.BLACK);
        title.setBounds(110, 40, 300, 30);
        background.add(title);

        // === Labels ===
        JLabel userLbl = new JLabel("Username:");
        JLabel passLbl = new JLabel("Password:");
        userLbl.setBounds(100, 130, 100, 25);
        passLbl.setBounds(100, 170, 100, 25);
        background.add(userLbl);
        background.add(passLbl);

        // === Fields ===
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        usernameField.setBounds(200, 130, 180, 25);
        passwordField.setBounds(200, 170, 180, 25);
        background.add(usernameField);
        background.add(passwordField);

        // === Buttons ===
        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register");
        exitBtn = new JButton("Exit");

        loginBtn.setBounds(200, 220, 180, 30);
        registerBtn.setBounds(200, 260, 180, 30);
        exitBtn.setBounds(200, 300, 180, 30);

        background.add(loginBtn);
        background.add(registerBtn);
        background.add(exitBtn);

        // === Button Actions ===
        loginBtn.addActionListener(e -> loginUser());
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "✅ Login successful as " + role);

                dispose();

                if (role.equalsIgnoreCase("admin")) {
                    // ✅ FIX: Use correct class name
                    new AdminDashboardFrame(username).setVisible(true);
                } else {
                    // ✅ Pass userId or username to UserDashboardFrame
                    new UserDashboardFrame(username, "user").setVisible(true);


                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid username or password!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
