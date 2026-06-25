package alumni.ui;

import alumni.db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SearchUserFrame extends JFrame {

    private JTextField searchField;
    private JComboBox<String> searchTypeBox;
    private JTable table;
    private DefaultTableModel model;
    private JButton searchBtn, refreshBtn, backBtn;

    public SearchUserFrame(String adminName) {
        setTitle("Search Alumni - Admin: " + adminName);
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // === Background from online URL ===
        JLabel background = loadBackgroundImage(
            "https://images.unsplash.com/photo-1627556704314-1f7aee38ec57?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTE1fHxhbHVtbml8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=600"
        );
        setContentPane(background);
        background.setLayout(null);

        // === Transparent Overlay ===
        JPanel overlay = new JPanel(null);
        overlay.setBackground(new Color(0, 0, 0, 140));
        overlay.setBounds(50, 40, 1000, 550);
        overlay.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
        background.add(overlay);
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
        // === Title ===
        JLabel title = new JLabel("🔍 Search Alumni Records", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 10, 1000, 40);
        overlay.add(title);

        // === Search Controls ===
        JLabel lblType = new JLabel("Search By:");
        lblType.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblType.setForeground(Color.WHITE);
        lblType.setBounds(80, 70, 100, 25);
        overlay.add(lblType);

        searchTypeBox = new JComboBox<>(new String[]{"name", "email", "department", "company"});
        searchTypeBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchTypeBox.setBounds(170, 70, 150, 25);
        overlay.add(searchTypeBox);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(340, 70, 220, 25);
        overlay.add(searchField);

        searchBtn = new JButton("Search");
        styleButton(searchBtn, new Color(0, 153, 76));
        searchBtn.setBounds(580, 70, 100, 25);
        overlay.add(searchBtn);

        refreshBtn = new JButton("Show All");
        styleButton(refreshBtn, new Color(0, 102, 204));
        refreshBtn.setBounds(690, 70, 100, 25);
        overlay.add(refreshBtn);

        backBtn = new JButton("← Back");
        styleButton(backBtn, new Color(204, 0, 0));
        backBtn.setBounds(800, 70, 100, 25);
        overlay.add(backBtn);

        // === Table ===
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
            "ID", "Name", "Email", "Passout Year", "Department", "Company",
            "Position", "Location", "Mobile", "Marital Status"
        });

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.DARK_GRAY);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 130, 900, 380);
        overlay.add(scrollPane);

        // === Load all records initially ===
        loadAllAlumni();

        // === Button Actions ===
        searchBtn.addActionListener(e -> searchAlumni());
        refreshBtn.addActionListener(e -> loadAllAlumni());
        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboardFrame(adminName).setVisible(true);
        });
    }

    private void loadAllAlumni() {
        model.setRowCount(0);
        String sql = "SELECT id, name, email, passout_year, department, company, position, location, mobile, marital_status FROM alumni";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("passout_year"),
                    rs.getString("department"),
                    rs.getString("company"),
                    rs.getString("position"),
                    rs.getString("location"),
                    rs.getString("mobile"),
                    rs.getString("marital_status")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchAlumni() {
        String type = (String) searchTypeBox.getSelectedItem();
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a search keyword!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.setRowCount(0);
        String sql = "SELECT id, name, email, passout_year, department, company, position, location, mobile, marital_status "
                   + "FROM alumni WHERE " + type + " LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("passout_year"),
                    rs.getString("department"),
                    rs.getString("company"),
                    rs.getString("position"),
                    rs.getString("location"),
                    rs.getString("mobile"),
                    rs.getString("marital_status")
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No records found!",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
    }

    private JLabel loadBackgroundImage(String urlStr) {
        JLabel label;
        try {
            URL url = new URL(urlStr);
            BufferedImage img = ImageIO.read(url);
            if (img == null) throw new Exception("Failed to load background");
            Image scaled = img.getScaledInstance(1100, 650, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            label = new JLabel();
            label.setOpaque(true);
            label.setBackground(new Color(70, 90, 130));
        }
        label.setBounds(0, 0, 1100, 650);
        label.setLayout(null);
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchUserFrame("Admin").setVisible(true));
    }
}
