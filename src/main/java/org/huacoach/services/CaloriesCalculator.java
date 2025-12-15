package org.huacoach.services;

import org.huacoach.enums.Sex;
import org.huacoach.enums.SportType;

public class CaloriesCalculator {


    // ΤΡΟΠΟΣ 1: Απλός (Μόνο με βάρος και είδος άθλησης)
    // Formula: C = mu * weight * time (mins)
    public static double calculateCaloriesSimple(double weightKg, long durationSeconds, SportType sport) {
        if (weightKg <= 0 || durationSeconds <= 0) return 0;

        double timeInMinutes = durationSeconds / 60.0;
        double mu = getMultiplier(sport);

        return mu * weightKg * timeInMinutes;
    }

    private static double getMultiplier(SportType sport) {
        if (sport == null) return 0.05;

        // Τιμές METs (κατά προσέγγιση για την άσκηση)
        switch (sport) {
            case RUNNING:
                return 0.13;
            case CYCLING:
                return 0.10;
            case SWIMMING:
                return 0.14;
            case WALKING:
                return 0.06;
            default:
                return 0.05;
        }
    }

    // -----------------------------------------------------------
    // ΤΡΟΠΟΣ 2: Ακριβής (Με καρδιακούς παλμούς, ηλικία, φύλο)
    // Formula: Από την εκφώνηση
    // -----------------------------------------------------------
    public static double calculateCaloriesAdvanced(double weight, long durationSeconds, int age, Sex sex, int avgHeartRate) {
        if (avgHeartRate <= 0 || weight <= 0 || durationSeconds <= 0) {
            return 0;
        }

        double t = durationSeconds / 60.0; // Ο χρόνος σε λεπτά
        double calories;

        if (sex == Sex.MALE) {
            // Formula Ανδρών
            calories = (-55.0969 + (0.6309 * avgHeartRate) + (0.1966 * weight) + (0.2017 * age)) * t / 4.184;
        } else {
            // Formula Γυναικών (και default)
            calories = (-20.4022 + (0.4472 * avgHeartRate) + (0.1263 * weight) + (0.074 * age)) * t / 4.184;
        }

        return calories;
    }
}