package org.huacoach.app;

import org.huacoach.enums.SportType;
import org.huacoach.model.XMLActivity;
import org.huacoach.parser.TcxFileParser;
import org.huacoach.validation.ArgsValidator;
import org.huacoach.model.*;
import java.util.ArrayList;
import java.util.List;
import static org.huacoach.services.StatisticsCalculator.*;


public class Main {
    private static final TcxFileParser TCX_FILE_PARSER = new TcxFileParser();
    private static final List<XMLActivity> activityModelList = new ArrayList<>();

    public static void main(String[] args){
        ArgsValidator.ArgsResult argsResult = null;
        try{
            argsResult = ArgsValidator.validate(args);
        }catch (IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }

        double weight = argsResult.getWeight();
        for (String j : args) {
            activityModelList.add(TCX_FILE_PARSER.readXMLFile(j));
        }
        System.out.println("Ok");

        for (XMLActivity activity : activityModelList) {

            SportType sport = activity.getSportType();
            System.out.println("Activity: " + sportToString(sport));

            long totalSec = getTotalTimeSeconds(activity.getLaps());
            double totalDistKm = getTotalDistanceMeters(activity.getLaps()) / 1000.0;
            int avgHr = getAverageHeartRate(activity.getLaps());

            System.out.println("Total Time: " + totalSec);
            if (totalDistKm > 0) {
                System.out.printf("Total Distance: %.2f km%n", totalDistKm);
            }

            if (sport == SportType.RUNNING) {
                double pace = getAveragePaceMinPerKm(activity.getLaps());
                if (pace > 0) {
                    int min = (int) pace;
                    int sec = (int) Math.round((pace - min) * 60);
                    System.out.printf("Avg Pace: %d:%02d min/km%n", min, sec);
                }
            } else {
                double avgSpeed = getAverageSpeedKmh(activity.getLaps());
                if (avgSpeed > 0) {
                    System.out.printf("Avg Speed: %.2f km/h%n", avgSpeed);
                }
            }

            if (avgHr > 0) {
                System.out.println("Avg Heart Rate: " + avgHr + " bpm");
            }

//        if (weightKg > 0) {
//            double calories = CaloriesCalculator.computeCaloriesSimple(activity, weightKg);
//            System.out.printf("Calories: %.0f kcal%n", calories);
//        }

            System.out.println(); // κενή γραμμή ανά Activity
        }
    }

    private static String sportToString(SportType sport) {
        switch (sport) {
            case RUNNING:
                return "Running";
            case CYCLING:
                return "Biking";
            case WALKING:
                return "Walking";
            case SWIMMING:
                return "Swimming";
            default:
                return "Other";
        }
    }
}
