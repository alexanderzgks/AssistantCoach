package org.huacoach.model;

import org.huacoach.enums.Sex;

public class UserProfile {

    private String userName;
    private Sex sex;
    private int age;
    private double weight;
    private String calcMethod;
    private int calorieGoal;


    public UserProfile(String userName, Sex sex, int age, double weight) {
        this.userName = userName;
        this.sex = sex;
        this.age = age;
        this.weight = weight;

        // Αρχικοποιούμε τα νέα πεδία με τις default τιμές εδώ
        this.calcMethod = "Simple";
        this.calorieGoal = 2000;
    }

    // --- Getters ---
    public String getUserName(){ return userName; }
    public Sex getSex(){ return sex; }
    public int getAge(){ return age; }
    public double getWeight(){ return weight; }
    public String getCalcMethod(){ return calcMethod; }
    public int getCalorieGoal(){ return calorieGoal; }

    // --- Setters ---
    public void setUserName(String userName){ this.userName = userName; }
    public void setSex(Sex sex){ this.sex = sex; }

    public void setAge(int age){
        if (age < 0) throw new IllegalArgumentException("Age must be positive");
        this.age = age;
    }

    public void setWeight(double weight){
        if (weight < 0) throw new IllegalArgumentException("Weight must be positive");
        this.weight = weight;
    }

    public void setCalcMethod(String calcMethod){
        this.calcMethod = calcMethod;
    }



    public void setCalorieGoal(int calorieGoal){
        if (calorieGoal < 0) throw new IllegalArgumentException("Goal cannot be negative");
        this.calorieGoal = calorieGoal;
    }
}