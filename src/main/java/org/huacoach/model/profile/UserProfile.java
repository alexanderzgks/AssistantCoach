package org.huacoach.model.profile;

public class UserProfile {

    private final int ssn;
    private final String userName;
    private final String sex;
    private int age;
    private double weight;

    public UserProfile(int ssn, String userName, String sex){

    }

    public UserProfile(int ssn, String userName, String sex, int age,double weight){
        this.ssn = ssn;
        this.userName = userName;
        this.sex = sex;
        this.age = age;
        this.weight = weight;
    }

    public int getSsn(){ return ssn; }
    public String getUserName(){ return userName; }
    public String getSex(){ return sex; }
    public int getAge(){ return age; }
    public double getWeight(){ return weight;}

    public void setAge(int age){ this.age = age; }
    public void setWeight(double weight){ this.weight = weight; }
}