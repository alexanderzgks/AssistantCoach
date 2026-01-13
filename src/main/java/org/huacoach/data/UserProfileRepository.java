package org.huacoach.data;

import org.huacoach.enums.Sex;
import org.huacoach.model.UserProfile;

/**
 * UserProfileRepository
 *
 * Repository κλάση υπεύθυνη για την αποθήκευση και
 * διαχείριση του UserProfile της εφαρμογής.
 * Η εφαρμογή υποθέτει single-user περιβάλλον,
 * επομένως το repository διατηρεί ένα μόνο profile.
 */
public class UserProfileRepository {

    /**
     * Το τρέχον προφίλ χρήστη της εφαρμογής.
     *
     * Αρχικοποιείται με προεπιλεγμένες τιμές ώστε
     * να αποφεύγεται null κατάσταση.
     */
    private UserProfile profile = new UserProfile("Username", Sex.NOT_SPECIFIED, 0, 0.0);

    /**
     * Επιστρέφει το τρέχον UserProfile.
     *
     * @return το αποθηκευμένο UserProfile
     */
    public UserProfile getProfile(){ return profile; }

    /**
     * Αντικαθιστά το τρέχον UserProfile με νέο.
     *
     * Χρησιμοποιείται όταν ο χρήστης αποθηκεύει
     * αλλαγές μέσω του ProfileDialog.
     *
     * @param profile το νέο UserProfile
     */
    public void setProfile(UserProfile profile){ this.profile = profile; }

}
