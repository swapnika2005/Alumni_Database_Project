package alumni.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class AdminDashboardFrame extends JFrame {

    private String adminName;

    public AdminDashboardFrame(String adminName) {
        this.adminName = adminName;
        initUI();
    }

    private void initUI() {
        setTitle("Admin Dashboard - Alumni System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // === Background from browser URL ===
        JLabel background = loadBackgroundImage(
                "https://plus.unsplash.com/premium_photo-1714265042273-9664e4537f7d?ixlib=rb-4.1.0&auto=format&fit=crop&q=60&w=600"
        );
        setContentPane(background);
        background.setLayout(null);

        // === Semi-transparent blur overlay for readability ===
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 140)); // translucent black
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        overlay.setBounds(60, 40, 880, 500);
        overlay.setOpaque(false);
        overlay.setLayout(null);
        background.add(overlay);

        // === Logo ===
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resource/logo.png"));
            Image logoImg = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(logoImg));
        } catch (Exception e) {
            logoLabel.setText("Logo");
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setBounds(40, 20, 100, 100);
        overlay.add(logoLabel);

        // === Welcome Label ===
        JLabel welcomeLabel = new JLabel("Welcome, Admin " + adminName);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(180, 50, 600, 60);
        overlay.add(welcomeLabel);

        // === Buttons ===
        JButton btnViewUsers = createStyledButton("👥 View Users", 120, 180);
        JButton btnAddUser = createStyledButton("➕ Add User", 360, 180);
        JButton btnManageEvents = createStyledButton("📅 Manage Events", 600, 180);
        JButton btnSearchUsers = createStyledButton("🔍 Search Users", 240, 280);
        JButton btnDeleteUser = createStyledButton("🗑 Delete User", 480, 280);
        JButton btnLogout = createStyledButton("🚪 Logout", 720, 280);

        overlay.add(btnViewUsers);
        overlay.add(btnAddUser);
        overlay.add(btnManageEvents);
        overlay.add(btnSearchUsers);
        overlay.add(btnDeleteUser);
        overlay.add(btnLogout);

        // === Button Actions ===
        btnViewUsers.addActionListener(e -> new ViewUsersFrame().setVisible(true));
        btnAddUser.addActionListener(e -> new AddUserFrame().setVisible(true));
        btnManageEvents.addActionListener(e -> new ManageEventsFrame().setVisible(true));
        btnSearchUsers.addActionListener(e -> new SearchUserFrame(adminName).setVisible(true));
        btnDeleteUser.addActionListener(e -> new DeleteUserFrame().setVisible(true));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 200, 60);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 128, 255));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 102, 204));
            }
        });
        return button;
    }

    private JLabel loadBackgroundImage(String urlStr) {
        JLabel label;
        try {
            URL url = new URL(urlStr);
            BufferedImage img = ImageIO.read(url);
            if (img == null) throw new Exception("Failed to load background");
            Image scaled = img.getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            label = new JLabel();
            label.setOpaque(true);
            label.setBackground(new Color(70, 90, 130));
        }
        label.setBounds(0, 0, 1000, 600);
        label.setLayout(null);
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardFrame("John").setVisible(true));
    }
}
