package org.huacoach.ui.tabs;

import org.huacoach.model.XMLActivity;
import org.huacoach.model.UserProfile;
import org.huacoach.services.CaloriesCalculator;
import org.huacoach.services.StatisticsCalculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class SummaryPanel extends JPanel {

    private final JPanel content = new JPanel(new BorderLayout(12, 12));
    private final JPanel cardsGrid = new JPanel(new GridLayout(2, 2, 12, 12)); // 2x2 Grid

    public SummaryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Συνολικά Στατιστικά (Όλες οι δραστηριότητες)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        content.add(title, BorderLayout.NORTH);
        content.add(cardsGrid, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    /**
     * Ενημερώνει το panel με τα νέα δεδομένα.
     * @param activities Η λίστα με όλες τις δραστηριότητες
     * @param profile Το προφίλ του χρήστη (για υπολογισμό θερμίδων)
     */
    public void updateSummary(List<XMLActivity> activities, UserProfile profile) {
        cardsGrid.removeAll();

        if (activities == null || activities.isEmpty()) {
            cardsGrid.add(createCard("Info", "Δεν υπάρχουν δεδομένα", ""));
            revalidate();
            repaint();
            return;
        }

        // --- Υπολογισμός Αθροισμάτων ---
        double totalDistance = 0.0;
        long totalSeconds = 0;
        double totalCalories = 0.0;

        for (XMLActivity activity : activities) {
            // Χρόνος και Απόσταση για κάθε Activity
            long duration = StatisticsCalculator.getTotalTimeSeconds(activity.getLaps());
            double dist = StatisticsCalculator.getTotalDistanceMeters(activity.getLaps());

            // Θερμίδες (χρησιμοποιούμε τον απλό τύπο για τώρα)
            double cals = 0;
            if (profile.getWeight() > 0) {
                cals = CaloriesCalculator.calculateCaloriesSimple(
                        profile.getWeight(),
                        duration,
                        activity.getSportType()
                );
            }

            totalDistance += dist;
            totalSeconds += duration;
            totalCalories += cals;
        }

        // --- Εμφάνιση καρτών ---

        // 1. Πλήθος Δραστηριοτήτων
        cardsGrid.add(createCard("Total Activities", String.valueOf(activities.size()), ""));

        // 2. Συνολική Απόσταση (σε km)
        cardsGrid.add(createCard("Total Distance", String.format("%.2f", totalDistance / 1000.0), "km"));

        // 3. Συνολικός Χρόνος
        cardsGrid.add(createCard("Total Time", formatDuration(totalSeconds), ""));

        // 4. Συνολικές Θερμίδες
        String calStr = (profile.getWeight() > 0) ? String.format("%.0f", totalCalories) : "--";
        cardsGrid.add(createCard("Total Calories", calStr, "kcal"));

        revalidate();
        repaint();
    }

    // Βοηθητική για τη δημιουργία καρτών (Design)
    private JPanel createCard(String title, String value, String unit) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        p.setBackground(Color.WHITE);

        JLabel t = new JLabel(title);
        t.setForeground(Color.GRAY);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel v = new JLabel(unit.isEmpty() ? value : value + " " + unit);
        v.setFont(new Font("Arial", Font.BOLD, 24));
        v.setForeground(new Color(50, 50, 50));
        v.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(t);
        p.add(Box.createVerticalStrut(10));
        p.add(v);

        return p;
    }

    private String formatDuration(long totalSec) {
        long hours = totalSec / 3600;
        long minutes = (totalSec % 3600) / 60;
        return String.format("%dh %02dm", hours, minutes);
    }
}