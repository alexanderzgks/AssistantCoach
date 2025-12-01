package org.huacoach.model.profile;

import org.huacoach.interfaces.Sex;
import org.huacoach.model.XMLmodels.ActivityModel;

import java.util.List;

public class UserProfile {

    private final List<ActivityModel> activities;
    private final String userName;
    private final Sex sx;
    private int age;
    private double weight;

    public UserProfile(List<ActivityModel> activities,String userName, Sex sx, int age,double weight){
        this.activities = activities;
        this.userName = userName;
        this.sx = sx;
        this.age = age;
        this.weight = weight;
    }

    public List<ActivityModel> getActivities(){ return activities; }
    public String getUserName(){ return userName; }
    public Sex getSex(){ return sx; }
    public int getAge(){ return age; }
    public double getWeight(){ return weight;}

    public void setAge(int age){ this.age = age; }
    public void setWeight(double weight){ this.weight = weight; }
}