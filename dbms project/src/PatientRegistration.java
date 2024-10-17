import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.bson.Document;
import java.util.regex.Pattern;

public class PatientRegistration {
    private MongoDBConnection mongoDBConnection;

    public PatientRegistration() {
        mongoDBConnection = new MongoDBConnection();

        JFrame frame = new JFrame("Patient Registration");
        frame.setSize(700, 700); // Increased size for additional fields
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        // Custom JPanel to create gradient background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Create gradient
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, Color.BLACK, 0, getHeight(), new Color(0, 0, 139)); // Dark Blue
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(new GridBagLayout());
        frame.setContentPane(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Title Label
        JLabel titleLabel = new JLabel("Patient Data");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size for title
        titleLabel.setForeground(Color.WHITE); // Set text color to white
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1; // Reset to default

        // Name Label and Field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        // Phone Number Label and Field
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 18));
        phoneLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(10);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 16));
        phoneField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        // Email Label and Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 18));
        emailLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Age Label and Field
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ageLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(ageLabel, gbc);

        JTextField ageField = new JTextField(5);
        ageField.setFont(new Font("Arial", Font.PLAIN, 16));
        ageField.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 1;
        panel.add(ageField, gbc);

        // Gender Label and ComboBox
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Arial", Font.BOLD, 18));
        genderLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(genderLabel, gbc);

        String[] genders = {"Male", "Female", "Other"};
        JComboBox<String> genderCombo = new JComboBox<>(genders);
        genderCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        genderCombo.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        panel.add(genderCombo, gbc);

        // Address Label and Field
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 18));
        addressLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(addressLabel, gbc);

        JTextField addressField = new JTextField(30);
        addressField.setFont(new Font("Arial", Font.PLAIN, 16));
        addressField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        // Marital Status Checkboxes
        JLabel maritalStatusLabel = new JLabel("Marital Status:");
        maritalStatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        maritalStatusLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(maritalStatusLabel, gbc);

        JCheckBox marriedCheckBox = new JCheckBox("Married");
        marriedCheckBox.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size
        marriedCheckBox.setForeground(Color.BLACK); // Set text color to white
        JCheckBox unmarriedCheckBox = new JCheckBox("Unmarried");
        unmarriedCheckBox.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size
        unmarriedCheckBox.setForeground(Color.BLACK); // Set text color to white

        JPanel maritalPanel = new JPanel();
        maritalPanel.setOpaque(false); // Make the panel transparent
        maritalPanel.add(marriedCheckBox);
        maritalPanel.add(unmarriedCheckBox);
        gbc.gridx = 1;
        panel.add(maritalPanel, gbc);

        // Medicare Checkbox
        JCheckBox medicareCheckBox = new JCheckBox("I accept Medicare");
        medicareCheckBox.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size
        medicareCheckBox.setForeground(Color.BLACK); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2; // Span across two columns
        panel.add(medicareCheckBox, gbc);
        gbc.gridwidth = 1; // Reset to default

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(50, 205, 50)); // Lime Green
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, gbc);

        // Add ActionListener for submit button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String age = ageField.getText();
                String gender = (String) genderCombo.getSelectedItem();
                String address = addressField.getText();
                boolean isMarried = marriedCheckBox.isSelected();
                boolean isUnmarried = unmarriedCheckBox.isSelected();
                boolean acceptsMedicare = medicareCheckBox.isSelected();

                // Validation
                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || age.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                    return;
                }

                if (!Pattern.matches("\\d{10}", phone)) {
                    JOptionPane.showMessageDialog(frame, "Phone number must be 10 digits.");
                    return;
                }

                if (!isValidEmail(email)) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid email address.");
                    return;
                }

                // Create a document to store in MongoDB
                Document patientDocument = new Document("name", name)
                        .append("phone", phone)
                        .append("email", email)
                        .append("age", age)
                        .append("gender", gender)
                        .append("address", address)
                        .append("maritalStatus", isMarried ? "Married" : "Unmarried")
                        .append("acceptsMedicare", acceptsMedicare);

                try {
                    mongoDBConnection.getPatientCollection().insertOne(patientDocument);
                    JOptionPane.showMessageDialog(frame, "Patient registered successfully!");
                    nameField.setText("");
                    phoneField.setText("");
                    emailField.setText("");
                    ageField.setText("");
                    genderCombo.setSelectedIndex(0); // Reset gender selection
                    addressField.setText(""); // Clear address field
                    marriedCheckBox.setSelected(false); // Uncheck married checkbox
                    unmarriedCheckBox.setSelected(false); // Uncheck unmarried checkbox
                    medicareCheckBox.setSelected(false); // Uncheck medicare checkbox

                
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving patient data: " + ex.getMessage());
                }
            }
        });

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 69, 0)); // Red-Orange
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 1;
        frame.add(backButton, gbc);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HomePage();
                frame.dispose(); // Close the registration page
            }
        });

        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(emailRegex, email);
    }
}