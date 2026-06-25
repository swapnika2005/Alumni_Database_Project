package alumni.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import alumni.db.DBConnection;
import alumni.ui.UserDashboardFrame;

public class ViewEventsFrame extends JFrame {

    private final String username;
    private JTable eventsTable;

    public ViewEventsFrame(String username) {
        this.username = username;

        setTitle("Events - " + username);
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // === Background Image ===
        JLabel background = loadBackgroundImage(
            "https://images.unsplash.com/photo-1658235081452-c2ded30b8d9f?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nzh8fGFsdW1uaXxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=600"
        );
        setContentPane(background);
        background.setLayout(new BorderLayout());

        // === Overlay Panel ===
        JPanel overlay = new JPanel(new BorderLayout());
        overlay.setBackground(new Color(255, 255, 255, 190)); // translucent white
        overlay.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        background.add(overlay, BorderLayout.CENTER);

        // === Header ===
        JLabel title = new JLabel("Upcoming Events", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 60, 90));
        overlay.add(title, BorderLayout.NORTH);

        // === Events Table ===
        eventsTable = new JTable(new DefaultTableModel(
                new Object[]{"Event ID", "Title", "Date", "Location", "Description"}, 0
        ));
        eventsTable.setRowHeight(28);
        eventsTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        eventsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        eventsTable.getTableHeader().setBackground(new Color(0, 102, 204));
        eventsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(eventsTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        overlay.add(scrollPane, BorderLayout.CENTER);

        // === Bottom Button ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottomPanel.setOpaque(false);

        JButton backButton = createStyledButton("⬅ Back");
        bottomPanel.add(backButton);
        overlay.add(bottomPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose();
            new UserDashboardFrame(username, "user").setVisible(true);
        });

        loadEvents();
    }

    // === Load Events from DB ===
    private void loadEvents() {
        DefaultTableModel model = (DefaultTableModel) eventsTable.getModel();
        model.setRowCount(0);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, title, event_date, location, description FROM events ORDER BY event_date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("event_date"),
                        rs.getString("location"),
                        rs.getString("description")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading events: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // === Background Loader ===
    private JLabel loadBackgroundImage(String urlStr) {
        JLabel label;
        try {
            URL url = new URL(urlStr);
            BufferedImage img = ImageIO.read(url);
            if (img == null) throw new Exception("Image load failed");
            Image scaled = img.getScaledInstance(850, 550, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            System.out.println("⚠️ Background not loaded: " + e.getMessage());
            label = new JLabel();
            label.setOpaque(true);
            label.setBackground(new Color(70, 90, 130));
        }
        label.setBounds(0, 0, 850, 550);
        return label;
    }

    // === Styled Button ===
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 102, 204));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        Color hover = new Color(0, 80, 180);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(0, 102, 204)); }
        });
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewEventsFrame("demoUser").setVisible(true));
    }
}
