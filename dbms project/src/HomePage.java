import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoClients;  // Ensure this is imported for MongoClients

// Custom JPanel to create a gradient background
class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, Color.BLACK, 0, getHeight(), new Color(0, 0, 139));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

public class HomePage {
    public HomePage() {
        JFrame frame = new JFrame("Medicare");
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create a gradient panel
        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(new BoxLayout(gradientPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical stacking

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false); // Transparent background

        JLabel titleLabel = new JLabel("Medicare");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Increased font size
        titleLabel.setForeground(Color.WHITE); // White color for the title

        // Description panel
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setOpaque(false); // Transparent background
        JTextArea descriptionArea = new JTextArea("Manage patient registrations and appointments efficiently.\n"
                + "Our system provides a simple interface for healthcare professionals to keep track of patients and appointments.\n"
                + "You can easily manage patient records and ensure timely appointments.\n"
                + "Streamline your workflow with our user-friendly application, designed to enhance efficiency and patient care.\n"
                + "Experience a seamless registration and booking process.\n");
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 20)); // Description font size
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false); // Transparent background
        descriptionArea.setBorder(null); // No border
        descriptionArea.setForeground(Color.WHITE); // White color for the text
        descriptionArea.setPreferredSize(new Dimension(600, 120)); // Increased height for more lines
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT); // Center text

        // Add some space between title and description
        descriptionPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Transparent background
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton registerButton = new JButton("Register Patient");
        JButton bookAppointmentButton = new JButton("Book Appointment");
        JButton cancelAppointmentButton = new JButton("Cancel Appointment");
        JButton rescheduleAppointmentButton = new JButton("Reschedule Appointment");

        registerButton.setPreferredSize(new Dimension(200, 50)); // Set button size
        bookAppointmentButton.setPreferredSize(new Dimension(200, 50)); // Set button size
        cancelAppointmentButton.setPreferredSize(new Dimension(230, 50)); // Set button size
        rescheduleAppointmentButton.setPreferredSize(new Dimension(260, 50)); // Set button size
        registerButton.setFont(new Font("Arial", Font.BOLD, 18)); // Button font size
        bookAppointmentButton.setFont(new Font("Arial", Font.BOLD, 18)); // Button font size
        cancelAppointmentButton.setFont(new Font("Arial", Font.BOLD, 18)); // Button font size
        rescheduleAppointmentButton.setFont(new Font("Arial", Font.BOLD, 18)); // Button font size
        registerButton.setForeground(Color.BLACK); // Button text color
        bookAppointmentButton.setForeground(Color.BLACK); // Button text color
        cancelAppointmentButton.setForeground(Color.BLACK); // Button text color
        rescheduleAppointmentButton.setForeground(Color.BLACK); // Button text color
        registerButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        bookAppointmentButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        cancelAppointmentButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        rescheduleAppointmentButton.setBackground(new Color(30, 144, 255));

        // Add action listeners for buttons
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PatientRegistration();
                frame.dispose();
            }
        });

        bookAppointmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AppointmentBooking();
                frame.dispose();
            }
        });

        // Add action listener for the cancel appointment button
        cancelAppointmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CancelAppointment(); // Open CancelAppointment UI
                frame.dispose();
            }
        });

        rescheduleAppointmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RescheduleAppointment(); // Open RescheduleAppointment UI
                frame.dispose();
            }
        });

        // Add components to panels
        titlePanel.add(titleLabel);
        descriptionPanel.add(descriptionArea);

        // Add the GIF after the description
        JPanel gifPanel = new JPanel();
        gifPanel.setOpaque(false);

        ImageIcon gifIcon = new ImageIcon("pulse.gif"); // Replace with the path to your GIF

        // Create a label for the GIF and set its preferred size
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setPreferredSize(new Dimension(5000, 190)); // Resize GIF area to 300x200

        // Add the label to the panel
        gifPanel.add(gifLabel);
        gifPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Add buttons to button panel
        buttonPanel.add(registerButton);
        buttonPanel.add(bookAppointmentButton);
        buttonPanel.add(cancelAppointmentButton);
        buttonPanel.add(rescheduleAppointmentButton); // Add the cancel button to the button panel

        // Centering components in the description panel
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.add(Box.createVerticalGlue());
        descriptionPanel.add(descriptionArea);
        descriptionPanel.add(Box.createVerticalGlue());

        // Logo Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        ImageIcon logoIcon = new ImageIcon(new ImageIcon("logo.png").getImage().getScaledInstance(250, 100, Image.SCALE_SMOOTH)); // Adjust the width and height as needed
        JLabel logoLabel = new JLabel(logoIcon);
        logoPanel.add(logoLabel);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center logo

        // Add panels to gradient panel
        gradientPanel.add(titlePanel);
        gradientPanel.add(descriptionPanel);
        gradientPanel.add(gifPanel); // Add the GIF panel here
        gradientPanel.add(buttonPanel);
        gradientPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons and logo
        gradientPanel.add(logoPanel);

        // Add gradient panel to frame
        frame.add(gradientPanel);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePage::new);
    }
}
