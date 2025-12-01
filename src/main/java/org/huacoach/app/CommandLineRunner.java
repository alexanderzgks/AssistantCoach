package org.huacoach.app;

import org.huacoach.parser.TcxFileParser;
import org.huacoach.model.XMLmodels.*;


public class CommandLineRunner {
    private TcxFileParser tcxFileParser = new TcxFileParser();
    private ActivityModel activityModel;
    private Lap lap;
    private Track track;
    private Trackpoint trackpoint;



    public void runTest(String[] args){

        for(String j : args){
            activityModel = tcxFileParser.readXMLFile(j);
            for(int i = 0; i < activityModel.getLaps().size(); i++){
                lap = activityModel.getIndexLap(i);
                for(int k = 0; k < lap.getTracks().size(); k++){
                    track = lap.getIndexTrack(k);
                    for(int h = 0; h < track.getTrackpoint().size(); h++){
                        trackpoint = track.getIndexTrackpoint(h);
                        System.out.println(trackpoint.toString());
                    }
                }
            }
        }
    }
}