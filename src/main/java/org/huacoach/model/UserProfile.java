package org.huacoach.model;

import org.huacoach.enums.Sex;

public class UserProfile {

    private String userName;
    private Sex sex;
    private int age;
    private double weight;

    public UserProfile(String userName, Sex sex, int age, double weight){
        this.userName = userName;
        this.sex = sex;
        this.age = age;
        this.weight = weight;
    }

    public String getUserName(){ return userName; }
    public Sex getSex(){ return sex; }
    public int getAge(){ return age; }
    public double getWeight(){ return weight; }

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
}
