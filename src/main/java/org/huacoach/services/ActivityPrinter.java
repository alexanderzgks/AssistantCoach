package org.huacoach.services;

import org.huacoach.model.XMLActivity;
import org.huacoach.model.Lap;
import org.huacoach.enums.SportType;
import java.util.List;

/**
 * Κλάση υπεύθυνη για την παρουσίαση (εκτύπωση) των στατιστικών στοιχείων
 * μιας αθλητικής δραστηριότητας στην κονσόλα.
 */
public class ActivityPrinter {
    /**
     * Εκτυπώνει μια πλήρη αναφορά για τη δραστηριότητα.
     * Υπολογίζει δυναμικά τα στατιστικά καλώντας τους απαραίτητους Calculators.
     * activity Η δραστηριότητα προς ανάλυση.
     *  weight   Το βάρος του αθλητή (αν είναι <= 0, δεν υπολογίζονται θερμίδες).
     */
    public void print(XMLActivity activity, double weight) {
        List<Lap> laps = activity.getLaps();
        SportType sport = activity.getSportType();

        System.out.println("---------- ACTIVITY REPORT ----------");
        System.out.println("Type: " + formatSportName(sport));

        // 1. Συλλογή των βασικών στατιστικών από τον StatisticsCalculator
        long totalSec = StatisticsCalculator.getTotalTimeSeconds(laps);
        double totalDistKm = StatisticsCalculator.getTotalDistanceMeters(laps) / 1000.0;
        int avgHr = StatisticsCalculator.getAverageHeartRate(laps);

        // 2. Εκτύπωση Διάρκειας (μορφοποιημένη σε ΩΩ:ΛΛ:ΔΔ)
        System.out.println("Duration: " + formatDuration(totalSec));

        //3. Εκτύπωση Απόστασης (μόνο αν υπάρχει καταγεγραμμένη απόσταση)
        if (totalDistKm > 0) {
            System.out.printf("Distance: %.2f km%n", totalDistKm);
        }

        // 4. Εξειδικευμένη εκτύπωση ανάλογα με το άθλημα
        if (sport == SportType.RUNNING) {
            printRunningStats(laps);
        } else {
            printGeneralStats(laps);
        }
        //υπολογισμος της καρδιακων παλμων
        if (avgHr > 0) {
            System.out.println("Avg Heart Rate: " + avgHr + " bpm");
        }

        //υπολογισμος θερμιδων
        if (weight > 0) {
            double calories = CaloriesCalculator.calculateCaloriesSimple(weight, totalSec, sport);
            System.out.printf("Calories: %.0f kcal%n", calories);
        }

        // ---------------------------------
        System.out.println("-------------------------------------\n");
    }

    //μεθοδος που εκτυπωνει τα στατιστικα τρεξιματος
    private void printRunningStats(List<Lap> laps) {
        double pace = StatisticsCalculator.getAveragePaceMinPerKm(laps);
        if (pace > 0) {
            // Μετατροπή του δεκαδικού pace (π.χ. 5.5) σε λεπτά και δευτερόλεπτα (5:30)
            int min = (int) pace;
            int sec = (int) Math.round((pace - min) * 60);
            System.out.printf("Avg Pace: %d:%02d min/km%n", min, sec);
        }
    }

    /**
     * Υπολογίζει και εκτυπώνει τη μέση ταχύτητα σε km/h.
     * Χρησιμοποιείται για ποδηλασία και γενικές δραστηριότητες.
     */
    private void printGeneralStats(List<Lap> laps) {
        double avgSpeed = StatisticsCalculator.getAverageSpeedKmh(laps);
        if (avgSpeed > 0) {
            System.out.printf("Avg Speed: %.2f km/h%n", avgSpeed);
        }
    }

    /**
     * Βοηθητική μέθοδος για τη μορφοποίηση του ονόματος του αθλήματος.
     * Μετατρέπει το "RUNNING" σε "Running".
     */
    private String formatSportName(SportType sport) {
        if (sport == null) return "Unknown";
        String s = sport.name().toLowerCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * Μετατρέπει τα συνολικά δευτερόλεπτα σε μορφή ώρας (HH:mm:ss).
     */
    private String formatDuration(long totalSec) {
        long hours = totalSec / 3600;
        long minutes = (totalSec % 3600) / 60;
        long seconds = totalSec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}