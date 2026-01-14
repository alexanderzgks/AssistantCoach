package org.huacoach.ui.tabs;

import org.huacoach.data.ActivityRepository;
import org.huacoach.data.UserProfileRepository;
import org.huacoach.model.XMLActivity;
import org.huacoach.model.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TabController {

    private final JFrame frame;
    private JTabbedPane tabbedPane;
    private final UserProfileRepository userProfileRepository;

    // Repositories για άντληση δεδομένων
    private final ActivityRepository activityRepository = new ActivityRepository();


    // Panels
    private ActivitiesPanel activitiesPanel;
    private SummaryPanel summaryPanel; // <--- ΝΕΟ

    public TabController(JFrame frame , UserProfileRepository repo){
        this.frame = frame;
        this.frame.add(createTabs(), BorderLayout.CENTER);
        this.userProfileRepository = repo;
    }

    private JTabbedPane createTabs(){
        tabbedPane = new JTabbedPane();

        // 1. Activities Tab
        tabbedPane.addTab("Activities", createActivitiesPanel());

        // 2. Summary Tab (ΝΕΟ)
        summaryPanel = new SummaryPanel();
        tabbedPane.addTab("Summary", summaryPanel);

        // --- LISTENER ΓΙΑ ΑΥΤΟΜΑΤΗ ΕΝΗΜΕΡΩΣΗ ---
        // Κάθε φορά που αλλάζει το tab, αν επιλεγεί το Summary (index 1), ενημερώνουμε τα δεδομένα
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) { // 1 = Summary Tab
                refreshSummary();
            }
        });

        return tabbedPane;
    }

    private void refreshSummary() {
        // Παίρνουμε τις δραστηριότητες και το προφίλ
        List<XMLActivity> activities = activityRepository.getTheActivities();
        UserProfile profile = userProfileRepository.getProfile();

        // Ενημερώνουμε το UI
        summaryPanel.updateSummary(activities, profile);
    }

    private JPanel createActivitiesPanel() {
        // ... (Ο κώδικας που έχεις ήδη για το createActivitiesPanel μένει ίδιος) ...
        // ΣΗΜΕΙΩΣΗ: Μην ξεχάσεις στο κουμπί Show Stats να πάρεις το σωστό βάρος:
        /*
           double weight = userProfileRepository.getProfile().getWeight();
           activitiesPanel.showReport(activities, weight);
        */

        // ... αντιγραφή του υπάρχοντος κώδικα σου ...
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        activitiesPanel = new ActivitiesPanel(); // Αρχικοποίηση πεδίου

        JPanel startPanel = new JPanel(new GridBagLayout());
        JButton showStatsButton = new JButton("Show Stats");
        startPanel.add(showStatsButton);

        JPanel statsPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Επιστροφή");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);

        statsPanel.add(topPanel, BorderLayout.NORTH);
        statsPanel.add(activitiesPanel, BorderLayout.CENTER);

        showStatsButton.addActionListener(e -> {
            List<XMLActivity> activities = activityRepository.getTheActivities();
            if (activities == null || activities.isEmpty()) {
                JOptionPane.showMessageDialog(container, "Δεν υπάρχουν αρχεία.");
                return;
            }
            // ΔΙΟΡΘΩΣΗ: Παίρνουμε το βάρος από το Profile
            double w = userProfileRepository.getProfile().getWeight();
            activitiesPanel.showReport(activities, userProfileRepository.getProfile());

            cardLayout.show(container, "STATS");
        });

        backButton.addActionListener(e -> cardLayout.show(container, "START"));

        container.add(startPanel, "START");
        container.add(statsPanel, "STATS");
        cardLayout.show(container, "START");

        return container;
    }

    // Η παλιά μέθοδος createSummaryPanel διαγράφεται γιατί αντικαταστάθηκε από την κλάση SummaryPanel
}