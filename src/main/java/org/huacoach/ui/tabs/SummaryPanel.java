package org.huacoach.ui.tabs;

import org.huacoach.model.UserProfile;
import org.huacoach.model.XMLActivity;
import org.huacoach.services.CaloriesCalculator;
import org.huacoach.services.StatisticsCalculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SummaryPanel extends JPanel {

    private final JPanel content = new JPanel(new BorderLayout(15, 15));
    private final JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 12, 12));

    // Πίνακας για την ανάλυση ανά ημέρα
    private JTable dailyTable;
    private DefaultTableModel tableModel;

    public SummaryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));

        // --- Τίτλος ---
        JLabel title = new JLabel("Συγκεντρωτική Αναφορά & Ημερήσιοι Στόχοι");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // --- Grid με Κάρτες (Πάνω μέρος) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        topPanel.add(cardsGrid, BorderLayout.CENTER);

        content.add(topPanel, BorderLayout.NORTH);

        // --- Πίνακας Ημερών (Κάτω μέρος) ---
        createTableSection();

        add(content, BorderLayout.CENTER);
    }

    private void createTableSection() {
        String[] columns = {"Ημερομηνία", "Θερμίδες (Burned)", "Στόχος (Goal)", "Κατάσταση (Status)"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        dailyTable = new JTable(tableModel);
        dailyTable.setRowHeight(25);
        dailyTable.setFont(new Font("Arial", Font.PLAIN, 14));
        dailyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < dailyTable.getColumnCount(); i++) {
            dailyTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(dailyTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Ανάλυση ανά Ημέρα"));

        content.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Καλείται από τον TabController όταν αλλάζουμε tab.
     */
    public void updateSummary(List<XMLActivity> activities, UserProfile profile) {
        cardsGrid.removeAll();
        tableModel.setRowCount(0);

        if (activities == null || activities.isEmpty()) {
            cardsGrid.add(createCard("Info", "No Data", ""));
            revalidate();
            repaint();
            return;
        }

        // 1. Υπολογισμός Γενικών Συνόλων
        double totalDistance = 0.0;
        long totalSeconds = 0;

        // Map για ομαδοποίηση ανά ημέρα (Ταξινομημένο φθινουσες ημερομηνίες)
        Map<LocalDate, Double> dailyCaloriesMap = new TreeMap<>(Collections.reverseOrder());

        for (XMLActivity activity : activities) {
            // Γενικά Σύνολα
            totalDistance += StatisticsCalculator.getTotalDistanceMeters(activity.getLaps());
            totalSeconds += StatisticsCalculator.getTotalTimeSeconds(activity.getLaps());

            // --- ΔΙΟΡΘΩΣΗ: Υπολογισμός Θερμίδων με βάση την επιλογή χρήστη ---
            double actCals = calculateCaloriesForActivity(activity, profile);

            // Ομαδοποίηση ανά Ημέρα
            LocalDate date = activity.getStartTime().toLocalDate();
            dailyCaloriesMap.put(date, dailyCaloriesMap.getOrDefault(date, 0.0) + actCals);
        }

        // 2. Ενημέρωση Καρτών (Grand Totals)
        cardsGrid.add(createCard("Total Activities", String.valueOf(activities.size()), ""));
        cardsGrid.add(createCard("Total Distance", String.format("%.2f", totalDistance / 1000.0), "km"));
        cardsGrid.add(createCard("Total Time", formatDuration(totalSeconds), ""));

        // Συνολικές θερμίδες (άθροισμα όλων των ημερών)
        double grandTotalCals = dailyCaloriesMap.values().stream().mapToDouble(Double::doubleValue).sum();
        cardsGrid.add(createCard("Total Calories", String.format("%.0f", grandTotalCals), "kcal"));


        // 3. Ενημέρωση Πίνακα (Daily Breakdown)
        int dailyGoal = profile.getCalorieGoal(); // Διορθωμένο: καλείται από το αντικείμενο profile
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Map.Entry<LocalDate, Double> entry : dailyCaloriesMap.entrySet()) {
            LocalDate date = entry.getKey();
            double burned = entry.getValue();

            String status;
            if (burned >= dailyGoal) {
                status = "✅ Επετεύχθη!";
            } else {
                double remaining = dailyGoal - burned;
                status = "❌ Υπόλοιπο: " + String.format("%.0f", remaining);
            }

            Object[] row = {
                    date.format(formatter),
                    String.format("%.0f", burned),
                    dailyGoal,
                    status
            };
            tableModel.addRow(row);
        }

        revalidate();
        repaint();
    }

    // --- ΝΕΑ ΜΕΘΟΔΟΣ: Έξυπνος υπολογισμός ανά δραστηριότητα ---
    private double calculateCaloriesForActivity(XMLActivity a, UserProfile profile) {
        if (profile.getWeight() <= 0) return 0.0;

        long durationSec = StatisticsCalculator.getTotalTimeSeconds(a.getLaps());

        // Παίρνουμε τους παλμούς της συγκεκριμένης δραστηριότητας
        int avgHr = StatisticsCalculator.getAverageHeartRate(a.getLaps());

        // Λογική: Advanced αν το θέλει ο χρήστης ΚΑΙ έχουμε παλμούς. Αλλιώς Simple.
        if ("Advanced".equals(profile.getCalcMethod()) && avgHr > 0) {
            return CaloriesCalculator.calculateCaloriesAdvanced(
                    profile.getWeight(),
                    durationSec,
                    profile.getAge(),
                    profile.getSex(),
                    avgHr
            );
        } else {
            return CaloriesCalculator.calculateCaloriesSimple(
                    profile.getWeight(),
                    durationSec,
                    a.getSportType()
            );
        }
    }

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

        JLabel v = new JLabel(unit.isEmpty() ? value : value + " " + unit);
        v.setFont(new Font("Arial", Font.BOLD, 20));
        v.setForeground(new Color(50, 50, 50));

        p.add(t);
        p.add(Box.createVerticalStrut(10));
        p.add(v);
        return p;
    }

    private String formatDuration(long totalSec) {
        long h = totalSec / 3600;
        long m = (totalSec % 3600) / 60;
        return String.format("%dh %02dm", h, m);
    }
}