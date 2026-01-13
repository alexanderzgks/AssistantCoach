package org.huacoach.ui.tabs;

import org.huacoach.data.ActivityRepository;
import org.huacoach.model.XMLActivity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TabController {

    private final JFrame frame;
    private JTabbedPane tabbedPane;

    public TabController(JFrame frame){
        this.frame = frame;
        this.frame.add(createTabs(), BorderLayout.CENTER);
    }

    private JTabbedPane createTabs(){
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Activities", createActivitiesPanel());
        tabbedPane.addTab("Summary", createSummaryPanel());
        return  tabbedPane;
    }


    private JPanel createActivitiesPanel() {

        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        ActivityRepository activityRepository = new ActivityRepository();
        ActivitiesPanel activitiesPanel = new ActivitiesPanel();

        // CARD 1 – START
        JPanel startPanel = new JPanel(new GridBagLayout());
        JButton showStatsButton = new JButton("Show Stats");
        startPanel.add(showStatsButton);

        // CARD 2 – STATS
        JPanel statsPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("Επιστροφή");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);

        statsPanel.add(topPanel, BorderLayout.NORTH);
        statsPanel.add(activitiesPanel, BorderLayout.CENTER);

        // ACTIONS
        showStatsButton.addActionListener(e -> {
            List<XMLActivity> activities = activityRepository.getTheActivities();

            if (activities == null || activities.isEmpty()) {
                JOptionPane.showMessageDialog(
                        container,
                        "Δεν υπάρχουν αρχεία δραστηριοτήτων.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            activitiesPanel.showReport(activities, 0.0);

            cardLayout.show(container, "STATS");
            container.revalidate();
            container.repaint();
        });

        backButton.addActionListener(e -> {
            cardLayout.show(container, "START");
            container.revalidate();
            container.repaint();
        });

        // ADD CARDS
        container.add(startPanel, "START");
        container.add(statsPanel, "STATS");

        // αρχική οθόνη
        cardLayout.show(container, "START");

        return container;
    }




    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel();
        // εδώ βάζεις συνολικά στατιστικά
        return panel;
    }

}
