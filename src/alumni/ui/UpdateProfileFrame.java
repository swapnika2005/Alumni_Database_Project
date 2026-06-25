package alumni.ui;

import alumni.dao.AlumniDAO;
import alumni.db.DBConnection;
import alumni.model.Alumni;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.Connection;

public class UpdateProfileFrame extends JFrame {

    private JTextField txtId, txtName, txtEmail, txtYear, txtCourse, txtCompany, txtPosition, txtLocation, txtMobile, txtStatus;
    private JLabel lblPhoto;
    private File selectedImageFile;
    private Alumni currentUser;

    public UpdateProfileFrame(String userName) {
        setTitle("Update Profile - " + userName);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🌄 Background
        JLabel background;
        try {
            URL imageUrl = new URL("https://images.unsplash.com/photo-1658235081452-c2ded30b8d9f?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nzh8fGFsdW1uaXxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=600"
        );
            ImageIcon bgIcon = new ImageIcon(new ImageIcon(imageUrl).getImage().getScaledInstance(1000, 650, Image.SCALE_SMOOTH));
            background = new JLabel(bgIcon);
        } catch (Exception e) {
            background = new JLabel();
            background.setOpaque(true);
            background.setBackground(Color.LIGHT_GRAY);
        }
        background.setLayout(new GridBagLayout());
        add(background);

        // 🧱 Main Panel (Card Look)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 255, 230));
        mainPanel.setPreferredSize(new Dimension(900, 520));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Update Your Profile", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(new EmptyBorder(10, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // 🌈 Split layout: left form, right photo area
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        centerPanel.setOpaque(false);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 🧾 Left form area
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("ID:", labelFont), gbc);
        gbc.gridx = 1; txtId = createTextField(fieldFont, false); formPanel.add(txtId, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("Name:", labelFont), gbc);
        gbc.gridx = 1; txtName = createTextField(fieldFont, false); formPanel.add(txtName, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("Email:", labelFont), gbc);
        gbc.gridx = 1; txtEmail = createTextField(fieldFont, true); formPanel.add(txtEmail, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("Graduation Year:", labelFont), gbc);
        gbc.gridx = 1; txtYear = createTextField(fieldFont, true); formPanel.add(txtYear, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("Course:", labelFont), gbc);
        gbc.gridx = 1; txtCourse = createTextField(fieldFont, true); formPanel.add(txtCourse, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("Company:", labelFont), gbc);
        gbc.gridx = 1; txtCompany = createTextField(fieldFont, true); formPanel.add(txtCompany, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("Position:", labelFont), gbc);
        gbc.gridx = 1; txtPosition = createTextField(fieldFont, true); formPanel.add(txtPosition, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("Location:", labelFont), gbc);
        gbc.gridx = 1; txtLocation = createTextField(fieldFont, true); formPanel.add(txtLocation, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("Mobile:", labelFont), gbc);
        gbc.gridx = 1; txtMobile = createTextField(fieldFont, true); formPanel.add(txtMobile, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(createLabel("Status:", labelFont), gbc);
        gbc.gridx = 1; txtStatus = createTextField(fieldFont, true); formPanel.add(txtStatus, gbc); y++;

        centerPanel.add(formPanel);

        // 🖼️ Right photo section
        JPanel photoPanel = new JPanel();
        photoPanel.setOpaque(false);
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        lblPhoto = new JLabel("No Photo", SwingConstants.CENTER);
        lblPhoto.setPreferredSize(new Dimension(200, 200));
        lblPhoto.setMaximumSize(new Dimension(200, 200));
        lblPhoto.setOpaque(true);
        lblPhoto.setBackground(Color.WHITE);
        lblPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lblPhoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnChoose = createButton("Choose Photo", new Color(0, 102, 204));
        btnChoose.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChoose.addActionListener(e -> choosePhoto());

        JButton btnSave = createButton("💾 Save Changes", new Color(0, 102, 204));
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.addActionListener(e -> saveChanges());

        JButton btnBack = createButton("← Back", new Color(128, 128, 128));
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.addActionListener(e -> dispose());

        photoPanel.add(lblPhoto);
        photoPanel.add(Box.createVerticalStrut(20));
        photoPanel.add(btnChoose);
        photoPanel.add(Box.createVerticalStrut(20));
        photoPanel.add(btnSave);
        photoPanel.add(Box.createVerticalStrut(20));
        photoPanel.add(btnBack);

        centerPanel.add(photoPanel);
        background.add(mainPanel);

        // Load User Info
        loadUserData(userName);

        setVisible(true);
    }

    // 🧠 Load user data
    private void loadUserData(String userName) {
        try {
            Connection conn = DBConnection.getConnection();
            AlumniDAO dao = new AlumniDAO(conn);
            currentUser = dao.getAlumniByUsername(userName);

            if (currentUser != null) {
                txtId.setText(String.valueOf(currentUser.getId()));
                txtName.setText(currentUser.getName());
                txtEmail.setText(currentUser.getEmail());
                txtYear.setText(String.valueOf(currentUser.getGradYear()));
                txtCourse.setText(currentUser.getDepartment());
                txtCompany.setText(currentUser.getCompany());
                txtPosition.setText(currentUser.getPosition());
                txtLocation.setText(currentUser.getLocation());
                txtMobile.setText(currentUser.getMobile());
                txtStatus.setText(currentUser.getMaritalStatus());

                if (currentUser.getPhotoPath() != null && !currentUser.getPhotoPath().isEmpty()) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(currentUser.getPhotoPath())
                            .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                    lblPhoto.setIcon(icon);
                    lblPhoto.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No record found for " + userName);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    // 💾 Save updates
    private void saveChanges() {
        try {
            Connection conn = DBConnection.getConnection();
            AlumniDAO dao = new AlumniDAO(conn);
            Alumni updated = new Alumni(
                    Integer.parseInt(txtId.getText()),
                    txtName.getText(),
                    txtEmail.getText(),
                    Integer.parseInt(txtYear.getText()),
                    txtCourse.getText(),
                    txtCompany.getText(),
                    txtPosition.getText(),
                    txtLocation.getText(),
                    txtMobile.getText(),
                    txtStatus.getText(),
                    (selectedImageFile != null ? selectedImageFile.getAbsolutePath() : currentUser.getPhotoPath())
            );
            if (dao.updateAlumni(updated)) {
                JOptionPane.showMessageDialog(this, "✅ Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Update failed.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
        }
    }

    private void choosePhoto() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = chooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(new ImageIcon(selectedImageFile.getAbsolutePath())
                    .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
            lblPhoto.setIcon(icon);
            lblPhoto.setText("");
        }
    }

    // 🔹 Helper UI methods
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JTextField createTextField(Font font, boolean editable) {
        JTextField field = new JTextField(15);
        field.setFont(font);
        field.setEditable(editable);
        return field;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateProfileFrame("demoUser"));
    }
}
