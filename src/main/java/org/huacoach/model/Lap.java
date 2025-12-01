package org.huacoach.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Lap {

    private OffsetDateTime startTime;
    private List<Track> tracks = new ArrayList<>();

    public Lap(OffsetDateTime startTime){
        this.startTime = startTime;
    }

    public OffsetDateTime getStartTime(){
        return startTime;
    }

    public List<Track> getTracks(){
        return tracks;
    }

    public Track getIndexTrack(int i){
        return tracks.get(i);
    }

    public void addTracks(Track track){
        tracks.add(track);
    }


}
