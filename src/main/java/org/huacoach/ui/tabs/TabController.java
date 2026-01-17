package org.huacoach.ui.tabs;

import org.huacoach.data.ActivityRepository;
import org.huacoach.data.UserProfileRepository;
import org.huacoach.model.XMLActivity;
import org.huacoach.model.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * TabController
 * --------------------------------
 * Υπεύθυνο για:
 * 1) Δημιουργία των tabs (Activities / Summary)
 * 2) Σύνδεση UI panels με repositories (φόρτωση δεδομένων)
 * 3) Refresh του Summary όταν:
 *    - ο χρήστης μπαίνει στο Summary tab
 *    - ο χρήστης πατάει το κουμπί Reload στο SummaryPanel
 */
public class TabController {

    // Κύριο παράθυρο (MainFrame) όπου μπαίνουν τα tabs
    private final JFrame frame;

    // Το component που περιέχει τα tabs
    private JTabbedPane tabbedPane;

    // Repository προφίλ (βάρος, ηλικία, φύλο, στόχος κ.λπ.)
    private final UserProfileRepository userProfileRepository;

    // Repository δραστηριοτήτων (φόρτωση / ανάκτηση XMLActivity)
    private final ActivityRepository activityRepository = new ActivityRepository();


    // Panels
    private ActivitiesPanel activitiesPanel;
    private SummaryPanel summaryPanel; // <--- ΝΕΟ

    /**
     * Constructor
     * - Παίρνει το κύριο frame και το repository του προφίλ
     * - Προσθέτει το JTabbedPane στο κέντρο του frame
     */
    public TabController(JFrame frame , UserProfileRepository repo){
        this.frame = frame;
        this.frame.add(createTabs(), BorderLayout.CENTER);
        this.userProfileRepository = repo;
    }

    /**
     * Δημιουργεί τα tabs και τα συνδέει με τις απαραίτητες ενέργειες (refresh/reload).
     */
    private JTabbedPane createTabs(){
        tabbedPane = new JTabbedPane();

        // 1. Activities Tab
        tabbedPane.addTab("Activities", createActivitiesPanel());

        // 2. Summary Tab
        summaryPanel = new SummaryPanel();

        // Σύνδεση του κουμπιού Reload (μέσα στο SummaryPanel)
        // ώστε να ξανατραβάει δεδομένα από repositories και να ενημερώνει το UI
        summaryPanel.setReloadAction(this::refreshSummary);

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


    /**
     * Refresh του Summary tab:
     * - Παίρνουμε δραστηριότητες από το activityRepository
     * - Παίρνουμε προφίλ χρήστη από το userProfileRepository
     * - Καλούμε summaryPanel.updateSummary(...) για ανανέωση cards + πίνακα
     */
    private void refreshSummary() {
        // Παίρνουμε τις δραστηριότητες και το προφίλ
        List<XMLActivity> activities = activityRepository.getTheActivities();
        UserProfile profile = userProfileRepository.getProfile();

        // Ενημερώνουμε το UI
        summaryPanel.updateSummary(activities, profile);
    }
    /**
     * Δημιουργεί το Activities tab με CardLayout:
     * - START panel: δείχνει μόνο το κουμπί "Show Stats"
     * - STATS panel: δείχνει το ActivitiesPanel και κουμπί "Επιστροφή"
     */

    private JPanel createActivitiesPanel() {
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

        // Όταν πατάμε "Show Stats":
        // 1) Φέρνουμε δραστηριότητες
        // 2) Αν δεν υπάρχουν → μήνυμα
        // 3) Αλλιώς → δείχνουμε το ActivitiesPanel με τα στατιστικά και πάμε στην κάρτα "STATS"
        showStatsButton.addActionListener(e -> {
            List<XMLActivity> activities = activityRepository.getTheActivities();
            if (activities == null || activities.isEmpty()) {
                JOptionPane.showMessageDialog(container, "Δεν υπάρχουν αρχεία.");
                return;
            }

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
}