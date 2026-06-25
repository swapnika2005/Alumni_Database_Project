package alumni.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import alumni.ui.UserDashboardFrame;
import alumni.ui.AdminDashboardFrame;


public class AlumniUI extends JFrame {
    private String role;
    private String username;

    public AlumniUI(String role, String username) {
        this.role = role;
        this.username = username;

        setTitle("Alumni Management System - Dashboard (" + role + ")");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Welcome Header ===
        JLabel header = new JLabel("Welcome, " + username + " (" + role + ")", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // === Center Panel ===
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // === Buttons ===
        JButton viewAlumniBtn = new JButton("View Alumni");
        JButton eventsBtn = new JButton("Events");
        JButton profileBtn = new JButton("My Profile");
        JButton logoutBtn = new JButton("Logout");

        viewAlumniBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        eventsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        profileBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        centerPanel.add(viewAlumniBtn);
        centerPanel.add(eventsBtn);
        centerPanel.add(profileBtn);
        centerPanel.add(logoutBtn);

        add(centerPanel, BorderLayout.CENTER);

        // === Admin-only Panel ===
        if (role.equalsIgnoreCase("admin")) {
            JPanel adminPanel = new JPanel(new FlowLayout());
            adminPanel.setBorder(BorderFactory.createTitledBorder("Admin Controls"));
            JButton manageUsersBtn = new JButton("Manage Users");
            JButton reportsBtn = new JButton("Generate Reports");
            adminPanel.add(manageUsersBtn);
            adminPanel.add(reportsBtn);
            add(adminPanel, BorderLayout.SOUTH);

            manageUsersBtn.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "Open Manage Users Page (Admin Only)"));
            reportsBtn.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "Generate Reports (Admin Only)"));
        }

        // === Logout Action ===
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginUI().setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AlumniUI("admin", "admin").setVisible(true));
    }
}
