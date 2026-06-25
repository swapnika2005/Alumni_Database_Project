package alumni.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.sql.*;
import alumni.db.DBConnection;

public class AlumniManagementFrame extends JFrame {
    private JTextField nameField, emailField, gradYearField, courseField, mobileField, maritalField;
    private JLabel photoLabel;
    private File selectedImage;

    public AlumniManagementFrame() {
        setTitle("Add Alumni Details");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(9, 2, 10, 10));

        nameField = new JTextField();
        emailField = new JTextField();
        gradYearField = new JTextField();
        courseField = new JTextField();
        mobileField = new JTextField();
        maritalField = new JTextField();
        photoLabel = new JLabel("No Image Selected");

        JButton browseBtn = new JButton("Browse");
        JButton saveBtn = new JButton("Save");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Graduation Year:"));
        add(gradYearField);
        add(new JLabel("Course:"));
        add(courseField);
        add(new JLabel("Mobile:"));
        add(mobileField);
        add(new JLabel("Marital Status:"));
        add(maritalField);
        add(photoLabel);
        add(browseBtn);
        add(new JLabel());
        add(saveBtn);

        browseBtn.addActionListener(e -> selectImage());
        saveBtn.addActionListener(e -> saveAlumni());
    }

    private void selectImage() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImage = fc.getSelectedFile();
            photoLabel.setText(selectedImage.getName());
        }
    }

    private void saveAlumni() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO alumni(name,email,grad_year,course,mobile,marital_status,photo_path) VALUES (?,?,?,?,?,?,?)")) {

            ps.setString(1, nameField.getText());
            ps.setString(2, emailField.getText());
            ps.setInt(3, Integer.parseInt(gradYearField.getText()));
            ps.setString(4, courseField.getText());
            ps.setString(5, mobileField.getText());
            ps.setString(6, maritalField.getText());
            ps.setString(7, selectedImage != null ? selectedImage.getAbsolutePath() : null);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Alumni added successfully!");
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving alumni details: " + e.getMessage());
        }
    }
}
