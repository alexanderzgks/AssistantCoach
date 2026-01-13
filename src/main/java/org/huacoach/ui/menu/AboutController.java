package org.huacoach.ui.menu;

import org.huacoach.interfaces.AboutActions;

import javax.swing.*;


/**
 * AboutController
 *
 * Controller κλάση υπεύθυνη για τις λειτουργίες
 * της ενότητας "About" της εφαρμογής.
 *
 * Υλοποιεί το interface AboutActions και
 * διαχειρίζεται την εμφάνιση πληροφοριών όπως:
 * - οι δημιουργοί της εφαρμογής
 * - γενικές πληροφορίες / οδηγίες χρήσης
 *
 * Η κλάση αυτή ανήκει στο UI layer και
 * επικοινωνεί με τον χρήστη μέσω JOptionPane dialogs.
 */
public class AboutController implements AboutActions {

    /**
     * Στοιχεία δημιουργών της εφαρμογής.
     */
    private final String creatorOne = "it2023017 - Ζόγκας Αλέξανδρος";
    private final String creatorTwo = "it2023053 - Ξυπολίας Κωσταντίνος";
    private final String creatorThree = "it2023131 - Ζακέο Μάρκος Νικόλαος";

    /**
     * Το βασικό JFrame της εφαρμογής.
     * Χρησιμοποιείται ως parent component
     * για τα JOptionPane dialogs.
     */
    private final JFrame frame;

    /**
     * Constructor που δέχεται το κύριο JFrame
     * της εφαρμογής.
     *
     * @param frame το βασικό παράθυρο της εφαρμογής
     */
    public AboutController(JFrame frame){
        this.frame = frame;
    }

    /**
     * Εμφανίζει dialog με τους δημιουργούς της εφαρμογής.
     *
     * Η πληροφορία παρουσιάζεται σε απλό
     * informational JOptionPane.
     */
    @Override
    public void getCreators(){
        StringBuilder message = new StringBuilder("Δημιουργοί:\n\n");
        message.append(creatorOne).append("\n");
        message.append(creatorTwo).append("\n");
        message.append(creatorThree).append("\n\n");
        JOptionPane.showMessageDialog(
                frame,
                message.toString(),
                "Creators",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    /**
     * Εμφανίζει dialog με γενικές πληροφορίες
     * σχετικά με την εφαρμογή.
     *
     * Προς το παρόν παραπέμπει στο αρχείο README.
     */
    @Override
    public void getInfo(){
        StringBuilder message = new StringBuilder("Δες το Readme\n\n");
        JOptionPane.showMessageDialog(
                frame,
                message.toString(),
                "Info",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
