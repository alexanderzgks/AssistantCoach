package org.huacoach.parser;

import org.huacoach.interfaces.SportType;
import org.huacoach.model.XMLmodels.XMLActivity;
import org.huacoach.model.XMLmodels.Lap;
import org.huacoach.model.XMLmodels.Track;
import org.huacoach.model.XMLmodels.Trackpoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.time.OffsetDateTime;

public class TcxFileParser {

    OffsetDateTime time;
    Double lat = null, lon = null, alt = null, dist = null;
    Integer hr = null, cad = null;

    public XMLActivity readXMLFile(String fileName){

        XMLActivity activityModel = new XMLActivity();

        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new FileInputStream(fileName));
            doc.getDocumentElement().normalize();
            NodeList activityNodes = doc.getElementsByTagName("Activity");
            for (int i = 0; i < activityNodes.getLength(); i++) {
                //Activity
                Element activityElement = (Element) activityNodes.item(i);
                //Sports
                String sportAttr = activityElement.getAttribute("Sport");
                SportType sport = mapSportType(sportAttr);
                Element firstLapElement = (Element) activityElement.getElementsByTagName("Lap").item(0);
                time = OffsetDateTime.parse(firstLapElement.getAttribute("StartTime"));
                activityModel = new XMLActivity(sport, time);
                //Lap
                NodeList lapNodes = activityElement.getElementsByTagName("Lap");
                for (int j = 0; j < lapNodes.getLength(); j++) {
                    //Laps
                    Element lapElement = (Element) lapNodes.item(j);
                    time = OffsetDateTime.parse(lapElement.getAttribute("StartTime"));
                    Lap lap = new Lap(time);
                    // Tracks
                    NodeList trackNodes = lapElement.getElementsByTagName("Track");
                    for (int k = 0; k < trackNodes.getLength(); k++) {
                        //Tracks
                        Element trackElement = (Element) trackNodes.item(k);
                        Track track = new Track();
                        // Trackpoints
                        NodeList tpNodes = trackElement.getElementsByTagName("Trackpoint");
                        for (int m = 0; m < tpNodes.getLength(); m++) {
                            Element tpElement = (Element) tpNodes.item(m);
                            time = OffsetDateTime.parse(getText(tpElement, "Time"));
                            Element posElem = (Element) tpElement.getElementsByTagName("Position").item(0);
                            if (posElem != null) {
                                lat = Double.parseDouble(
                                        getText(posElem, "LatitudeDegrees"));
                                lon = Double.parseDouble(
                                        getText(posElem, "LongitudeDegrees"));
                            }
                            String altStr = getTextOrNull(tpElement, "AltitudeMeters");
                            if (altStr != null) alt = Double.parseDouble(altStr);
                            String distStr = getTextOrNull(tpElement, "DistanceMeters");
                            if (distStr != null) dist = Double.parseDouble(distStr);
                            Element hrElem = (Element) tpElement.getElementsByTagName("HeartRateBpm").item(0);
                            if (hrElem != null) {
                                String hrVal = getText(hrElem, "Value");
                                hr = Integer.parseInt(hrVal);
                            }
                            String cadStr = getTextOrNull(tpElement, "Cadence");
                            if (cadStr != null) cad = Integer.parseInt(cadStr);
                            Trackpoint tp = new Trackpoint(
                                    time, lat, lon, alt, dist, hr, cad
                            );
                            track.addTrackpoint(tp);
                        }

                        lap.addTracks(track);
                    }

                    activityModel.addLaps(lap);
                }
            }
            return activityModel;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    private SportType mapSportType(String sportAttr) {
        return switch (sportAttr.toLowerCase()) {
            case "running" -> SportType.RUNNING;
            case "biking", "cycling" -> SportType.CYCLING;
            case "walking" -> SportType.WALKING;
            case "swimming" -> SportType.SWIMMING;
            default -> SportType.OTHER;
        };
    }

    private String getText(Element parent, String tag) {
        return ((Element) parent.getElementsByTagName(tag).item(0)).getTextContent();
    }

    private String getTextOrNull(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() == 0) return null;
        return ((Element) list.item(0)).getTextContent();
    }
}
