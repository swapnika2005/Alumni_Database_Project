package alumni.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import alumni.db.DBConnection;

public class ViewAchievementsFrame extends JFrame {

    private final String username;
    private JTable achievementsTable;

    public ViewAchievementsFrame(String username) {
        this.username = username;

        setTitle("Achievements - " + username);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // === Background (from browser URL) ===
        JLabel background = loadBackgroundImage(
            "https://images.unsplash.com/photo-1658235081452-c2ded30b8d9f?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nzh8fGFsdW1uaXxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=600"
        );
        setContentPane(background);
        background.setLayout(null);

        // === Semi-transparent overlay ===
        JPanel overlay = new JPanel(new BorderLayout());
        overlay.setBackground(new Color(255, 255, 255, 180)); // soft white transparency
        overlay.setBounds(60, 40, 780, 500);
        overlay.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2, true));
        background.add(overlay);

        // === Title ===
        JLabel title = new JLabel("🏆 Your Achievements", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(25, 60, 100));
        overlay.add(title, BorderLayout.NORTH);

        // === Table ===
        achievementsTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Title", "Year", "Description"}, 0
        ));
        achievementsTable.setRowHeight(30);
        achievementsTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        achievementsTable.setGridColor(Color.LIGHT_GRAY);
        achievementsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        achievementsTable.getTableHeader().setBackground(new Color(0, 102, 204));
        achievementsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(achievementsTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        overlay.add(scrollPane, BorderLayout.CENTER);

        // === Bottom Buttons ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottomPanel.setOpaque(false);

        JButton addButton = createStyledButton("➕ Add Achievement", new Color(0, 153, 76));
        JButton backButton = createStyledButton("⬅ Back", new Color(204, 0, 0));

        bottomPanel.add(addButton);
        bottomPanel.add(backButton);
        overlay.add(bottomPanel, BorderLayout.SOUTH);

        // === Actions ===
        addButton.addActionListener(e -> addAchievement());
        backButton.addActionListener(e -> {
            dispose();
            new UserDashboardFrame(username, "user").setVisible(true);
        });

        loadAchievements();
    }

    // === Background loader ===
    private JLabel loadBackgroundImage(String urlStr) {
        JLabel label;
        try {
            URL url = new URL(urlStr);
            BufferedImage img = ImageIO.read(url);
            if (img == null) throw new Exception("Image load failed");
            Image scaled = img.getScaledInstance(900, 600, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            System.out.println("⚠️ Background not loaded: " + e.getMessage());
            label = new JLabel();
            label.setOpaque(true);
            label.setBackground(new Color(70, 90, 130));
        }
        label.setBounds(0, 0, 900, 600);
        return label;
    }

    // === Styled buttons ===
    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(baseColor);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(baseColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(baseColor);
            }
        });
        return btn;
    }

    private void loadAchievements() {
        DefaultTableModel model = (DefaultTableModel) achievementsTable.getModel();
        model.setRowCount(0);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, title, year, description FROM achievements WHERE user_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("year"),
                        rs.getString("description")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Error loading achievements: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addAchievement() {
        JTextField titleField = new JTextField();
        JTextField yearField = new JTextField();
        JTextArea descArea = new JTextArea(3, 20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descArea));

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Achievement", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO achievements (title, description, year, user_id) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, titleField.getText().trim());
                ps.setString(2, descArea.getText().trim());
                ps.setInt(3, Integer.parseInt(yearField.getText().trim()));
                ps.setString(4, username);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "✅ Achievement added successfully!");
                loadAchievements();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Failed to add achievement: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter a valid year.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewAchievementsFrame("demoUser").setVisible(true));
    }
}

