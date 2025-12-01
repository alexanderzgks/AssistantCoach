package org.huacoach.model;

import java.util.ArrayList;
import java.util.List;

public class Track {

    private List<Trackpoint> trackpointList = new ArrayList<>();

    public void addTrackpoint(Trackpoint tp){
        trackpointList.add(tp);
    }

    public List<Trackpoint> getTrackpoint(){
        return  trackpointList;
    }

    public Trackpoint getIndexTrackpoint(int i){
        return trackpointList.get(i);
    }
}
