package alumni.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import alumni.db.DBConnection;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class ViewAlumniFrame extends JFrame {

    private JTable alumniTable;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> deptComboBox;

    public ViewAlumniFrame() {
        setTitle("View Alumni Records");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Background ===
        JLabel background = new JLabel(new ImageIcon(
                new ImageIcon(getClass().getResource("/resource/bg_dashboard.jpg"))
                        .getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH)
        ));
        setContentPane(background);
        background.setLayout(new BorderLayout());

        // === Header Panel ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 0, 0, 150));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("🎓 Alumni Records", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // === Search and Filter Panel ===
        JPanel filterPanel = new JPanel();
        filterPanel.setOpaque(false);
        filterPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        searchField = new JTextField(20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setToolTipText("Search by Name, Email, or Company");

        deptComboBox = new JComboBox<>();
        deptComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        deptComboBox.setPreferredSize(new Dimension(160, 28));

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(new Color(220, 53, 69));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);

        filterPanel.add(new JLabel("Filter by Dept:"));
        filterPanel.add(deptComboBox);
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);
        filterPanel.add(resetButton);
        headerPanel.add(filterPanel, BorderLayout.EAST);

        background.add(headerPanel, BorderLayout.NORTH);

        // === Table Model ===
        model = new DefaultTableModel(new String[]{
                "ID", "Name", "Email", "Passout Year", "Department",
                "Company", "Position", "Location", "Mobile", "Marital Status", "Photo"
        }, 0);

        alumniTable = new JTable(model);
        alumniTable.setRowHeight(80);
        alumniTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        alumniTable.setBackground(Color.WHITE);
        alumniTable.setForeground(Color.BLACK);
        alumniTable.setSelectionBackground(new Color(30, 144, 255));
        alumniTable.setSelectionForeground(Color.WHITE);
        alumniTable.setGridColor(Color.LIGHT_GRAY);
        alumniTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(alumniTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        background.add(scrollPane, BorderLayout.CENTER);

        // === Load Data ===
        loadDepartments();
        loadAlumniData(null, null);

        alumniTable.getColumn("Photo").setCellRenderer(new ImageRenderer());

        // === Listeners ===
        searchButton.addActionListener(e -> performSearch());
        resetButton.addActionListener(e -> resetFilters());
        deptComboBox.addActionListener(e -> performSearch());
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });

        // === Clickable Photos ===
        alumniTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = alumniTable.getSelectedColumn();
                int row = alumniTable.getSelectedRow();
                if (col == 10 && row >= 0) { // Photo column
                    String path = (String) model.getValueAt(row, col);
                    showFullImage(path);
                }
            }
        });
    }

    private void loadDepartments() {
        deptComboBox.addItem("All");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT DISTINCT department FROM alumni")) {

            HashSet<String> depts = new HashSet<>();
            while (rs.next()) {
                depts.add(rs.getString("department"));
            }
            for (String d : depts) deptComboBox.addItem(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAlumniData(String searchText, String department) {
        model.setRowCount(0);
        StringBuilder query = new StringBuilder("SELECT * FROM alumni WHERE 1=1");

        if (searchText != null && !searchText.isEmpty()) {
            query.append(" AND (name LIKE ? OR email LIKE ? OR company LIKE ?)");
        }
        if (department != null && !"All".equalsIgnoreCase(department)) {
            query.append(" AND department = ?");
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            int index = 1;
            if (searchText != null && !searchText.isEmpty()) {
                String text = "%" + searchText + "%";
                ps.setString(index++, text);
                ps.setString(index++, text);
                ps.setString(index++, text);
            }
            if (department != null && !"All".equalsIgnoreCase(department)) {
                ps.setString(index++, department);
            }

            ResultSet rs = ps.executeQuery();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        String department = (String) deptComboBox.getSelectedItem();
        loadAlumniData(searchText, department);
    }

    private void resetFilters() {
        searchField.setText("");
        deptComboBox.setSelectedIndex(0);
        loadAlumniData(null, null);
    }

    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            try {
                String path = (String) value;
                if (path != null && !path.trim().isEmpty()) {
                    File imageFile = new File(path);
                    if (imageFile.exists()) {
                        BufferedImage img = ImageIO.read(imageFile);
                        Image scaled = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(scaled));
                    } else label.setText("Not Found");
                } else label.setText("No Image");
            } catch (Exception ex) {
                label.setText("No Image");
            }
            return label;
        }
    }

    private void showFullImage(String path) {
        if (path == null || path.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No image available for this record.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            File file = new File(path);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "Image file not found:\n" + path, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BufferedImage img = ImageIO.read(file);
            Image scaled = img.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));

            JDialog dialog = new JDialog(this, "Alumni Photo", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(imgLabel, BorderLayout.CENTER);
            dialog.setSize(420, 420);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to open image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewAlumniFrame().setVisible(true));
    }
}
