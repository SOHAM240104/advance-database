import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.bson.Document;
import com.mongodb.client.MongoCollection;


public class AppointmentBooking {
    private MongoDBConnection mongoDBConnection;

    public AppointmentBooking() {
        mongoDBConnection = new MongoDBConnection();

        // Create main frame
        JFrame frame = new JFrame("Book Appointment");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setLayout(new BorderLayout());

        // Set background gradient panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, Color.BLACK, 0, getHeight(), new Color(0, 0, 139)); // Dark Blue
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridLayout(8, 2, 10, 10)); // 8 rows, 2 columns

        // Patient Name Field
        JLabel patientNameLabel = new JLabel("Patient Name:");
        patientNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        patientNameLabel.setForeground(Color.WHITE);
        JTextField patientNameField = new JTextField();
        patientNameField.setPreferredSize(new Dimension(200, 25));
        patientNameField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Phone Number Field
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        phoneNumberLabel.setForeground(Color.WHITE);
        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setPreferredSize(new Dimension(200, 25));
        phoneNumberField.setFont(new Font("Arial", Font.PLAIN, 16));
        

        // Medication Field
        JLabel medicationLabel = new JLabel("Symptoms:");
        medicationLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        medicationLabel.setForeground(Color.WHITE);
        JTextField medicationField = new JTextField();
        medicationField.setPreferredSize(new Dimension(200, 25));
        medicationField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Date Field
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dateLabel.setForeground(Color.WHITE);
        JTextField dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(200, 25));
        dateField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Time Field
        JLabel timeLabel = new JLabel("Time (HH:mm AM/PM):");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        timeLabel.setForeground(Color.WHITE);
        JTextField timeField = new JTextField();
        timeField.setPreferredSize(new Dimension(200, 25));
        timeField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Location Combo Box
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        locationLabel.setForeground(Color.WHITE);
        String[] locations = {"Guindy", "Anna Nagar", "Sholinganallur", "Adyar", "T Nagar", "Koyambedu", "Velachery"};
        JComboBox<String> locationCombo = new JComboBox<>(locations);

        // Submit Button
        JButton submitButton = new JButton("Book Appointment");
        submitButton.setBackground(new Color(50, 205, 50)); // Lime Green
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));

        // ActionListener for submit button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String patientName = patientNameField.getText().trim();
                String phoneNumber = phoneNumberField.getText().trim();
                String medication = medicationField.getText().trim();
                String date = dateField.getText().trim();
                String time = timeField.getText().trim();
                String location = (String) locationCombo.getSelectedItem();

                // Validate date and time format
                if (!isValidDate(date)) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid date in YYYY-MM-DD format.");
                    return;
                }
                if (!isValidTime(time)) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid time in HH:mm format.");
                    return;
                }

                // Check if the patient is registered
                if (!isPatientRegistered(patientName)) {
                    JOptionPane.showMessageDialog(frame, "Patient not registered. Please register first.");
                    return;
                }

                // Create a document to store in MongoDB
                Document appointmentDocument = new Document("patientName", patientName)
                        .append("phoneNumber", phoneNumber) // Store phone number
                        .append("medication", medication) // Store medication
                        .append("date", date)
                        .append("time", time)
                        .append("location", location);

                // Insert the document into the MongoDB collection
                try {
                    mongoDBConnection.getAppointmentCollection().insertOne(appointmentDocument);
                    mongoDBConnection.getLocationCollection(location).insertOne(appointmentDocument); // Save to location-specific collection
                    JOptionPane.showMessageDialog(frame, "Appointment booked successfully!");

                    // Reset input fields after booking
                    resetFields(patientNameField, phoneNumberField, medicationField, dateField, timeField, locationCombo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving appointment data: " + ex.getMessage());
                }
            }
        });

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 69, 0)); // Red-Orange
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HomePage();
                frame.dispose(); // Close the appointment booking page
            }
        });

        // Adding components to the panel
        panel.add(patientNameLabel);
        panel.add(patientNameField);
        panel.add(phoneNumberLabel);
        panel.add(phoneNumberField);
        panel.add(medicationLabel);
        panel.add(medicationField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(timeLabel);
        panel.add(timeField);
        panel.add(locationLabel);
        panel.add(locationCombo);
        panel.add(submitButton);
        panel.add(backButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void resetFields(JTextField patientNameField, JTextField phoneNumberField, JTextField medicationField, JTextField dateField, JTextField timeField, JComboBox<String> locationCombo) {
        patientNameField.setText("");
        phoneNumberField.setText("");
        medicationField.setText("");
        dateField.setText("");
        timeField.setText("");
        locationCombo.setSelectedIndex(0); // Reset to the first location
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true; // Valid date
        } catch (ParseException e) {
            return false; // Invalid date
        }
    }

    private boolean isValidTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        try {
            sdf.parse(time);
            return true; // Valid time
        } catch (ParseException e) {
            return false; // Invalid time
        }
    }

    private boolean isPatientRegistered(String patientName) {
        // Check if the patient is registered in MongoDB
        MongoCollection<Document> patientCollection = mongoDBConnection.getPatientCollection();
        Document foundPatient = patientCollection.find(new Document("name", patientName)).first();
        return foundPatient != null; // Return true if patient is found
    }
}
