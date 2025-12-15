package org.huacoach.services;

import org.huacoach.model.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Kλάση που περιέχει στατικές μεθόδους για τον υπολογισμό στατιστικών
 * δεδομένων από μια λίστα γύρων (Laps).
 * Υπολογίζει μεγέθη όπως συνολικό χρόνο, απόσταση, μέση ταχύτητα,
 * μέσο ρυθμό (pace) και μέσους καρδιακούς παλμούς.
 */
public class StatisticsCalculator {

    /**
     * Υπολογίζει τον συνολικό χρόνο της δραστηριότητας σε δευτερόλεπτα.
     * Η μέθοδος σαρώνει όλα τα Trackpoints για να βρει την ελάχιστη (αρχή)
     * και τη μέγιστη (τέλος) χρονική στιγμή και υπολογίζει τη διαφορά τους.
     * laps Η λίστα με τους γύρους της δραστηριότητας.
     * Επιστρέφει την συνολική διάρκεια σε δευτερόλεπτα. Επιστρέφει 0 αν δεν υπάρχουν δεδομένα χρόνου.
     */
    public static long getTotalTimeSeconds(List<Lap> laps) {
        OffsetDateTime min = null;
        OffsetDateTime max = null;

        for (Lap lap : laps) {
            for (Track track : lap.getTracks()) {
                for (Trackpoint tp : track.getTrackpoint()) {
                    OffsetDateTime t = tp.getTime();
                    if (t == null) continue;

                    // Εύρεση της νωρίτερης ώρας
                    if (min == null || t.isBefore(min)) {
                        min = t;
                    }
                    // Εύρεση της αργότερης ώρας
                    if (max == null || t.isAfter(max)) {
                        max = t;
                    }
                }
            }
        }

        if (min == null || max == null) {
            return 0;
        }

        return Duration.between(min, max).getSeconds();
    }

    /**
     * Υπολογίζει τη συνολική απόσταση της δραστηριότητας σε μέτρα.
     * Στα αρχεία TCX, η απόσταση καταγράφεται αθροιστικά (cumulative).
     * Επομένως, η συνολική απόσταση είναι η μέγιστη τιμή DistanceMeters
     * που θα βρεθεί σε οποιοδήποτε Trackpoint.
     *  laps Η λίστα με τους γύρους.
     * Επιστρέφει τη μέγιστη απόσταση που καταγράφηκε σε μέτρα.
     */
    public static double getTotalDistanceMeters(List<Lap> laps) {
        double maxDistance = 0.0;
        for (Lap lap : laps) {
            for (Track track : lap.getTracks()) {
                for (Trackpoint tp : track.getTrackpoint()) {
                    Double d = tp.getDistanceMeters();
                    // Κρατάμε τη μεγαλύτερη τιμή που θα συναντήσουμε
                    if (d != null && d > maxDistance) {
                        maxDistance = d;
                    }
                }
            }
        }
        return maxDistance;
    }

    /**
     * Υπολογίζει τον μέσο όρο των καρδιακών παλμών.
     * Αθροίζει όλες τις μη κενές (non-null) εγγραφές καρδιακών παλμών
     * και διαιρεί με το πλήθος τους.
     *  laps Η λίστα με τους γύρους.
     * Επιστρέφει  το μέσο καρδιακό παλμό (BPM). Επιστρέφει -1 αν δεν υπάρχουν καθόλου δεδομένα.
     */
    public static int getAverageHeartRate(List<Lap> laps) {
        int sum = 0;
        int count = 0;
        for (Lap lap : laps) {
            for (Track track : lap.getTracks()) {
                for (Trackpoint tp : track.getTrackpoint()) {
                    Integer hr = tp.getHeartRate();
                    if (hr != null) {
                        sum += hr;
                        count++;
                    }
                }
            }
        }
        if (count == 0) return -1;
        return sum / count;
    }

    /**
     * Υπολογίζει τη μέση ταχύτητα σε χιλιόμετρα ανά ώρα (km/h).
     * Χρησιμοποιεί τις μεθόδους getTotalDistanceMeters και
     * getTotalTimeSeconds για τον υπολογισμό.
     *  laps Η λίστα με τους γύρους.
     *  Επιστρέφει τη μέση ταχύτητα σε km/h. Επιστρέφει 0.0 αν ο χρόνος είναι μηδέν.
     */
    public static double getAverageSpeedKmh(List<Lap> laps) {
        double distMeters = getTotalDistanceMeters(laps);
        long timeSec = getTotalTimeSeconds(laps);

        if (timeSec <= 0) return 0.0;

        double distKm = distMeters / 1000.0;
        double hours = timeSec / 3600.0;

        return distKm / hours;
    }

    /**
     * Υπολογίζει τον μέσο ρυθμό (pace) σε λεπτά ανά χιλιόμετρο (min/km).
     * Ο ρυθμός είναι το αντίστροφο της ταχύτητας και χρησιμοποιείται κυρίως στο τρέξιμο.
     *  laps Η λίστα με τους γύρους.
     * Επιστρέφει το ρυθμό σε min/km. Επιστρέφει 0.0 αν η απόσταση ή ο χρόνος είναι μηδέν.
     */
    public static double getAveragePaceMinPerKm(List<Lap> laps) {
        double distMeters = getTotalDistanceMeters(laps);
        long timeSec = getTotalTimeSeconds(laps);

        if (distMeters <= 0 || timeSec <= 0) return 0.0;

        double distKm = distMeters / 1000.0;
        double minutes = timeSec / 60.0;

        return minutes / distKm;
    }
}