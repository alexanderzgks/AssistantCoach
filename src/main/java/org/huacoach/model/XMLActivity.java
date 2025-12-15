package org.huacoach.model;

import org.huacoach.enums.SportType;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Κλάση που αναπαριστά μία αθλητική δραστηριότητα
 * όπως αυτή διαβάζεται από TCX αρχείο.
 * Η κλάση λειτουργεί ως data holder.
 */
public class XMLActivity {

    // Τύπος αθλήματος (running, cycling κλπ)
    private SportType sportType;
    // Χρόνος έναρξης της δραστηριότητας
    private OffsetDateTime startTime;
    // Αναγνωριστικό δραστηριότητας από το XML
    private String id;
    // Λίστα με τα laps
    private List<Lap> laps = new ArrayList<>();

    // Constructor
    public XMLActivity(SportType sportType, OffsetDateTime startTime, String id){
        this.sportType = sportType;
        this.startTime = startTime;
        this.id = id;
    }

    // -------- Setters --------
    public void setSportType(SportType sportType){
        this.sportType = sportType;
    }

    public void setStartTime(OffsetDateTime startTime){
        this.startTime = startTime;
    }

    // -------- Getters --------

    public SportType getSportType(){
        return sportType;
    }

    public OffsetDateTime getStartTime(){
        return startTime;
    }

    // Παίρνει τη λίστα από laps του Activity
    public List<Lap> getLaps(){
        return laps;
    }

    public void addLaps(Lap lap){
        laps.add(lap);
    }

    public Lap getIndexLap(int i){
        return laps.get(i);
    }
}
