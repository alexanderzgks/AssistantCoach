package org.huacoach.services;

import org.huacoach.model.XMLActivity;
import org.huacoach.model.Lap;
import org.huacoach.enums.SportType;
import org.huacoach.services.StatisticsCalculator;

import java.util.List;

public class ActivityPrinter {

    public void print(XMLActivity activity, double weight) {
        List<Lap> laps = activity.getLaps();
        SportType sport = activity.getSportType();

        System.out.println("---------- ACTIVITY REPORT ----------");
        System.out.println("Type: " + formatSportName(sport));

        // Καλούμε τον Calculator εδώ για να πάρουμε τα νούμερα
        long totalSec = StatisticsCalculator.getTotalTimeSeconds(laps);
        double totalDistKm = StatisticsCalculator.getTotalDistanceMeters(laps) / 1000.0;
        int avgHr = StatisticsCalculator.getAverageHeartRate(laps);

        // Εκτύπωση
        System.out.println("Duration: " + formatDuration(totalSec)); // Πιο ωραία μορφή ώρας

        if (totalDistKm > 0) {
            System.out.printf("Distance: %.2f km%n", totalDistKm);
        }

        // Ειδική εκτύπωση ανάλογα με το άθλημα
        if (sport == SportType.RUNNING) {
            printRunningStats(laps);
        } else {
            printGeneralStats(laps);
        }

        if (avgHr > 0) {
            System.out.println("Avg Heart Rate: " + avgHr + " bpm");
        }

        // --- ΝΕΟ ΚΟΜΜΑΤΙ ΓΙΑ ΘΕΡΜΙΔΕΣ ---
        if (weight > 0) {
            // Σωστό:
            double calories = CaloriesCalculator.calculateCaloriesSimple(weight, totalSec, sport);
            System.out.printf("Calories: %.0f kcal%n", calories);
        }

        // ---------------------------------
        System.out.println("-------------------------------------\n");
    }

    private void printRunningStats(List<Lap> laps) {
        double pace = StatisticsCalculator.getAveragePaceMinPerKm(laps);
        if (pace > 0) {
            int min = (int) pace;
            int sec = (int) Math.round((pace - min) * 60);
            System.out.printf("Avg Pace: %d:%02d min/km%n", min, sec);
        }
    }

    private void printGeneralStats(List<Lap> laps) {
        double avgSpeed = StatisticsCalculator.getAverageSpeedKmh(laps);
        if (avgSpeed > 0) {
            System.out.printf("Avg Speed: %.2f km/h%n", avgSpeed);
        }
    }

    // Helper για ωραία εμφάνιση ονόματος (Running αντί για RUNNING)
    private String formatSportName(SportType sport) {
        if (sport == null) return "Unknown";
        String s = sport.name().toLowerCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    // Helper για να δείχνει ΩΩ:ΛΛ:ΔΔ αντί για σκέτα δευτερόλεπτα
    private String formatDuration(long totalSec) {
        long hours = totalSec / 3600;
        long minutes = (totalSec % 3600) / 60;
        long seconds = totalSec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}