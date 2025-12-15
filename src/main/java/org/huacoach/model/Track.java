package org.huacoach.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  Η κλάση αυτή αναπαριστά ένα track, δηλαδή μια ακολουθία από track points.
 */
public class Track {

    // Λίστα με τα track points του track
    private List<Trackpoint> trackpointList = new ArrayList<>();

    // Προσθέτει track point στο track
    public void addTrackpoint(Trackpoint tp){
        trackpointList.add(tp);
    }

    // --------- Getters ---------
    public List<Trackpoint> getTrackpoint(){
        return  trackpointList;
    }

    public Trackpoint getIndexTrackpoint(int i){
        return trackpointList.get(i);
    }
}
