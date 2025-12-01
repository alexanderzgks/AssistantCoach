package org.huacoach.services;

import org.huacoach.model.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

public class StatisticsCalculator {

    /**
     * Συνολικός χρόνος σε δευτερόλεπτα:
     * από την ελάχιστη ώρα Trackpoint μέχρι τη μέγιστη.
     */
    public static long getTotalTimeSeconds(List<Lap> laps) {
        OffsetDateTime min = null;
        OffsetDateTime max = null;

        for (Lap lap : laps) {
            for (Track track : lap.getTracks()) {
                for (Trackpoint tp : track.getTrackpoint()) {
                    OffsetDateTime t = tp.getTime();
                    if (t == null) continue;
                    if (min == null || t.isBefore(min)) {
                        min = t;
                    }
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
     * Συνολική απόσταση σε μέτρα:
     * παίρνουμε τη ΜΕΓΙΣΤΗ DistanceMeters οποιουδήποτε Trackpoint.
     * (Στα TCX η απόσταση είναι cumulative).
     */
    public static double getTotalDistanceMeters(List<Lap> laps) {
        double maxDistance = 0.0;
        for (Lap lap : laps) {
            for (Track track : lap.getTracks()) {
                for (Trackpoint tp : track.getTrackpoint()) {
                    Double d = tp.getDistanceMeters();
                    if (d != null && d > maxDistance) {
                        maxDistance = d;
                    }
                }
            }
        }
        return maxDistance;
    }

    /**
     * Μέσος καρδιακός παλμός (αν υπάρχουν δεδομένα).
     * Επιστρέφει -1 αν δεν έχει καθόλου heart rate.
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
     * Μέση ταχύτητα σε km/h.
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
     * Μέσο pace σε min/km (0 αν δεν υπάρχει απόσταση).
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

