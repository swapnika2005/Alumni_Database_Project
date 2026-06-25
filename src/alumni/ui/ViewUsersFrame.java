package alumni.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import alumni.db.DBConnection;

public class ViewUsersFrame extends JFrame {
    private JTable alumniTable;
    private DefaultTableModel model;
    private JTextField txtSearch;

    public ViewUsersFrame() {
        setTitle("View Alumni Records");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Background from online image ===
        try {
            URL bgUrl = new URL("https://images.unsplash.com/photo-1627556704314-1f7aee38ec57?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTE1fHxhbHVtbml8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=600");
            ImageIcon bgIcon = new ImageIcon(bgUrl);
            Image img = bgIcon.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
            JLabel background = new JLabel(new ImageIcon(img));
            background.setLayout(new BorderLayout());
            setContentPane(background);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // === Header ===
        JLabel lblTitle = new JLabel("🎓 Alumni Directory", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // === Search Panel ===
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(0, 123, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(40, 167, 69));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnBack = new JButton("← Back");
        btnBack.setBackground(new Color(108, 117, 125));
        btnBack.setForeground(Color.WHITE);

        searchPanel.add(new JLabel("Search by Name / ID:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        searchPanel.add(btnBack);
        add(searchPanel, BorderLayout.SOUTH);

        // === Table ===
        model = new DefaultTableModel(new String[]{
            "ID", "Name", "Email", "Passout Year", "Department", "Company", "Position", "Location", "Mobile", "Marital Status", "Photo"
        }, 0);
        alumniTable = new JTable(model);
        alumniTable.setRowHeight(80);
        alumniTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        alumniTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        alumniTable.setBackground(new Color(255, 255, 255, 200));

        JScrollPane scrollPane = new JScrollPane(alumniTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // === Table Image Renderer ===
        alumniTable.getColumn("Photo").setCellRenderer(new ImageRenderer());

        // === Event Listeners ===
        btnSearch.addActionListener(e -> searchAlumni());
        btnRefresh.addActionListener(e -> loadAlumniData());
        btnBack.addActionListener(e -> {
            dispose();
            new AdminDashboardFrame("Admin").setVisible(true);
        });

        // === Load data initially ===
        loadAlumniData();
    }

    private void loadAlumniData() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM alumni")) {

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("passout_year"),
                    rs.getString("department"),
                    rs.getString("company"),
                    rs.getString("position"),
                    rs.getString("location"),
                    rs.getString("mobile"),
                    rs.getString("marital_status"),
                    rs.getString("photo_path")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchAlumni() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search keyword!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM alumni WHERE id LIKE ? OR name LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                Object[] row = new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("passout_year"),
                    rs.getString("department"),
                    rs.getString("company"),
                    rs.getString("position"),
                    rs.getString("location"),
                    rs.getString("mobile"),
                    rs.getString("marital_status"),
                    rs.getString("photo_path")
                };
                model.addRow(row);
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "No alumni found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === Custom Renderer for Images ===
    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            try {
                String path = (String) value;
                if (path != null && !path.isEmpty()) {
                    BufferedImage img = ImageIO.read(new File(path));
                    Image scaled = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(scaled));
                } else {
                    label.setText("No Image");
                }
            } catch (Exception e) {
                label.setText("Invalid");
            }
            return label;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewUsersFrame().setVisible(true));
    }
}
