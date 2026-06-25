package alumni.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;

public class DeleteUserFrame extends JFrame {
    private static final String URL_DB = "jdbc:mysql://localhost:3306/alumni";
    private static final String USER = "root";
    private static final String PASSWORD = "2005"; // change as needed

    private JTable table;
    private DefaultTableModel model;
    private JTextField deleteIdField;

    public DeleteUserFrame() {
        setTitle("Delete Alumni Record");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Background setup ===
        JLabel background;
        try {
            URL bgUrl = new URL("https://images.unsplash.com/photo-1627556704314-1f7aee38ec57?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTE1fHxhbHVtbml8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=600");
            ImageIcon bgIcon = new ImageIcon(bgUrl);
            Image scaled = bgIcon.getImage().getScaledInstance(950, 600, Image.SCALE_SMOOTH);
            background = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            background = new JLabel();
            System.out.println("⚠️ Background image couldn't load: " + e.getMessage());
        }

        background.setLayout(new BorderLayout());
        setContentPane(background);

        // === Table setup ===
        String[] columns = {
            "ID", "Name", "Email", "Passout Year", "Department",
            "Company", "Position", "Location", "Mobile", "Marital Status"
        };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(255, 120, 120, 220));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(255, 255, 255, 230));
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };

        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(20, 20, 20));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        background.add(scrollPane, BorderLayout.CENTER);

        // === Load data ===
        loadData();

        // === Double-click delete ===
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String id = model.getValueAt(row, 0).toString();
                        String name = model.getValueAt(row, 1).toString();
                        deleteUser(id, name);
                    }
                }
            }
        });

        // === Bottom Panel ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setOpaque(false);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = model.getValueAt(row, 0).toString();
                String name = model.getValueAt(row, 1).toString();
                deleteUser(id, name);
            } else {
                JOptionPane.showMessageDialog(DeleteUserFrame.this,
                        "Please select a record to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        JLabel idLabel = new JLabel("Delete by ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(Color.WHITE);

        deleteIdField = new JTextField(10);
        deleteIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton deleteByIdBtn = new JButton("Delete");
        deleteByIdBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteByIdBtn.setBackground(new Color(255, 140, 0));
        deleteByIdBtn.setForeground(Color.WHITE);
        deleteByIdBtn.setFocusPainted(false);
        deleteByIdBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        deleteByIdBtn.addActionListener(e -> {
            String id = deleteIdField.getText().trim();
            if (!id.isEmpty()) {
                deleteUserById(id);
            } else {
                JOptionPane.showMessageDialog(DeleteUserFrame.this,
                        "Please enter an ID to delete.", "Input Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        bottomPanel.add(deleteBtn);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(idLabel);
        bottomPanel.add(deleteIdField);
        bottomPanel.add(deleteByIdBtn);

        background.add(bottomPanel, BorderLayout.SOUTH);
    }

    // === Load all alumni data ===
    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(URL_DB, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT id, name, email, passout_year, department, company, position, location, mobile, marital_status FROM alumni")) {

            while (rs.next()) {
                Object[] row = {
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
                };
                model.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading alumni: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === Delete by name + id (from table) ===
    private void deleteUser(String id, String name) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + name + " (ID: " + id + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(URL_DB, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM alumni WHERE id = ?")) {
                stmt.setString(1, id);

                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully.",
                            "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "No matching record found.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // === Delete by ID ===
    private void deleteUserById(String id) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the record with ID: " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(URL_DB, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM alumni WHERE id = ?")) {
                stmt.setString(1, id);

                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully.",
                            "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    deleteIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No record found with ID: " + id,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteUserFrame().setVisible(true));
    }
}
