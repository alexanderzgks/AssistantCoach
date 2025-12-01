package org.huacoach.services;

import org.huacoach.enums.Sex;
import java.time.Duration;

public class CaloriesCalculator { // Διόρθωση 1: Χωρίς ()


    public double getCalories(double weight, Duration duration, int age, Sex sex, double avgHeartRate) {


        double t = duration.toMinutes();


        if (avgHeartRate <= 0) {
            return 0;
        }

        double calories;

        if (sex == Sex.MALE) { // Διόρθωση 2: Σύγκριση Enum με ==
            // Formula Ανδρών

            calories = (-55.0969 + (0.6309 * avgHeartRate) + (0.1966 * weight) + (0.2017 * age)) * t / 4.184;
        } else {
            // Formula Γυναικών (Χρησιμοποιούμε αυτό και για Sex.OTHER ως default ή βάζουμε logic)

            calories = (-20.4022 + (0.4472 * avgHeartRate) + (0.1263 * weight) + (0.074 * age)) * t / 4.184;
        }

        return calories;
    }
}