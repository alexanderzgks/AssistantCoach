package org.huacoach.model;

import org.huacoach.enums.SportType;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class XMLActivity {

    private SportType sportType;
    private OffsetDateTime startTime;
    private String id;
    private List<Lap> laps = new ArrayList<>();

    public XMLActivity(SportType sportType, OffsetDateTime startTime, String id){
        this.sportType = sportType;
        this.startTime = startTime;
        this.id = id;
    }

    public XMLActivity(){
        this.sportType = SportType.OTHER;
        this.startTime = null;
    }

    public void setSportType(SportType sportType){
        this.sportType = sportType;
    }

    public void setStartTime(OffsetDateTime startTime){
        this.startTime = startTime;
    }

    public SportType getSportType(){
        return sportType;
    }

    public OffsetDateTime getStartTime(){
        return startTime;
    }

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
