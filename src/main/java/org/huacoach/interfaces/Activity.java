package org.huacoach.interfaces;

import java.time.Duration;

public interface Activity {
    String getType();
    Duration getTotalTime();
    double getTotalDistanceKm();
    double getAvgSpeedKmh();
    double getMaxSpeedMinKm();
    double getMinSpeedMinKm();
    int getAvgHeartRate();
    int getMaxHearRate();
    double getCalories();
    void setCalories(double calories);
}
