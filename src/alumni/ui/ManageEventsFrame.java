package alumni.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import alumni.db.DBConnection;

public class ManageEventsFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTitle, txtLocation, txtDate;
    private JTextArea txtDescription;

    public ManageEventsFrame() {
        setTitle("Manage Events - Admin Panel");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Background ===
        try {
            URL bgUrl = new URL("https://plus.unsplash.com/premium_photo-1714265042273-9664e4537f7d?ixlib=rb-4.1.0&auto=format&fit=crop&q=60&w=600");
            ImageIcon bgIcon = new ImageIcon(bgUrl);
            Image img = bgIcon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
            JLabel background = new JLabel(new ImageIcon(img));
            background.setLayout(new BorderLayout());
            setContentPane(background);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // === Top Panel (Title + Buttons) ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JLabel heading = new JLabel("Manage Events", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(Color.black);
        heading.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        topPanel.add(heading, BorderLayout.CENTER);

        JButton btnBack = new JButton("← Back to Dashboard");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.setBackground(new Color(102, 102, 102));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> {
            dispose();
            new AdminDashboardFrame("Admin").setVisible(true);
        });
        topPanel.add(btnBack, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // === Table ===
        model = new DefaultTableModel(new String[]{"ID", "Title", "Description", "Event Date", "Location"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // === Form Panel (Add/Edit) ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Title:");
        JLabel lblDesc = new JLabel("Description:");
        JLabel lblDate = new JLabel("Event Date (YYYY-MM-DD):");
        JLabel lblLoc = new JLabel("Location:");

        txtTitle = new JTextField(20);
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDate = new JTextField(10);
        txtLocation = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblTitle, gbc);
        gbc.gridx = 1; formPanel.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblDesc, gbc);
        gbc.gridx = 1; formPanel.add(new JScrollPane(txtDescription), gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblDate, gbc);
        gbc.gridx = 1; formPanel.add(txtDate, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblLoc, gbc);
        gbc.gridx = 1; formPanel.add(txtLocation, gbc);

        // === Buttons ===
        JButton btnAdd = new JButton("Add Event");
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Selected");

        btnAdd.setBackground(new Color(0, 102, 204));
        btnAdd.setForeground(Color.WHITE);
        btnEdit.setBackground(new Color(0, 153, 76));
        btnEdit.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(204, 0, 0));
        btnDelete.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // === Load Events ===
        loadEvents();

        // === Button Actions ===
        btnAdd.addActionListener(e -> addEvent());
        btnEdit.addActionListener(e -> editEvent());
        btnDelete.addActionListener(e -> deleteEvent());
    }

    private void loadEvents() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM event")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("event_date"),
                        rs.getString("location")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEvent() {
        String title = txtTitle.getText();
        String desc = txtDescription.getText();
        String date = txtDate.getText();
        String location = txtLocation.getText();

        if (title.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Date are required!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO event (title, description, event_date, location) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, date);
            ps.setString(4, location);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Event added successfully!");
            loadEvents();
            clearFields();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding event: " + ex.getMessage());
        }
    }

    private void editEvent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to edit.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String title = txtTitle.getText();
        String desc = txtDescription.getText();
        String date = txtDate.getText();
        String location = txtLocation.getText();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE event SET title=?, description=?, event_date=?, location=? WHERE id=?")) {
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, date);
            ps.setString(4, location);
            ps.setInt(5, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Event updated successfully!");
            loadEvents();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating event: " + ex.getMessage());
        }
    }

    private void deleteEvent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an event to delete.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this event?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM event WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Event deleted successfully!");
            loadEvents();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting event: " + ex.getMessage());
        }
    }

    private void clearFields() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtDate.setText("");
        txtLocation.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageEventsFrame().setVisible(true));
    }
}
