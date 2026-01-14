package org.huacoach.ui.menu;
import org.huacoach.data.UserProfileRepository;
import org.huacoach.enums.Sex;
import org.huacoach.model.UserProfile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 * ProfileDialog
 *
 * Μικρό παράθυρο (JDialog) για προβολή/επεξεργασία του προφίλ χρήστη.
 * Ανοίγει ως modal dialog (μπλοκάρει το main window μέχρι να κλείσει).
 *
 * Ρόλος:
 * - Παρουσιάζει φόρμα (username/sex/age/weight)
 * - Κάνει βασικό validation των τιμών
 * - Ενημερώνει το UserProfile και το αποθηκεύει στο UserProfileRepository
 */
public class ProfileDialog extends JDialog{

    /** Πεδίο εισαγωγής για το username του χρήστη */
    private JTextField userNameField;
    /** Επιλογή φύλου μέσω enum Sex */
    private JComboBox<Sex>  sexComboBox;
    /** Πεδίο εισαγωγής για ηλικία (αναμένεται ακέραιος αριθμός) */
    private JTextField ageField;
    /** Πεδίο εισαγωγής για βάρος (αναμένεται δεκαδικός αριθμός) */
    private JTextField weightField;
    /**
     * Repository που κρατάει/διαχειρίζεται το τρέχον UserProfile της εφαρμογής.
     */
    private final UserProfileRepository userProfileRepository;
    /**
     * Το προφίλ που επεξεργάζεται το dialog.
     */
    private UserProfile profile;
    private JComboBox<String> methodComboBox;
    private JTextField goalField;

    /**
     * Constructor του dialog.
     *
     * @param frame το κύριο JFrame (parent) ώστε το dialog να κεντράρει και να είναι modal πάνω του
     * @param userProfileRepository repository όπου αποθηκεύονται οι αλλαγές του profile
     * @param profile το προφίλ που θα φορτωθεί στη φόρμα (τρέχουσες τιμές)
     */
    public ProfileDialog(JFrame frame, UserProfileRepository userProfileRepository , UserProfile profile){
        super(frame, "My Profile", true);
        this.userProfileRepository = userProfileRepository;
        this.profile = profile;
        setSize(400, 550);
        setLocationRelativeTo(frame);
        setLayout(new BorderLayout(10, 10));
        add(createForm(profile), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }


    /**
     * Δημιουργεί το panel της φόρμας (labels + inputs) για το προφίλ.
     *
     * @param profile το προφίλ από το οποίο θα φορτωθούν οι αρχικές τιμές
     * @return JPanel με GridLayout 4x2
     */
    private JPanel createForm(UserProfile profile) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Username
        panel.add(new JLabel("Username:"));
        userNameField = new JTextField(profile != null ? profile.getUserName() : "You");
        panel.add(userNameField);

        // Sex
        panel.add(new JLabel("Sex:"));
        sexComboBox = new JComboBox<>(Sex.values());
        if (profile != null && profile.getSex() != null) {
            sexComboBox.setSelectedItem(profile.getSex());
        }
        panel.add(sexComboBox);

        // Age
        panel.add(new JLabel("Age:"));
        ageField = new JTextField(profile != null ? String.valueOf(profile.getAge()) : "");
        panel.add(ageField);

        // Weight
        panel.add(new JLabel("Weight (kg):"));
        weightField = new JTextField(profile != null ? String.valueOf(profile.getWeight()) : "");
        panel.add(weightField);

        panel.add(new JLabel("Calculation Method:"));
        methodComboBox = new JComboBox<>(new String[]{"Simple", "Advanced"});
        if (profile != null && profile.getCalcMethod() != null) {
            methodComboBox.setSelectedItem(profile.getCalcMethod());
        }
        panel.add(methodComboBox);

        if (profile != null && profile.getCalcMethod() != null) {
            methodComboBox.setSelectedItem(profile.getCalcMethod());
        }
        panel.add(methodComboBox);

        //  Daily Goal
        panel.add(createLabel("Daily Goal (kcal):"));
        // Παίρνουμε την τρέχουσα τιμή από το προφίλ (αν υπάρχει), αλλιώς 2000
        String goalVal = (profile != null) ? String.valueOf(profile.getCalorieGoal()) : "2000";
        goalField = new JTextField(goalVal);
        panel.add(goalField);


        return panel;

    }

    /**
     * Δημιουργεί το panel με τα κουμπιά Save/Cancel.
     *
     * @return JPanel με FlowLayout δεξιά
     */
    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        save.setPreferredSize(new Dimension(80, 30));
        cancel.setPreferredSize(new Dimension(80, 30));

        save.addActionListener(e -> onSave());
        cancel.addActionListener(e -> dispose());

        panel.add(save);
        panel.add(cancel);
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    /**
     * Διαβάζει τις τιμές από τη φόρμα, κάνει βασικό validation
     * και αν όλα είναι σωστά ενημερώνει το UserProfile και το repository.
     */
    private void onSave() {
        try {
            // Διαβάζουμε τις τιμές από τα inputs
            String userName = userNameField.getText().trim();
            Sex sex = (Sex) sexComboBox.getSelectedItem();
            int goal = Integer.parseInt(goalField.getText().trim());
            String method = (String) methodComboBox.getSelectedItem();

            // Age: αν μείνει κενό, κρατάμε 0 (design choice)
            int age = 0;
            if (!ageField.getText().trim().isEmpty()) {
                age = Integer.parseInt(ageField.getText().trim());
            }

            // Weight: αν μείνει κενό, κρατάμε 0.0 (design choice)
            double weight = 0.0;
            if (!weightField.getText().trim().isEmpty()) {
                weight = Double.parseDouble(weightField.getText().trim());
            }

            // Validation: username δεν πρέπει να είναι κενό
            if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Το Username δεν μπορεί να είναι κενό.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (age < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Η ηλικία δεν μπορεί να είναι αρνητική.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (weight < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Το βάρος δεν μπορεί να είναι αρνητικό.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Ενημερώνουμε το profile με τις νέες τιμές
            profile.setUserName(userName);
            profile.setSex(sex);
            profile.setAge(age);
            profile.setWeight(weight);
            profile.setCalorieGoal(goal);
            profile.setCalcMethod(method);

            // Αποθήκευση (in-memory) στο repository
            userProfileRepository.setProfile(profile);
            String selectedMethod = (String) methodComboBox.getSelectedItem();
            profile.setCalcMethod(selectedMethod);

            // Κλείσιμο dialog μετά από επιτυχημένο Save
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    // Σφάλμα parsing σε age/weight (π.χ. γράφτηκε "abc")
                    this,
                    "Age πρέπει να είναι ακέραιος και Weight δεκαδικός αριθμός (π.χ. 72.5).",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
