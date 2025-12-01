package org.huacoach.app;

import org.huacoach.interfaces.Sex;

import java.util.Arrays;
import java.util.Scanner;

public class Check {

    public Sex checkInputForSex(Scanner scanner){
        Sex selectedSex = null;
        while (selectedSex == null) {
            System.out.print("\nSex (choose one): ");
            Arrays.stream(Sex.values())
                    .forEach(s -> System.out.print(" [" + s.name() + "] "));
            System.out.print("\nYour choice: ");
            String sexInput = scanner.nextLine().trim().toUpperCase();
            try {
                selectedSex = Sex.valueOf(sexInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please choose MALE, FEMALE, or OTHER.");
            }
        }
        return selectedSex;
    }

    public int checkInputForAge(Scanner scanner){
        int age = -1;
        while (age <= 0) {
            System.out.print("\nAge: ");
            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                if (age <= 0) {
                    System.out.println("Age must be a positive number.");
                }
            } else {
                System.out.println("Invalid input. Enter a number.");
                scanner.next();
            }
        }
        return age;
    }

     public double checkInputForWeight(Scanner scanner){
         double weight = -1;
         while (weight <= 0) {
             System.out.print("\nWeight: ");
             if (scanner.hasNextDouble()) {
                 weight = scanner.nextDouble();
                 if (weight <= 0) {
                     System.out.println("Weight must be a positive number.");
                 }
             } else {
                 System.out.println("Invalid input. Enter a number.");
                 scanner.next();
             }
         }
         return weight;
     }

}
