package org.huacoach.ui.tabs;

import org.huacoach.model.XMLActivity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ActivitiesPanel extends JPanel {

    private final JPanel content = new JPanel(new BorderLayout(12, 12));
    private final JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 12, 12));

    public ActivitiesPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));

        // Προαιρετικά: τίτλος πάνω
        JLabel title = new JLabel("Activity Report");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

        content.add(title, BorderLayout.NORTH);
        content.add(cardsGrid, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    public void showReport(List<XMLActivity> activities, double weight) {
        cardsGrid.removeAll();

        if (activities == null || activities.isEmpty()) {
            cardsGrid.add(statCard("INFO", "Δεν υπάρχουν δραστηριότητες", null));
            cardsGrid.add(emptyCard());
            cardsGrid.add(emptyCard());
            cardsGrid.add(emptyCard());
            revalidate();
            repaint();
            return;
        }

        XMLActivity a = activities.get(activities.size() - 1);

        cardsGrid.add(statCard("DURATION", extractDuration(a), null));
        cardsGrid.add(statCard("DISTANCE", extractDistance(a), "km"));
        cardsGrid.add(statCard("AVG SPEED", extractAvgSpeed(a), "km/h"));
        cardsGrid.add(statCard("AVG HEART RATE", extractAvgHr(a), "bpm"));

        revalidate();
        repaint();
    }

    private JPanel emptyCard() {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return p;
    }

    private JPanel statCard(String title, String value, String unit) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(160, 110));

        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 12f));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel v = new JLabel(unit == null ? value : value + " " + unit);
        v.setFont(v.getFont().deriveFont(Font.BOLD, 22f));
        v.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(t);
        p.add(Box.createVerticalStrut(8));
        p.add(v);

        return p;
    }
    // ---------------------------
    // ΒΑΛΕ ΕΔΩ ΤΑ ΔΙΚΑ ΣΟΥ GETTERS
    // ---------------------------
    private String extractDuration(XMLActivity a) {
        //return a.getDurationFormatted();
        return "5:00";
    }

    private String extractDistance(XMLActivity a) {
        // πχ: return String.format("%.2f", a.getDistanceKm());
        return "0.25";
    }

    private String extractAvgSpeed(XMLActivity a) {
        // πχ: return String.format("%.2f", a.getAvgSpeedKmh());
        return "3.00";
    }

    private String extractAvgHr(XMLActivity a) {
        // πχ: return String.valueOf(a.getAvgHeartRate());
        return "117";
    }
}
