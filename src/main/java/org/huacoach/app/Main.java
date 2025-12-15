package org.huacoach.app;

import org.huacoach.model.XMLActivity;
import org.huacoach.parser.TcxFileParser;
import org.huacoach.services.ActivityPrinter;
import org.huacoach.validation.ArgsValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Η κεντρική κλάση της εφαρμογής.
 * Συντονίζει τη λειτουργία του προγράμματος σε 3 στάδια:
 * 1. Έλεγχος Ορισμάτων (Validation)
 * 2. Ανάγνωση Αρχείων (Parsing)
 * 3. Εμφάνιση Αποτελεσμάτων (Printing)
 */
public class Main {

    // Στατικά αντικείμενα που θα χρησιμοποιήσουμε για το parsing και την εκτύπωση
    private static final TcxFileParser PARSER = new TcxFileParser();
    private static final ActivityPrinter PRINTER = new ActivityPrinter();

    // Λίστα για να αποθηκεύσουμε όλες τις δραστηριότητες που θα διαβαστούν επιτυχώς
    private static final List<XMLActivity> activities = new ArrayList<>();

    public static void main(String[] args) {

        // Βήμα 1: Έλεγχος και Επικύρωση Ορισμάτων (Validation)
        ArgsValidator.ArgsResult argsResult;
        try {
            // Καλείται ο Validator για να ξεχωρίσει το βάρος (-w) από τα αρχεία
            argsResult = ArgsValidator.validate(args);
        } catch (IllegalArgumentException e) {
            // Αν υπάρχει λάθος στα ορίσματα, εμφανίζουμε το μήνυμα και τερματίζουμε
            System.err.println("Error: " + e.getMessage());
            return;
        }

        // Βήμα 2: Ανάγνωση Αρχείων (Parsing)

        // Διατρέχουμε μόνο τη λίστα των έγκυρων αρχείων που επέστρεψε ο Validator
        for (String filePath : argsResult.getTcxFiles()) {
            try {
                // Διαβάζουμε το αρχείο XML
                XMLActivity activity = PARSER.readXMLFile(filePath);

                // Αν διαβάστηκε σωστά, το προσθέτουμε στη λίστα
                if (activity != null) {
                    activities.add(activity);
                }
            } catch (Exception e) {
                // Αν αποτύχει ένα αρχείο, εμφανίζουμε μήνυμα αλλά συνεχίζουμε στα επόμενα
                System.err.println("Failed to read: " + filePath);
            }
        }


        // Βήμα 3: Εμφάνιση Στατιστικών (Printing)
        // Αν δεν βρέθηκε καμία έγκυρη δραστηριότητα, τερματίζουμε
        if (activities.isEmpty()) {
            System.out.println("No valid activities found.");
            return;
        }

        // Για κάθε δραστηριότητα στη λίστα, καλούμε τον Printer
        for (XMLActivity activity : activities) {
            // Περνάμε και το βάρος (αν υπάρχει, αλλιώς είναι -1) για τον υπολογισμό θερμίδων
            PRINTER.print(activity, argsResult.getWeight());
        }
    }
}