import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.bson.Document;

public class RescheduleAppointment {
    private MongoDBConnection mongoDBConnection;
    private JTextField patientNameField;
    private JTextField phoneNumberField;
    private JTextField medicationField;
    private JTextField dateField;
    private JTextField timeField;
    private JComboBox<String> locationDropdown;

    public RescheduleAppointment() {
        mongoDBConnection = new MongoDBConnection();

        // Create reschedule appointment frame
        JFrame frame = new JFrame("Reschedule Appointment");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create a JPanel with gradient background
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
        panel.setLayout(new GridLayout(8, 2, 10, 10)); // Changed to 8 rows for the back button

        // Set font and color for labels
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 18); // Font for text fields
        Color textColor = Color.WHITE;

        // Patient Name Field
        JLabel patientNameLabel = new JLabel("Patient Name:");
        patientNameLabel.setForeground(textColor);
        patientNameLabel.setFont(labelFont);
        patientNameField = new JTextField(); // Initialize as class-level variable
        patientNameField.setFont(textFieldFont);

        // Phone Number Field
        // Phone Number Field
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setForeground(textColor);
        phoneNumberLabel.setFont(labelFont);
        phoneNumberField = new JTextField(); // Initialize first
        phoneNumberField.setEditable(false); // Make it non-editable
        phoneNumberField.setFont(textFieldFont); // Set font for text field

        // Medication Field
        JLabel medicationLabel = new JLabel("Symptoms:");
        medicationLabel.setForeground(textColor);
        medicationLabel.setFont(labelFont);
        medicationField = new JTextField(); // Initialize first
        medicationField.setEditable(false); // Make it non-editable
        medicationField.setFont(textFieldFont); // Set font for text field


        // Date Field
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setForeground(textColor);
        dateLabel.setFont(labelFont);
        dateField = new JTextField(); // Initialize as class-level variable
        dateField.setFont(textFieldFont); // Set font for text field

        // Location Field (Dropdown)
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setForeground(textColor);
        locationLabel.setFont(labelFont);
        String[] locations = {"Anna Nagar", "Adyar", "T Nagar", "Velachery", "Koyambedu", "Guindy", "Sholinganallur"};
        locationDropdown = new JComboBox<>(locations); // Initialize as class-level variable
        locationDropdown.setFont(textFieldFont);

        // Time Field
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setForeground(textColor);
        timeLabel.setFont(labelFont);
        timeField = new JTextField(); // Initialize as class-level variable
        timeField.setFont(textFieldFont); // Set font for text field

        // Fetch Button
        JButton fetchButton = new JButton("Fetch");
        fetchButton.setBackground(Color.GREEN);
        fetchButton.setForeground(Color.WHITE);
        fetchButton.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String patientName = patientNameField.getText().trim();
                fetchAppointmentDetails(patientName);
            }
        });

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.BLUE);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAppointment(patientNameField.getText().trim(), dateField.getText().trim(), timeField.getText().trim(), (String) locationDropdown.getSelectedItem());
            }
        });

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current frame
                // Open the homepage frame (assuming you have a method for it)
                new HomePage(); // Replace with your homepage class
            }
        });

        // Add components to panel
        panel.add(patientNameLabel);
        panel.add(patientNameField);
        panel.add(phoneNumberLabel);
        panel.add(phoneNumberField);
        panel.add(medicationLabel);
        panel.add(medicationField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(locationLabel);
        panel.add(locationDropdown);
        panel.add(timeLabel);
        panel.add(timeField);
        panel.add(fetchButton);
        panel.add(submitButton);
        panel.add(backButton); // Add the back button to the panel

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);
    }

    private void fetchAppointmentDetails(String patientName) {
        // Fetch appointment details from MongoDB
        Document appointment = mongoDBConnection.getAppointmentCollection().find(new Document("patientName", patientName)).first();
        if (appointment != null) {
            // Autofill fields with appointment details
            phoneNumberField.setText(appointment.getString("phoneNumber"));
            medicationField.setText(appointment.getString("medication"));
            dateField.setText(appointment.getString("date")); // Fetching date
            locationDropdown.setSelectedItem(appointment.getString("location"));
            timeField.setText(appointment.getString("time"));
        } else {
            JOptionPane.showMessageDialog(null, "No appointment scheduled for this patient.");
            clearFields();
        }
    }

    private void updateAppointment(String patientName, String newDate, String newTime, String newLocation) {
        // Fetch the current appointment details to remove old location document
        Document currentAppointment = mongoDBConnection.getAppointmentCollection()
                .find(new Document("patientName", patientName)).first();

        if (currentAppointment != null) {
            // Attempt to update the appointment
            Document updatedAppointment = new Document("time", newTime)
                    .append("date", newDate) // Include the new date
                    .append("location", newLocation);

            // Update the appointment in the appointments collection
            mongoDBConnection.getAppointmentCollection()
                    .findOneAndUpdate(new Document("patientName", patientName), new Document("$set", updatedAppointment));

            // Remove the old location document from the previous location collection
            String oldLocation = currentAppointment.getString("location");
            mongoDBConnection.getLocationCollection(oldLocation)
                    .deleteOne(new Document("patientName", patientName));

            // Create a new document with updated details for the new location
            Document newLocationDocument = new Document("patientName", patientName)
                    .append("phoneNumber", currentAppointment.getString("phoneNumber"))
                    .append("medication", currentAppointment.getString("medication"))
                    .append("date", newDate) // Updated date
                    .append("dateTime", newTime) // Assuming you want to use 'newTime' as dateTime
                    .append("location", newLocation);

            // Insert the new document into the new location collection
            mongoDBConnection.getLocationCollection(newLocation).insertOne(newLocationDocument);

            JOptionPane.showMessageDialog(null, "Appointment rescheduled successfully.");
            clearFields(); // Clear all fields after submission
        } else {
            JOptionPane.showMessageDialog(null, "No appointment found to reschedule.");
        }
    }

        private void clearFields() {
            patientNameField.setText(""); // Clear patient name
            phoneNumberField.setText(""); // Clear phone number
            medicationField.setText(""); // Clear medication
            dateField.setText(""); // Clear date
            timeField.setText(""); // Clear time
    }
}
