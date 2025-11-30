package org.huacoach.model.XMLmodels;

import org.huacoach.interfaces.SportType;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityModel {

    private SportType sportType;
    private OffsetDateTime startTime;
    private List<Lap> laps = new ArrayList<>();

    public ActivityModel(SportType sportType, OffsetDateTime startTime){
        this.sportType = sportType;
        this.startTime = startTime;
    }

    public ActivityModel(){
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
