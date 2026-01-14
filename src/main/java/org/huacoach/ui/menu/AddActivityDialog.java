package org.huacoach.ui.menu;

import org.huacoach.data.ActivityRepository;
import org.huacoach.enums.SportType;
import org.huacoach.model.Lap;
import org.huacoach.model.Track;
import org.huacoach.model.Trackpoint;
import org.huacoach.model.XMLActivity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


public class AddActivityDialog extends JDialog {

    private final ActivityRepository activityRepository;
    private JComboBox<SportType> sportCombo;
    private JTextField durationField; // Λεπτά
    private JTextField distanceField; // Χιλιόμετρα
    private JTextField dateField;     // Ημερομηνία (String για απλότητα)
    private JTextField hrField;

    public AddActivityDialog(JFrame parent, ActivityRepository repository) {
        super(parent, "Προσθήκη Δραστηριότητας", true);
        this.activityRepository = repository;

        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(new JScrollPane(createForm()), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private JPanel createForm() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 8));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));


        panel.add(new JLabel("Άθλημα:"));
        sportCombo = new JComboBox<>(SportType.values());
        panel.add(sportCombo);

        panel.add(new JLabel("Διάρκεια (λεπτά):"));
        durationField = new JTextField();
        panel.add(durationField);

        // 4. Heart Rate
        panel.add(new JLabel("Avg Heart Rate (bpm):"));
        hrField = new JTextField();
        // Προσθέτουμε ένα tooltip για να ξέρει ο χρήστης ότι είναι προαιρετικό
        hrField.setToolTipText("Προαιρετικό. Αφήστε κενό αν δεν γνωρίζετε.");
        panel.add(hrField);

        panel.add(new JLabel("Απόσταση (km):"));
        distanceField = new JTextField();
        panel.add(distanceField);

        panel.add(new JLabel("Ημερομηνία (YYYY-MM-DD):"));
        // Προεπιλογή τη σημερινή ημερομηνία
        String today = OffsetDateTime.now().toLocalDate().toString();
        dateField = new JTextField(today);
        panel.add(dateField);

        return panel;
    }

    // Βοηθητική για ομοιόμορφα Labels
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Λίγο αέρα στα κουμπιά

        JButton save = new JButton("Αποθήκευση");
        JButton cancel = new JButton("Άκυρο");

        // Στυλ κουμπιών (Προαιρετικό - για ομορφιά)
        save.setPreferredSize(new Dimension(110, 30));
        cancel.setPreferredSize(new Dimension(80, 30));

        save.addActionListener(e -> onSave());
        cancel.addActionListener(e -> dispose());

        panel.add(save);
        panel.add(cancel);
        return panel;
    }

    private void onSave() {
        try {
            // 1. Λήψη δεδομένων
            SportType sport = (SportType) sportCombo.getSelectedItem();
            double durationMins = Double.parseDouble(durationField.getText());
            double distanceKm = Double.parseDouble(distanceField.getText());
            String dateStr = dateField.getText(); // Αναμένουμε YYYY-MM-DD

            int heartRate = 0;
            String hrText = hrField.getText().trim();
            if (!hrText.isEmpty()) {
                heartRate = Integer.parseInt(hrText);
            }

            // 2. Μετατροπή σε μονάδες συστήματος (Meters, Seconds)
            double distanceMeters = distanceKm * 1000.0;
            long durationSeconds = (long) (durationMins * 60);

            // 3. Δημιουργία Χρόνου
            // Προσθέτουμε "T10:00:00Z" για να γίνει έγκυρο OffsetDateTime
            OffsetDateTime startTime = OffsetDateTime.parse(dateStr + "T10:00:00Z");
            OffsetDateTime endTime = startTime.plusSeconds(durationSeconds);

            // 4. Δημιουργία XMLActivity "Manual"
            // Φτιάχνουμε ένα Activity που περιέχει 1 Lap -> 1 Track -> 2 Trackpoints
            XMLActivity newActivity = new XMLActivity(sport, startTime, "manual-" + System.currentTimeMillis());

            Lap lap = new Lap(startTime);
            Track track = new Track();

            // Trackpoint Αρχής (0 μέτρα, ώρα έναρξης)
            Trackpoint startTp = new Trackpoint(startTime, 0.0, 0.0, 0.0, 0.0, heartRate, 0);

            // Trackpoint Τέλους (Max μέτρα, ώρα λήξης)
            Trackpoint endTp = new Trackpoint(endTime, 0.0, 0.0, 0.0, distanceMeters, heartRate, 0);

            track.addTrackpoint(startTp);
            track.addTrackpoint(endTp);
            lap.addTracks(track);
            newActivity.addLaps(lap);

            // 5. Αποθήκευση στο Repository
            activityRepository.setActivities(newActivity);

            JOptionPane.showMessageDialog(this, "Η δραστηριότητα προστέθηκε επιτυχώς!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Λάθος στα δεδομένα. Ελέγξτε τους αριθμούς και τη μορφή ημερομηνίας (YYYY-MM-DD).\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}