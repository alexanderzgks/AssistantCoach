package org.huacoach.parser;

import org.huacoach.enums.SportType;
import org.huacoach.model.XMLActivity;
import org.huacoach.model.Lap;
import org.huacoach.model.Track;
import org.huacoach.model.Trackpoint;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.OffsetDateTime;

public class TcxFileParser {

    /**
     * Διαβάζει ένα TCX αρχείο και το μετατρέπει στο εσωτερικό μας μοντέλο (XMLActivity).
     * Χρησιμοποιούμε DOM parsing γιατί θέλουμε εύκολη πλοήγηση στη δομή:
     * Activity -> Lap -> Track -> Trackpoint.
     */
    public XMLActivity readXMLFile(String fileName) {

        // Χρησιμοποιήσαμε το try καθώς ανοίγει το αρχείο, αν υπάρχει,
        // και κλείνει μόνο του.
        try (InputStream is = new FileInputStream(fileName)) {

            // Δημιουργία DOM parser (DocumentBuilder).
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            // Επιτρέπει στον parser να χειρίζεται σωστά XML με namespaces
            // έτσι ώστε σε περίπτωση που έχει να μην αγνοεί τα tags
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            /*
             * Διαβάζει το tcx αρχείο και το κάνει σε DOM tree
             * Αυτό το κάναμε καθώς θέλαμε η αρχιτεκτονική του αρχείου (model) να είναι
             * παρόμοια με αυτή του προγράμματος μας.
             */
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            // -------------------- Εντοπισμός Activity --------------------

            // Βρίσκουμε όλα τα <Activity> ανεξάρτητα από namespace (local name = "Activity").
            // Αν δεν βρεθεί τίποτα, κάνουμε fallback σε αναζήτηση χωρίς namespace.
            NodeList activityNodes = getNodesByLocalName(doc, "Activity");
            if (activityNodes.getLength() == 0) {
                activityNodes = doc.getElementsByTagName("Activity");
            }
            // Aν δε βρεί κανένα Activity
            if (activityNodes.getLength() == 0) {
                throw new IllegalArgumentException("No <Activity> elements found in TCX file: " + fileName);
            }
            // Παίρνει το πρώτο Activity
            Element activityElement = (Element) activityNodes.item(0);

            // -------------------- Metadata Activity (Sport, Id, StartTime) --------------------
            // Διαβάζει τι Sport είναι πχ <Activity Sport="Swimming">...
            String sportAttr = activityElement.getAttribute("Sport");
            SportType sport = mapSportType(sportAttr);

            // Διαβάζει το Id του
            // πχ <Id>2024-01-01T10:00:00Z</Id>
            String id = getTextOrNull(activityElement, "Id");
            if (id == null) id = "";

            // StartTime του πρώτου Lap StartTime, αλλιώς null
            Element firstLapElement = getFirstElement(activityElement, "Lap");
            OffsetDateTime activityTime = null;
            if (firstLapElement != null) {
                String startTime = firstLapElement.getAttribute("StartTime");
                if (startTime != null && !startTime.isBlank()) {
                    activityTime = OffsetDateTime.parse(startTime);
                }
            }

            // Δημιουργούμε το αντικείμενο XML
            XMLActivity activityModel = new XMLActivity(sport, activityTime, id);

            // -------------------- Laps --------------------
            // Βρίσκει όλα τα Laps
            NodeList lapNodes = getNodesByLocalName(activityElement, "Lap");
            if (lapNodes.getLength() == 0) {
                lapNodes = activityElement.getElementsByTagName("Lap");
            }

            // Για κάθε Lap: διαβάζουμε StartTime και χτίζουμε τη δομή Track -> Trackpoint.
            for (int j = 0; j < lapNodes.getLength(); j++) {
                Element lapElement = (Element) lapNodes.item(j);
                OffsetDateTime lapTime = null;
                String lapStart = lapElement.getAttribute("StartTime");
                if (lapStart != null && !lapStart.isBlank()) {
                    lapTime = OffsetDateTime.parse(lapStart);
                }
                Lap lap = new Lap(lapTime);

                // -------------------- Tracks --------------------
                NodeList trackNodes = getNodesByLocalName(lapElement, "Track");
                if (trackNodes.getLength() == 0) {
                    trackNodes = lapElement.getElementsByTagName("Track");
                }

                for (int k = 0; k < trackNodes.getLength(); k++) {
                    Element trackElement = (Element) trackNodes.item(k);
                    Track track = new Track();

                    // -------------------- Trackpoints --------------------
                    NodeList tpNodes = getNodesByLocalName(trackElement, "Trackpoint");
                    if (tpNodes.getLength() == 0) {
                        tpNodes = trackElement.getElementsByTagName("Trackpoint");
                    }

                    for (int m = 0; m < tpNodes.getLength(); m++) {
                        Element tpElement = (Element) tpNodes.item(m);
                        OffsetDateTime tpTime = null;
                        Double tpLat = null, tpLon = null, tpAlt = null, tpDist = null;
                        Integer tpHr = null, tpCad = null;

                        // Διαβάζει όλα τα δεδομένα
                        // ------ Time ------
                        String timeStr = getTextOrNull(tpElement, "Time");
                        if (timeStr != null && !timeStr.isBlank()) {
                            tpTime = OffsetDateTime.parse(timeStr);
                        }

                        // ------ Position ------
                        Element posElem = getFirstElement(tpElement, "Position");
                        if (posElem != null) {
                            tpLat = parseDoubleOrNull(getTextOrNull(posElem, "LatitudeDegrees"));
                            tpLon = parseDoubleOrNull(getTextOrNull(posElem, "LongitudeDegrees"));
                        }

                        // ------Altitude / Distance / Cadence ------
                        tpAlt = parseDoubleOrNull(getTextOrNull(tpElement, "AltitudeMeters"));
                        tpDist = parseDoubleOrNull(getTextOrNull(tpElement, "DistanceMeters"));
                        tpCad = parseIntOrNull(getTextOrNull(tpElement, "Cadence"));

                        // ------ HeartRateBpm/Value------
                        Element hrElem = getFirstElement(tpElement, "HeartRateBpm");
                        if (hrElem != null) {
                            tpHr = parseIntOrNull(getTextOrNull(hrElem, "Value"));
                        }

                        // Δημιουργεί Trackpoint και προσθήκη στο Track
                        Trackpoint tp = new Trackpoint(tpTime, tpLat, tpLon, tpAlt, tpDist, tpHr, tpCad);
                        track.addTrackpoint(tp);
                    }

                    // Προσθέτουμε το Track στο Lap.
                    lap.addTracks(track);
                }

                // Προσθέτουμε το Lap στο Activity.
                activityModel.addLaps(lap);
            }

            // Επιστρέφουμε το χτισμένο μοντέλο Activity.
            return activityModel;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Μετατρέπει το Sport attribute του TCX (String) σε enum SportType.
    private SportType mapSportType(String sportAttr) {
        if (sportAttr == null) return SportType.OTHER;
        return switch (sportAttr.toLowerCase()) {
            case "running" -> SportType.RUNNING;
            case "biking", "cycling" -> SportType.CYCLING;
            case "walking" -> SportType.WALKING;
            case "swimming" -> SportType.SWIMMING;
            default -> SportType.OTHER;
        };
    }

    // -------- Βοηθητικές κλάσεις --------

    // Επιστρέφουμε nodes με localName σε όλο το Document, αγνοώντας namespace.
    private NodeList getNodesByLocalName(Document doc, String localName) {
        return doc.getElementsByTagNameNS("*", localName);
    }

    // Επιστρέφουμε nodes με localName μέσα σε συγκεκριμένο Element, αγνοώντας namespace.
    private NodeList getNodesByLocalName(Element elem, String localName) {
        return elem.getElementsByTagNameNS("*", localName);
    }

    // Επιστρέφει το πρώτο Element για tag. Αν δεν υπάρχει -> null.
    private Element getFirstElement(Element parent, String tag) {
        NodeList list = getNodesByLocalName(parent, tag);
        if (list.getLength() == 0) list = parent.getElementsByTagName(tag);
        if (list.getLength() == 0) return null;
        Node n = list.item(0);
        return (n instanceof Element) ? (Element) n : null;
    }

    // Παίρνει text του πρώτου tag, trim, και επιστρέφει null αν λείπει/είναι κενό.
    private String getTextOrNull(Element parent, String tag) {
        Element el = getFirstElement(parent, tag);
        if (el == null) return null;
        String text = el.getTextContent();
        if (text == null) return null;
        text = text.trim();
        return text.isEmpty() ? null : text;
    }

    // Ασφαλές Double parse: null ή invalid -> null.
    private Double parseDoubleOrNull(String s) {
        if (s == null) return null;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Ασφαλές Integer parse: null ή invalid -> null.
    private Integer parseIntOrNull(String s) {
        if (s == null) return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}