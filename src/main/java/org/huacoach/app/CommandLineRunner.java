package org.huacoach.app;

import org.huacoach.parser.TcxFileParser;
import org.huacoach.model.XMLmodels.*;

import java.lang.annotation.Target;
import java.util.List;

public class CommandLineRunner {
    private TcxFileParser tcxFileParser = new TcxFileParser();
    private List<ActivityModel> activityModelList;
    private ActivityModel activityModel;
    private Lap lap;
    private Track track;
    private Trackpoint trackpoint;

    public void runTest(){
        activityModelList = tcxFileParser.readXMLFile("activity.xml");
        for(int i = 0; i < activityModelList.size(); i++){
            ActivityModel activityModel = activityModelList.get(i);
            lap = activityModel.getIndexLap(i);
            track = lap.getIndexTrack(i);
            trackpoint = track.getIndexTrackpoint(i);
            System.out.println(trackpoint.toString());
        }
    }
}