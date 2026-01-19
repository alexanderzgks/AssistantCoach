package org.huacoach.app;

public class AppMain {

    public static void main(String[] args) {
        // ελέγχει αν δόθηκαν ορίσματα κατά την εκτέλεση
        if (args.length > 0) {
            // μέρος 1 υπάρχουν ορίσματα τότε τρέχει την Console έκδοση
            // Καλεί την main της OldMain
            OldMain.main(args);
        } else {
            // μέρος 2 δεν υπάρχουν ορίσματα  τρέχει το GUI
            // Καλεί την main της NewMain
            NewMain.main(args);
        }
    }
}