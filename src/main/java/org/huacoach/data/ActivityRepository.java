package org.huacoach.data;

import org.huacoach.model.XMLActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * ActivityRepository
 *
 * Η κλάση λειτουργεί ως κεντρικό σημείο αποθήκευσης
 * των δραστηριοτήτων κατά την εκτέλεση της εφαρμογής.
 *
 */
public class ActivityRepository {

    /**
     * Λίστα που περιέχει όλες τις δραστηριότητες
     * που έχουν φορτωθεί επιτυχώς.
     */
    private static final List<XMLActivity> activities = new ArrayList<>();

    /**
     * Επιστρέφει όλες τις αποθηκευμένες δραστηριότητες.
     *
     * @return λίστα XMLActivity
     */
    public List<XMLActivity> getTheActivities() {
        return activities;
    }


    /**
     * Προσθέτει μία νέα δραστηριότητα στο repository.
     *
     * @param activity το XMLActivity που προέκυψε από parsing αρχείου
     */
    public void setActivities(XMLActivity activity){
        activities.add(activity);
    }
}
