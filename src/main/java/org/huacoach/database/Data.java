package org.huacoach.database;

import org.huacoach.model.profile.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {

    private List<String> usernames = new ArrayList<>();
    private HashMap<String, UserProfile> accounts = new HashMap<>();

    /**
     *  Αποθηκεύει τα account του χρήστη στη βάση της εφαρμογής.
     *
     *  @param username είναι το όνομα του χρήστη.
     *  @param user είναι μια η οποία περιέχει τα προσωπικά στοιχεία του χρήστη αλλά
     *                  και όλα τα δεδομένα που έχουν διαβαστεί από τα αρχεία tcx.
     */
    public void addToDatabase(String username, UserProfile user){
        usernames.add(username);
        accounts.put(username, user);
        System.out.println("The save was completed.");
    }

    /**
     *  Παραδίδει τo αντικείμενο με τα προσωπικά στοιχεία αλλά και τα δεδομένα από τα αρχεία
     *  του χρήστη με το ανάλογο username.
     *
     *  @param username όνομα χρήστη
     *  @return το αντικείμενο της κλάσεις UserInfo
     */
    public UserProfile getUserInfo(String username){
        return accounts.get(username);
    }

    /**
     *  Μια μέθοδος που χρησιμοποιείται για να ελέγχει αν το username με το οποίο πάει να
     *  κάνει την εγγραφή χρησιμοποιείται από άλλο χρήστη.
     *
     *  @param username όνομα χρήστη.
     *  @return false αν δεν περιέχεται
     *          ή true αν περιέχεται.
     */
    public boolean checkIfTheUsernameExist(String username){
        if(usernames.contains(username)){
            return true;
        }
        return false;
    }

}
