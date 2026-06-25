package alumni.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import alumni.db.DBConnection;

public class AdminEventFrame extends JFrame {

    private JTable eventTable;
    private JTextField titleField, dateField, locationField;
    private JTextArea descriptionArea;
    private String adminName;

    public AdminEventFrame(String adminName) {
        this.adminName = adminName;

        setTitle("Manage Events - Admin: " + adminName);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Header =====
        JLabel header = new JLabel("Event Management", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        add(header, BorderLayout.NORTH);

        // ===== Table =====
        eventTable = new JTable(new DefaultTableModel(
                new Object[]{"Event ID", "Title", "Date", "Location", "Description"}, 0
        ));
        JScrollPane tableScroll = new JScrollPane(eventTable);
        add(tableScroll, BorderLayout.CENTER);

        // ===== Form Panel =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Event"));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dateField = new JTextField(10);
        formPanel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        locationField = new JTextField(20);
        formPanel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        JButton addButton = new JButton("Add Event");
        JButton deleteButton = new JButton("Delete Selected");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Button Actions =====
        addButton.addActionListener(e -> addEvent());
        deleteButton.addActionListener(e -> deleteEvent());
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboardFrame(adminName).setVisible(true);

        });

        loadEvents();
    }

    private void loadEvents() {
        DefaultTableModel model = (DefaultTableModel) eventTable.getModel();
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT event_id, title, event_date, location, description FROM events ORDER BY event_date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("event_id"),
                        rs.getString("title"),
                        rs.getString("event_date"),
                        rs.getString("location"),
                        rs.getString("description")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading events.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEvent() {
        String title = titleField.getText().trim();
        String date = dateField.getText().trim();
        String location = locationField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || date.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO events (title, event_date, location, description) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, date);
            ps.setString(3, location);
            ps.setString(4, description);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Event added successfully!");
            clearFields();
            loadEvents();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add event.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int eventId = (int) eventTable.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this event?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "DELETE FROM events WHERE event_id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, eventId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Event deleted successfully.");
                loadEvents();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting event.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        titleField.setText("");
        dateField.setText("");
        locationField.setText("");
        descriptionArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminEventFrame("adminUser").setVisible(true));
    }
}
