import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.bson.Document;

public class CancelAppointment {
    private MongoDBConnection mongoDBConnection;
    private JTextField patientNameField; // Declare as instance variable
    private JTextField phoneNumberField; // Declare as instance variable
    private JTextField medicationField; // Declare as instance variable
    private JTextField locationField; // Declare as instance variable

    public CancelAppointment() {
        mongoDBConnection = new MongoDBConnection();

        // Create cancel appointment frame
        JFrame frame = new JFrame("Cancel Appointment");
        frame.setSize(700, 700); // Increased size
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
        panel.setLayout(new GridLayout(7, 2, 10, 10)); // Increased row count for Back button
        
        // Set font and color for labels
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 18); // Font for text fields
        Color textColor = Color.WHITE;

        // Patient Name Field
        JLabel patientNameLabel = new JLabel("Patient Name:");
        patientNameLabel.setForeground(textColor);
        patientNameLabel.setFont(labelFont);
        patientNameField = new JTextField(); // Initialize here
        patientNameField.setFont(textFieldFont); // Set font for text field     

        // Phone Number Field
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setForeground(textColor);
        phoneNumberLabel.setFont(labelFont);
        phoneNumberField = new JTextField(); // Initialize here
        phoneNumberField.setFont(textFieldFont); // Font set to size 18
        phoneNumberField.setEditable(false); // Make it non-editable for now

        // Medication Field
        JLabel medicationLabel = new JLabel("Symptoms:");
        medicationLabel.setForeground(textColor);
        medicationLabel.setFont(labelFont);
        medicationField = new JTextField(); // Initialize here
        medicationField.setFont(textFieldFont); // Font set to size 18
        medicationField.setEditable(false); // Make it non-editable for now

        // Location Field
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setForeground(textColor);
        locationLabel.setFont(labelFont);
        locationField = new JTextField(); // Initialize here
        locationField.setFont(textFieldFont); // Font set to size 18
        locationField.setEditable(false); // Make it non-editable for now

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

        // Cancel Button
        JButton cancelButton = new JButton("Cancel Appointment");
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String patientName = patientNameField.getText().trim();
                cancelAppointment(patientName);
                clearFields(); // Call clearFields after cancelling
            }
        });

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current frame
                new HomePage(); // Create and show the homepage (ensure you have this class)
            }
        });

        // Add components to panel
        panel.add(patientNameLabel);
        panel.add(patientNameField);
        panel.add(phoneNumberLabel);
        panel.add(phoneNumberField);
        panel.add(medicationLabel);
        panel.add(medicationField);
        panel.add(locationLabel);
        panel.add(locationField);
        panel.add(fetchButton);
        panel.add(cancelButton);
        panel.add(backButton); // Add back button to panel

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
            locationField.setText(appointment.getString("location"));
        } else {
            JOptionPane.showMessageDialog(null, "No appointment scheduled for this patient.");
            clearFields();
        }
    }

    private void cancelAppointment(String patientName) {
        // Attempt to delete the appointment
        Document appointment = mongoDBConnection.getAppointmentCollection().findOneAndDelete(new Document("patientName", patientName));
        if (appointment != null) {
            // Delete from location collection as well
            mongoDBConnection.getLocationCollection(appointment.getString("location")).findOneAndDelete(new Document("patientName", patientName));
            JOptionPane.showMessageDialog(null, "Appointment cancelled successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "No appointment found to cancel.");
        }
    }

    private void clearFields() {
        patientNameField.setText(""); // Clear patient name
        phoneNumberField.setText(""); // Clear phone number
        medicationField.setText(""); // Clear medication
        locationField.setText(""); // Clear location
    }
}
