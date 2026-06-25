package alumni.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import javax.imageio.ImageIO;
import alumni.db.DBConnection;

public class AddUserFrame extends JFrame {
    private JTextField txtId, txtName, txtEmail, txtYear, txtDept, txtCompany, txtPosition, txtLocation, txtMobile;
    private JComboBox<String> cmbMarital;
    private JLabel lblPhoto;
    private String photoPath = null;

    public AddUserFrame() {
        setTitle("Add New Alumni - Admin Panel");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Background from online URL ===
        try {
            URL bgUrl = new URL("https://images.unsplash.com/photo-1627556704314-1f7aee38ec57?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTE1fHxhbHVtbml8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=600");
            ImageIcon bgIcon = new ImageIcon(bgUrl);
            Image img = bgIcon.getImage().getScaledInstance(950, 650, Image.SCALE_SMOOTH);
            JLabel background = new JLabel(new ImageIcon(img));
            background.setLayout(new BorderLayout());
            setContentPane(background);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // === Title ===
        JLabel lblTitle = new JLabel("👤 Add New Alumni", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.white);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // === Form Panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblId = new JLabel("ID:");
        JLabel lblName = new JLabel("Name:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblYear = new JLabel("Passout Year:");
        JLabel lblDept = new JLabel("Department:");
        JLabel lblCompany = new JLabel("Company:");
        JLabel lblPosition = new JLabel("Position:");
        JLabel lblLocation = new JLabel("Location:");
        JLabel lblMobile = new JLabel("Mobile:");
        JLabel lblMarital = new JLabel("Marital Status:");
        JLabel lblPhotoLabel = new JLabel("Photo:");

        txtId = new JTextField(15);
        txtName = new JTextField(15);
        txtEmail = new JTextField(15);
        txtYear = new JTextField(10);
        txtDept = new JTextField(15);
        txtCompany = new JTextField(15);
        txtPosition = new JTextField(15);
        txtLocation = new JTextField(15);
        txtMobile = new JTextField(15);
        cmbMarital = new JComboBox<>(new String[]{"Single", "Married", "Other"});

        lblPhoto = new JLabel("No Image", SwingConstants.CENTER);
        lblPhoto.setPreferredSize(new Dimension(140, 140));
        lblPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lblPhoto.setOpaque(true);
        lblPhoto.setBackground(Color.LIGHT_GRAY);

        JButton btnBrowse = new JButton("Upload Photo");
        btnBrowse.setBackground(new Color(0, 123, 255));
        btnBrowse.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblId, gbc);
        gbc.gridx = 1; formPanel.add(txtId, gbc);
        gbc.gridx = 2; formPanel.add(lblEmail, gbc);
        gbc.gridx = 3; formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblName, gbc);
        gbc.gridx = 1; formPanel.add(txtName, gbc);
        gbc.gridx = 2; formPanel.add(lblDept, gbc);
        gbc.gridx = 3; formPanel.add(txtDept, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblYear, gbc);
        gbc.gridx = 1; formPanel.add(txtYear, gbc);
        gbc.gridx = 2; formPanel.add(lblCompany, gbc);
        gbc.gridx = 3; formPanel.add(txtCompany, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblPosition, gbc);
        gbc.gridx = 1; formPanel.add(txtPosition, gbc);
        gbc.gridx = 2; formPanel.add(lblLocation, gbc);
        gbc.gridx = 3; formPanel.add(txtLocation, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(lblMobile, gbc);
        gbc.gridx = 1; formPanel.add(txtMobile, gbc);
        gbc.gridx = 2; formPanel.add(lblMarital, gbc);
        gbc.gridx = 3; formPanel.add(cmbMarital, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(lblPhotoLabel, gbc);
        gbc.gridx = 1; formPanel.add(lblPhoto, gbc);
        gbc.gridx = 2; formPanel.add(btnBrowse, gbc);

        add(formPanel, BorderLayout.CENTER);

        // === Button Panel ===
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        JButton btnSave = new JButton("Save Alumni");
        btnSave.setBackground(new Color(0, 153, 51));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnClear = new JButton("Clear");
        btnClear.setBackground(new Color(255, 193, 7));
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnBack = new JButton("← Back to Dashboard");
        btnBack.setBackground(new Color(102, 102, 102));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btnPanel.add(btnSave);
        btnPanel.add(btnClear);
        btnPanel.add(btnBack);
        add(btnPanel, BorderLayout.SOUTH);

        // === Event Listeners ===
        btnBrowse.addActionListener(e -> choosePhoto());
        btnSave.addActionListener(e -> saveAlumni());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> {
            dispose();
            new AdminDashboardFrame("Admin").setVisible(true);
        });
    }

    private void choosePhoto() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            photoPath = file.getAbsolutePath();
            try {
                Image img = ImageIO.read(file).getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                lblPhoto.setIcon(new ImageIcon(img));
                lblPhoto.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAlumni() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String year = txtYear.getText().trim();
        String dept = txtDept.getText().trim();
        String company = txtCompany.getText().trim();
        String position = txtPosition.getText().trim();
        String location = txtLocation.getText().trim();
        String mobile = txtMobile.getText().trim();
        String marital = (String) cmbMarital.getSelectedItem();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all mandatory fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO alumni (id, name, email, passout_year, department, company, position, location, mobile, marital_status, photo_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, year);
            ps.setString(5, dept);
            ps.setString(6, company);
            ps.setString(7, position);
            ps.setString(8, location);
            ps.setString(9, mobile);
            ps.setString(10, marital);
            ps.setString(11, photoPath);

            int inserted = ps.executeUpdate();
            if (inserted > 0) {
                JOptionPane.showMessageDialog(this, "Alumni added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add alumni.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtYear.setText("");
        txtDept.setText("");
        txtCompany.setText("");
        txtPosition.setText("");
        txtLocation.setText("");
        txtMobile.setText("");
        cmbMarital.setSelectedIndex(0);
        lblPhoto.setIcon(null);
        lblPhoto.setText("No Image");
        photoPath = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddUserFrame().setVisible(true));
    }
}
