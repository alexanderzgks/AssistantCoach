package org.huacoach.app;

import org.huacoach.ui.MainFrame;

import javax.swing.*;

/**
 * Entry point της εφαρμογής Assistant Coach.
 * Εκκινεί το UI δημιουργώντας το MainFrame.
 */
public class NewMain {

//    public static void main(String[] args){
//        // Εκκίνηση του γραφικού περιβάλλοντος
//        MainFrame UI = new MainFrame();
//    }

    public static void main(String[] args) {

        // Εκκίνηση του Swing UI στο Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
