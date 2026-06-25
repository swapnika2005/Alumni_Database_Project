package alumni.ui;

import alumni.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.sql.*;

public class AddAlumniFrame extends JFrame {
    private JTextField nameField, emailField, yearField, deptField, companyField,
            positionField, locationField, mobileField;
    private JComboBox<String> maritalStatusBox;
    private JLabel photoLabel;
    private JButton uploadPhotoBtn, saveBtn, cancelBtn;
    private String photoPath = null;
    private String adminName;

    public AddAlumniFrame(String adminName) {
        this.adminName = adminName;

        setTitle("Add Alumni - Admin Panel");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // ==============================
        // Background Image
        // ==============================
        ImageIcon bgIcon = null;
        try {
            bgIcon = new ImageIcon(
                    new ImageIcon(new URL("https://images.unsplash.com/photo-1627556704314-1f7aee38ec57?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTE1fHxhbHVtbml8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=600"))
                            .getImage().getScaledInstance(600, 750, Image.SCALE_SMOOTH)
            );
        } catch (Exception e) {
            bgIcon = new ImageIcon();
        }

        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 600, 750);
        background.setLayout(null);
        add(background);

        // ==============================
        // Title
        // ==============================
        JLabel title = new JLabel("Add Alumni Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.black);
        title.setBounds(180, 20, 300, 40);
        background.add(title);

        // ==============================
        // Labels & Text Fields
        // ==============================
        Font labelFont = new Font("Arial", Font.BOLD, 15);
        Color labelColor = Color.WHITE;

        int y = 90;
        int labelX = 70;
        int fieldX = 220;

        String[] labelNames = {
                "Name:", "Email:", "Year:", "Department:", "Company:",
                "Position:", "Location:", "Mobile:", "Marital Status:", "Photo:"
        };

        JTextField[] fields = {
                nameField = new JTextField(), emailField = new JTextField(),
                yearField = new JTextField(), deptField = new JTextField(),
                companyField = new JTextField(), positionField = new JTextField(),
                locationField = new JTextField(), mobileField = new JTextField()
        };

        for (int i = 0; i < labelNames.length - 2; i++) {
            JLabel lbl = new JLabel(labelNames[i]);
            lbl.setFont(labelFont);
            lbl.setForeground(labelColor);
            lbl.setBounds(labelX, y, 140, 25);
            background.add(lbl);

            fields[i].setBounds(fieldX, y, 250, 28);
            background.add(fields[i]);
            y += 45;
        }

        // ==============================
        // Marital Status
        // ==============================
        JLabel maritalLbl = new JLabel("Marital Status:");
        maritalLbl.setFont(labelFont);
        maritalLbl.setForeground(labelColor);
        maritalLbl.setBounds(labelX, y, 140, 25);
        background.add(maritalLbl);

        maritalStatusBox = new JComboBox<>(new String[]{"Unmarried", "Married"});
        maritalStatusBox.setBounds(fieldX, y, 250, 28);
        background.add(maritalStatusBox);
        y += 50;

        // ==============================
        // Photo Upload Section
        // ==============================
        JLabel photoLbl = new JLabel("Photo:");
        photoLbl.setFont(labelFont);
        photoLbl.setForeground(labelColor);
        photoLbl.setBounds(labelX, y, 100, 25);
        background.add(photoLbl);

        photoLabel = new JLabel();
        photoLabel.setBounds(fieldX, y, 120, 120);
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        background.add(photoLabel);

        uploadPhotoBtn = new JButton("Upload Photo");
        uploadPhotoBtn.setBounds(fieldX + 130, y + 45, 130, 30);
        background.add(uploadPhotoBtn);
        y += 150;

        // ==============================
        // Buttons
        // ==============================
        saveBtn = new JButton("Save Alumni");
        saveBtn.setBackground(new Color(34, 139, 34));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 15));
        saveBtn.setBounds(140, y, 140, 40);
        background.add(saveBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.RED);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 15));
        cancelBtn.setBounds(310, y, 140, 40);
        background.add(cancelBtn);

        // ==============================
        // Button Actions
        // ==============================
        uploadPhotoBtn.addActionListener(e -> uploadPhoto());
        saveBtn.addActionListener(e -> saveAlumni());
        cancelBtn.addActionListener(e -> dispose());
    }

    // ------------------------------
    // Photo Upload Logic
    // ------------------------------
    private void uploadPhoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Alumni Photo");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            // Create directory for storing alumni photos
            File destDir = new File("resource/alumni_photos");
            if (!destDir.exists()) destDir.mkdirs();

            // Copy file to local folder
            String newPath = destDir.getAbsolutePath() + File.separator + file.getName();
            try (InputStream in = new FileInputStream(file);
                 OutputStream out = new FileOutputStream(newPath)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                photoPath = newPath;
                ImageIcon imgIcon = new ImageIcon(new ImageIcon(photoPath)
                        .getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
                photoLabel.setIcon(imgIcon);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error uploading photo: " + ex.getMessage());
            }
        }
    }

    // ------------------------------
    // Save Alumni Details
    // ------------------------------
    private void saveAlumni() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String year = yearField.getText().trim();
        String dept = deptField.getText().trim();
        String company = companyField.getText().trim();
        String position = positionField.getText().trim();
        String location = locationField.getText().trim();
        String mobile = mobileField.getText().trim();
        String maritalStatus = maritalStatusBox.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || year.isEmpty() || dept.isEmpty() ||
            company.isEmpty() || position.isEmpty() || location.isEmpty() || mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO alumni (name, email, year, department, company, position, location, mobile, marital_status, photo_path) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, year);
            ps.setString(4, dept);
            ps.setString(5, company);
            ps.setString(6, position);
            ps.setString(7, location);
            ps.setString(8, mobile);
            ps.setString(9, maritalStatus);
            ps.setString(10, photoPath);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Alumni added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ------------------------------
    // Clear Input Fields
    // ------------------------------
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        yearField.setText("");
        deptField.setText("");
        companyField.setText("");
        positionField.setText("");
        locationField.setText("");
        mobileField.setText("");
        maritalStatusBox.setSelectedIndex(0);
        photoLabel.setIcon(null);
        photoPath = null;
    }

    // ------------------------------
    // Main
    // ------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddAlumniFrame("Admin").setVisible(true));
    }
}
