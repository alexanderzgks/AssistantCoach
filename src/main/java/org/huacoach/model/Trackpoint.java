package org.huacoach.model;

import java.time.OffsetDateTime;

/**
 * H class Trackpoint αντιπροσωπεύει τα trackpoint XML.
 */
public class Trackpoint {

    private OffsetDateTime time;
    private Double latitudeDegrees;
    private Double longitudeDegrees;
    private Double altitudeMeters;
    private Double distanceMeters;
    private Integer heartRate;
    private Integer cadence;

    public Trackpoint(OffsetDateTime time,
                      Double latitudeDegrees,
                      Double longitudeDegrees,
                      Double altitudeMeters,
                      Double distanceMeters,
                      Integer heartRate,
                      Integer cadence){
        this.time = time;
        this.latitudeDegrees = latitudeDegrees;
        this.longitudeDegrees = longitudeDegrees;
        this.altitudeMeters = altitudeMeters;
        this.distanceMeters = distanceMeters;
        this.heartRate = heartRate;
        this.cadence = cadence;
    }

    public OffsetDateTime getTime(){ return time; }
    public Double getLatitudeDegrees(){ return latitudeDegrees; }
    public Double getLongitudeDegrees(){ return longitudeDegrees; }
    public Double getAltitudeMeters(){ return altitudeMeters; }
    public Double getDistanceMeters(){ return distanceMeters; }
    public Integer getHeartRate(){ return heartRate; }
    public Integer getCadence(){ return cadence; }


    @Override
    public String toString(){
        return  "\n---Trackpoint---"+
                "\nTime: " + getTime() +
                "\nLatitude: " + getLatitudeDegrees() +
                "\nLongitude: " + getLongitudeDegrees() +
                "\nAltitude: " + getAltitudeMeters() +
                "\nDistance: " + getDistanceMeters() +
                "\nHeart Rate: " + getHeartRate() +
                "\nCadence: " + getCadence();
    }



}
