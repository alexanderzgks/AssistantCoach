package org.huacoach.validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Κλάση υπεύθυνη για τον έλεγχο των arguments της γραμμής εντολών.
 * Υποστηριζόμενες μορφές:
 *   java -jar coach.jar file1.tcx file2.tcx
 *   java -jar coach.jar -w 65.9 file1.tcx file2.tcx
 */
public final class ArgsValidator {

    // private constructor για να μην μπορείς να κάνεις new ArgsValidator()
    private ArgsValidator() { }

    public static ArgsResult validate(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Δεν δώθηκαν arguments. Πρέπει να δώσεις τουλάχιστον ένα .tcx αρχείο.");
        }
        double weight = -1.0;   // default: δεν υπάρχει βάρος
        int index = 0;          // από πού ξεκινάνε τα αρχεία στα args
        // ===== Έλεγχος για -w στην αρχή =====
        if (args.length >= 2 && "-w".equals(args[0])) {
            String weightStr = args[1];
            try {
                weight = Double.parseDouble(weightStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Η τιμή βάρους δεν είναι έγκυρος αριθμός: " + weightStr);
            }
            if (weight <= 0) {
                throw new IllegalArgumentException("Το βάρος πρέπει να είναι θετικός αριθμός.");
            }
            index = 2;
        }

        // ===== Δεν δόθηκαν αρχεία .tcx =====
        if (index >= args.length) {
            throw new IllegalArgumentException("Δεν δόθηκε κανένα .tcx αρχείο.");
        }

        // ===== Συλλογή & έλεγχος των .tcx αρχείων =====
        List<String> tcxFiles = new ArrayList<>();
        for (int i = index; i < args.length; i++) {
            String fileName = args[i];
            // Έλεγχος κατάληξης .tcx
            if (!fileName.toLowerCase().endsWith(".tcx")) {
                throw new IllegalArgumentException("Το αρχείο δεν είναι τύπου .tcx: " + fileName);
            }
            // Έλεγχος αν υπάρχει το αρχείο στον δίσκο
            File f = new File(fileName);
            if (!f.exists() || f.isDirectory()) {
                throw new IllegalArgumentException("Το αρχείο δεν βρέθηκε ή δεν είναι έγκυρο: " + fileName);
            }
            tcxFiles.add(fileName);
        }

        // Αν όλα είναι ΟΚ, φτιάχνουμε το αποτέλεσμα
        return new ArgsResult(weight, tcxFiles);
    }

    /**
     * Μικρή βοηθητική κλάση που κρατάει το αποτέλεσμα του validation.
     */
    public static class ArgsResult {

        private double weight = 0.0;
        private final List<String> tcxFiles;

        public ArgsResult(double weight, List<String> tcxFiles) {
            this.weight = weight;
            this.tcxFiles = tcxFiles;
        }

        /**
         * @return το βάρος που δόθηκε, ή -1 αν δεν δόθηκε καθόλου -w
         */
        public double getWeight() {
            return weight;
        }

        /**
         * @return λίστα με όλα τα .tcx αρχεία
         */
        public List<String> getTcxFiles() {
            return tcxFiles;
        }

        /**
         * @return true αν ο χρήστης έδωσε βάρος με -w
         */
        public boolean hasWeight() {
            return weight > 0;
        }
    }
}

