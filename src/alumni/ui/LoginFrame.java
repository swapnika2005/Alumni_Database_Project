package alumni.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.*;
import alumni.db.DBConnection;

// ✅ Import your register and forgot password classes
import alumni.ui.RegisterFrame;
import alumni.ui.ForgotPasswordFrame;

public class LoginFrame extends JFrame {

    private JTextField idField, usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JCheckBox showPassword;
    private JButton loginButton, resetButton, registerButton, forgotButton, exitButton;

    public LoginFrame() {
        setTitle("Garden City University Alumni Portal");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ✅ Background image
        String imageUrl = "https://plus.unsplash.com/premium_photo-1714265042273-9664e4537f7d?ixlib=rb-4.1.0&auto=format&fit=crop&q=60&w=1200";
        ImageIcon backgroundIcon;
        try {
            BufferedImage img = ImageIO.read(new URL(imageUrl));
            Image scaled = img.getScaledInstance(900, 650, Image.SCALE_SMOOTH);
            backgroundIcon = new ImageIcon(toBufferedImage(scaled));
        } catch (Exception e) {
            e.printStackTrace();
            backgroundIcon = new ImageIcon();
        }

        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // 🔹 Transparent overlay
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        add(overlayPanel);

        // 🔹 Header with logo + title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);

        JLabel logoLabel;
        try {
            URL logoUrl = getClass().getResource("/resource/logo.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                Image logo = logoIcon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
                logoLabel = new JLabel(new ImageIcon(logo));
            } else {
                logoLabel = new JLabel("LOGO");
                logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
                logoLabel.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            logoLabel = new JLabel("LOGO");
            logoLabel.setForeground(Color.WHITE);
        }

        JLabel titleLabel = new JLabel("Garden City University Alumni Portal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.BLACK);

        headerPanel.add(logoLabel);
        headerPanel.add(titleLabel);
        overlayPanel.add(headerPanel, BorderLayout.NORTH);

        // 🔹 Form section
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 160));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("ALUMNI LOGIN");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        loginTitle.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginTitle, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Dimension fieldSize = new Dimension(250, 38);

        // ✅ User ID
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(labelFont);
        formPanel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        idField = new JTextField();
        idField.setPreferredSize(fieldSize);
        idField.setFont(fieldFont);
        formPanel.add(idField, gbc);

        // ✅ Username
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField();
        usernameField.setPreferredSize(fieldSize);
        usernameField.setFont(fieldFont);
        formPanel.add(usernameField, gbc);

        // ✅ Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);
        passwordField.setFont(fieldFont);
        formPanel.add(passwordField, gbc);

        // ✅ Show Password Checkbox
        gbc.gridy++;
        gbc.gridx = 1;
        showPassword = new JCheckBox("Show Password");
        showPassword.setOpaque(false);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(showPassword, gbc);

        // 🔹 Toggle password visibility
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        // ✅ Role
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(labelFont);
        formPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        roleComboBox = new JComboBox<>(new String[]{"user", "admin"});
        roleComboBox.setPreferredSize(fieldSize);
        roleComboBox.setFont(fieldFont);
        formPanel.add(roleComboBox, gbc);

        // 🔹 Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        Color buttonColor = new Color(0, 102, 204);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 15);

        loginButton = createStyledButton("Login", buttonColor, buttonFont);
        resetButton = createStyledButton("Reset", buttonColor, buttonFont);
        registerButton = createStyledButton("Register", buttonColor, buttonFont);
        forgotButton = createStyledButton("Forgot Password", buttonColor, buttonFont);
        exitButton = createStyledButton("Exit", buttonColor, buttonFont);

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(forgotButton);
        buttonPanel.add(exitButton);

        formPanel.add(buttonPanel, gbc);
        overlayPanel.add(formPanel, BorderLayout.CENTER);

        // 🔹 Actions
        loginButton.addActionListener(e -> loginUser());
        resetButton.addActionListener(e -> resetFields());
        exitButton.addActionListener(e -> System.exit(0));

        // ✅ Added Integration for Register and Forgot Password
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
        });

        forgotButton.addActionListener(e -> {
            new ForgotPasswordFrame().setVisible(true);
        });
    }

    private JButton createStyledButton(String text, Color bgColor, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void resetFields() {
        idField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        showPassword.setSelected(false);
        passwordField.setEchoChar('•');
    }

    // ✅ loginUser() with role-based dashboard redirection
    private void loginUser() {
        String userId = idField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (userId.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE USER_ID=? AND Username=? AND password=? AND role=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, userId);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.setString(4, role);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();

                // ✅ Role-based navigation
                if (role.equalsIgnoreCase("admin")) {
                    new AdminDashboardFrame(username).setVisible(true);
                } else {
                    new UserDashboardFrame(username, role).setVisible(true);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static BufferedImage toBufferedImage(Image img) {
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        if (width <= 0 || height <= 0) {
            width = 900;
            height = 650;
        }
        BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bimage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return bimage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
