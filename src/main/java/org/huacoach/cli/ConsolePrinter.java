package org.huacoach.cli;

import org.huacoach.interfaces.Activity;

public class ConsolePrinter {

    public void print(Activity a) {
        System.out.println("Activity: " + a.getType());
        System.out.println("Total Time: " + a.getTotalTime());
        System.out.println("Distance: " + a.getTotalDistanceKm());
        System.out.println("Avg Speed: " + a.getAvgSpeedKmh());
        System.out.println("Calories: " + a.getCalories());
        System.out.println();
    }
}
