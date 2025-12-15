package org.huacoach.model;

import java.time.OffsetDateTime;

/**
 * Η κλάση Trackpoint αναπαριστά ένα αντίστοιχο track point
 * όπως στο TCX.
 */
public class Trackpoint {

    private OffsetDateTime time; // Χρονική στιγμή καταγραφής του track point
    private Double latitudeDegrees; // Γεωγραφικό πλάτος
    private Double longitudeDegrees; // Γεωγραφικό μήκος
    private Double altitudeMeters; // Υψόμετρο σε μέτρα
    private Double distanceMeters; // Συνολική απόσταση μέχρι το συγκεκριμένο σημείο
    private Integer heartRate; // Καρδιακοί παλμοί τη συγκεκριμένη στιγμή
    private Integer cadence;  // Cadence (βήματα ή στροφές ανά λεπτό)

    // Constructor
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


    // ------------------ Getters ------------------
    public OffsetDateTime getTime(){ return time; }
    public Double getLatitudeDegrees(){ return latitudeDegrees; }
    public Double getLongitudeDegrees(){ return longitudeDegrees; }
    public Double getAltitudeMeters(){ return altitudeMeters; }
    public Double getDistanceMeters(){ return distanceMeters; }
    public Integer getHeartRate(){ return heartRate; }
    public Integer getCadence(){ return cadence; }

    // Eκτυπώνει τα δεδομένων
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
