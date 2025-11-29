package org.huacoach.model.profile;

import org.huacoach.interfaces.Sex;

public class UserProfile {

    private final int ssn;
    private final String userName;
    private final Sex sx;
    private int age;
    private double weight;

    public UserProfile(int ssn, String userName, Sex sx, int age,double weight){
        this.ssn = ssn;
        this.userName = userName;
        this.sx = sx;
        this.age = age;
        this.weight = weight;
    }

    public int getSsn(){ return ssn; }
    public String getUserName(){ return userName; }
    public Sex getSex(){ return sx; }
    public int getAge(){ return age; }
    public double getWeight(){ return weight;}

    public void setAge(int age){ this.age = age; }
    public void setWeight(double weight){ this.weight = weight; }
}