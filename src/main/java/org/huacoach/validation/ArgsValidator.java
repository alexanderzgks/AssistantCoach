package org.huacoach.validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Κλάση υπεύθυνη για τον έλεγχο των arguments της γραμμής εντολών.
 * Ελέγχει αν ο χρήστης έδωσε σωστά τα αρχεία .tcx και αν έδωσε προαιρετικά
 * το βάρος του με τη σημαία -w.
 */
public final class ArgsValidator {

    // Private constructor για να μην μπορείς να κάνεις new ArgsValidator()
    private ArgsValidator() { }

    /**
     * Ελέγχει και αναλύει τα ορίσματα που δόθηκαν στην εφαρμογή.
     * Επιστρέφει ένα αντικείμενο ArgsResult με το βάρος και τη λίστα των αρχείων.
     * Αν υπάρχει λάθος στα ορίσματα, πετάει IllegalArgumentException.
     */
    public static ArgsResult validate(String[] args) {
        // 1. Έλεγχος αν υπάρχουν καθόλου ορίσματα
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Δεν δόθηκαν arguments. Πρέπει να δώσεις τουλάχιστον ένα .tcx αρχείο.");
        }

        double weight = -1.0;   // default: -1 σημαίνει ότι δεν δόθηκε βάρος
        int index = 0;          // Δείκτης που δείχνει από ποια θέση του πίνακα ξεκινάνε τα αρχεία

        // 2. Έλεγχος για το προαιρετικό flag -w στην αρχή
        if (args.length >= 2 && "-w".equals(args[0])) {
            String weightStr = args[1];
            try {
                weight = Double.parseDouble(weightStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Η τιμή βάρους δεν είναι έγκυρος αριθμός: " + weightStr);
            }

            // Το βάρος πρέπει να είναι θετικός αριθμός
            if (weight <= 0) {
                throw new IllegalArgumentException("Το βάρος πρέπει να είναι θετικός αριθμός.");
            }

            // Αν βρήκαμε βάρος, τα αρχεία ξεκινάνε από τη θέση 2
            index = 2;
        }

        // 3. Έλεγχος αν μετά το βάρος (ή χωρίς αυτό) υπάρχουν αρχεία
        if (index >= args.length) {
            throw new IllegalArgumentException("Δεν δόθηκε κανένα .tcx αρχείο.");
        }

        // 4. Συλλογή και έλεγχος των αρχείων .tcx
        List<String> tcxFiles = new ArrayList<>();

        for (int i = index; i < args.length; i++) {
            String fileName = args[i];

            // Έλεγχος σωστής κατάληξης
            if (!fileName.toLowerCase().endsWith(".tcx")) {
                throw new IllegalArgumentException("Το αρχείο δεν είναι τύπου .tcx: " + fileName);
            }

            // Έλεγχος ύπαρξης στο δίσκο
            File f = new File(fileName);
            if (!f.exists() || f.isDirectory()) {
                throw new IllegalArgumentException("Το αρχείο δεν βρέθηκε ή δεν είναι έγκυρο: " + fileName);
            }

            tcxFiles.add(fileName);
        }

        // Αν φτάσαμε εδώ, όλα είναι σωστά
        return new ArgsResult(weight, tcxFiles);
    }

    /**
     * Βοηθητική κλάση που κρατάει το αποτέλεσμα του ελέγχου (Validation).
     * Περιέχει το βάρος (αν δόθηκε) και τη λίστα των έγκυρων αρχείων.
     */
    public static class ArgsResult {

        private double weight = 0.0;
        private final List<String> tcxFiles;

        public ArgsResult(double weight, List<String> tcxFiles) {
            this.weight = weight;
            this.tcxFiles = tcxFiles;
        }

        // Επιστρέφει το βάρος σε κιλά (ή -1 αν δεν δόθηκε)
        public double getWeight() {
            return weight;
        }

        // Επιστρέφει τη λίστα με τα μονοπάτια των αρχείων
        public List<String> getTcxFiles() {
            return tcxFiles;
        }

        // Επιστρέφει true αν ο χρήστης έδωσε βάρος, αλλιώς false
        public boolean hasWeight() {
            return weight > 0;
        }
    }
}