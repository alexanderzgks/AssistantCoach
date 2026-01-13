package org.huacoach.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * TcxFileRepository
 *
 * Αποθηκεύει τα αρχεία τύπου .tcx
 * τα οποία έχει επιλέξει ή χρησιμοποιήσει ο χρήστης
 * κατά την εκτέλεση της εφαρμογής.
 */
public class TcxFileRepository {

    /**
     * Λίστα που περιέχει όλα τα .tcx αρχεία
     * που έχουν επιλεγεί από τον χρήστη.
     */
    private static final List<File> tcxFiles = new ArrayList<>();

    /**
     * Επιστρέφει τη λίστα των αποθηκευμένων .tcx αρχείων.
     *
     * @return λίστα File
     */
    public List<File> getTcxFiles(){
        return tcxFiles;
    }

    /**
     * Προσθέτει ένα νέο .tcx αρχείο στο repository.
     *
     * @param newTcxFile το αρχείο που επιλέχθηκε από τον χρήστη
     */
    public void setTcxFiles(File newTcxFile){
        tcxFiles.add(newTcxFile);
    }
}
