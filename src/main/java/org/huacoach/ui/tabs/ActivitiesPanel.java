package org.huacoach.ui.tabs;

import org.huacoach.model.UserProfile;
import org.huacoach.model.XMLActivity;
import org.huacoach.services.CaloriesCalculator;
import org.huacoach.services.StatisticsCalculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ActivitiesPanel extends JPanel {

    private final JPanel content = new JPanel(new BorderLayout(12, 12));
    private final JPanel cardsGrid = new JPanel(new GridLayout(1, 5, 12, 12)); // 5 στήλες (μαζί με θερμίδες)

    // Στοιχεία Πλοήγησης
    private final JButton prevBtn = new JButton("< Prev");
    private final JButton nextBtn = new JButton("Next >");
    private final JLabel counterLabel = new JLabel("0 / 0");

    // Δεδομένα
    private List<XMLActivity> currentActivities;
    private UserProfile currentProfile;
    private int currentIndex = 0;

    public ActivitiesPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));

        // Τίτλος
        JLabel title = new JLabel("Activity Report");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Grid Καρτών
        content.add(title, BorderLayout.NORTH);
        content.add(cardsGrid, BorderLayout.CENTER);

        // Navigation Panel (Κάτω μέρος)
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.add(prevBtn);
        navPanel.add(counterLabel);
        navPanel.add(nextBtn);

        content.add(navPanel, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        // Listeners κουμπιών
        prevBtn.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateView();
            }
        });

        nextBtn.addActionListener(e -> {
            if (currentActivities != null && currentIndex < currentActivities.size() - 1) {
                currentIndex++;
                updateView();
            }
        });
    }

    /**
     * Καλείται από τον TabController όταν πατηθεί το "Show Stats".
     */
    public void showReport(List<XMLActivity> activities, UserProfile profile) {
        this.currentActivities = activities;
        this.currentProfile = profile;

        if (activities == null || activities.isEmpty()) {
            currentIndex = -1;
            updateView();
            return;
        }

        // Πηγαίνουμε αυτόματα στην τελευταία δραστηριότητα (η πιο πρόσφατη ή αυτή που μόλις προστέθηκε)
        currentIndex = activities.size() - 1;
        updateView();
    }

    /**
     * Ενημερώνει την οθόνη με βάση το currentIndex
     */
    private void updateView() {
        cardsGrid.removeAll();

        // 1. Περίπτωση Κενής Λίστας
        if (currentActivities == null || currentActivities.isEmpty()) {
            counterLabel.setText("0 / 0");
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            cardsGrid.add(statCard("INFO", "No Data", ""));
            revalidate();
            repaint();
            return;
        }

        // 2. Ενημέρωση Κουμπιών & Label
        counterLabel.setText((currentIndex + 1) + " of " + currentActivities.size());
        prevBtn.setEnabled(currentIndex > 0);
        nextBtn.setEnabled(currentIndex < currentActivities.size() - 1);

        // 3. Εμφάνιση Στατιστικών Τρέχουσας Δραστηριότητας
        XMLActivity a = currentActivities.get(currentIndex);

        // Προσθήκη Τύπου Αθλήματος στον τίτλο (προαιρετικά)
        // extractSport(a) -> Θα μπορούσε να μπει σε κάρτα ή τίτλο

        cardsGrid.add(statCard("SPORT", a.getSportType().toString(), ""));
        cardsGrid.add(statCard("DURATION", extractDuration(a), ""));
        cardsGrid.add(statCard("DISTANCE", extractDistance(a), "km"));
        cardsGrid.add(statCard("AVG HEART RATE", extractAvgHr(a), "bpm"));
        cardsGrid.add(statCard("CALORIES", extractCalories(a, currentProfile), "kcal"));

        revalidate();
        repaint();
    }

    // --- Helpers / Getters ---

    private JPanel statCard(String title, String value, String unit) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.BOLD, 11));
        t.setForeground(Color.GRAY);

        JLabel v = new JLabel(unit.isEmpty() ? value : value + " " + unit);
        v.setFont(new Font("Arial", Font.BOLD, 18));

        p.add(t);
        p.add(Box.createVerticalStrut(5));
        p.add(v);
        return p;
    }

    private String extractDuration(XMLActivity a) {
        long sec = StatisticsCalculator.getTotalTimeSeconds(a.getLaps());
        long h = sec / 3600;
        long m = (sec % 3600) / 60;
        long s = sec % 60;
        return String.format("%d:%02d:%02d", h, m, s);
    }

    private String extractDistance(XMLActivity a) {
        double m = StatisticsCalculator.getTotalDistanceMeters(a.getLaps());
        return String.format("%.2f", m / 1000.0);
    }

    private String extractAvgHr(XMLActivity a) {
        int hr = StatisticsCalculator.getAverageHeartRate(a.getLaps());
        return (hr > 0) ? String.valueOf(hr) : "--";
    }

    private String extractCalories(XMLActivity a, UserProfile profile) {
        if (profile.getWeight() <= 0) return "--";

        long durationSec = StatisticsCalculator.getTotalTimeSeconds(a.getLaps());
        int avgHr = StatisticsCalculator.getAverageHeartRate(a.getLaps());
        double cals;

        if ("Advanced".equals(profile.getCalcMethod()) && avgHr > 0) {
            cals = CaloriesCalculator.calculateCaloriesAdvanced(
                    profile.getWeight(), durationSec, profile.getAge(), profile.getSex(), avgHr);
        } else {
            cals = CaloriesCalculator.calculateCaloriesSimple(
                    profile.getWeight(), durationSec, a.getSportType());
        }
        return String.format("%.0f", cals);
    }
}