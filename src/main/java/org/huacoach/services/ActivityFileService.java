package org.huacoach.services;

import org.huacoach.data.ActivityRepository;
import org.huacoach.model.XMLActivity;
import org.huacoach.parser.TcxFileParser;


public class ActivityFileService extends ActivityRepository {

    private final TcxFileParser PARSER = new TcxFileParser();

    public void loadActivityFromFile(String Path){
        try { //Διαβάζουμε το αρχείο XML
            XMLActivity activity = PARSER.readXMLFile(Path);
            // Αν διαβάστηκε σωστά, το προσθέτουμε στη λίστα
            if (activity != null) {
                setActivities(activity);
            }
            // System.out.println("OKK");
        } catch (Exception e) {
            // Αν αποτύχει ένα αρχείο, εμφανίζουμε μήνυμα αλλά συνεχίζουμε στα επόμενα
            System.err.println("Failed to read: " + Path);
        }
    }

}
