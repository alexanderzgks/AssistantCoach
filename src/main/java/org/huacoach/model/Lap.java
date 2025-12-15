package org.huacoach.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Η κλάση αυτή αναπαριστά ένα lap μιας δραστηριότητας.
 */
public class Lap {

    // Χρόνος έναρξης του lap
    private OffsetDateTime startTime;
    // Track points που ανήκουν στο lap
    private List<Track> tracks = new ArrayList<>();

    // Constructor
    public Lap(OffsetDateTime startTime){
        this.startTime = startTime;
    }


    // -------- Getters -------

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
