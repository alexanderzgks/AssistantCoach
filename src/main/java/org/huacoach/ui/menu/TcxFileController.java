package org.huacoach.ui.menu;

import org.huacoach.data.TcxFileRepository;
import org.huacoach.interfaces.FileActions;
import org.huacoach.services.ActivityFileService;
import javax.swing.*;
import java.io.File;

public class TcxFileController extends TcxFileRepository implements FileActions {

    // Το βασικό JFrame της εφαρμογής
    private final JFrame frame;

    // Service υπεύθυνο για parsing και φόρτωση δραστηριοτήτων
    private final ActivityFileService activityFileService = new ActivityFileService();


    /**
     * Constructor που δέχεται το βασικό frame της εφαρμογής.
     *
     * @param frame το κύριο JFrame
     */
    public TcxFileController(JFrame frame){
        this.frame = frame;
    }


    /**
     * Ανοίγει JFileChooser ώστε ο χρήστης να επιλέξει αρχείο .tcx.
     * Αν η επιλογή εγκριθεί:
     * - το αρχείο αποθηκεύεται στο repository
     * - φορτώνεται μέσω ActivityFileService
     * - εμφανίζεται ενημερωτικό μήνυμα στον χρήστη
     */
    @Override
    public void openFiles(){
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if(getTcxFiles().contains(selectedFile)){
                JOptionPane.showMessageDialog(
                        frame,
                        "Το αρχείο αυτό υπάρχει ήδη:\n" + selectedFile.getName()
                );
                return;
            }
            // Αποθήκευση αρχείου στο repository
            setTcxFiles(selectedFile);

            // Φόρτωση δραστηριότητας από το αρχείο
            activityFileService.loadActivityFromFile(
                    selectedFile.getAbsolutePath()
            );

            JOptionPane.showMessageDialog(
                    frame,
                    "Άνοιξε το αρχείο:\n" + selectedFile.getName()
            );
        }
    }

    /**
     * Εμφανίζει dialog με όλα τα αρχεία .tcx
     * που έχουν χρησιμοποιηθεί μέχρι στιγμής.
     */
    @Override
    public void useFiles(){
        if (getTcxFiles().isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Δεν έχουν χρησιμοποιηθεί αρχεία ακόμα."
            );
            return;
        }

        StringBuilder message = new StringBuilder("Χρησιμοποιημένα αρχεία:\n\n");

        for (File file : getTcxFiles()) {
            message.append(file.getName()).append("\n");
        }

        JOptionPane.showMessageDialog(
                frame,
                message.toString(),
                "Using Files",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

}
