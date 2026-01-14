package org.huacoach.ui;

import org.huacoach.data.UserProfileRepository;
import org.huacoach.ui.menu.AboutController;
import org.huacoach.ui.menu.MenuController;
import org.huacoach.ui.menu.ProfileDialog;
import org.huacoach.ui.menu.TcxFileController;
import org.huacoach.ui.tabs.TabController;

import javax.swing.*;

/**
 * MainFrame
 *
 * Κύρια κλάση UI της εφαρμογής Assistant Coach.
 * Είναι υπεύθυνη για:
 * - τη δημιουργία και βασική ρύθμιση του κύριου JFrame
 * - το "δέσιμο" (wiring) των βασικών controllers (Menu / Tabs)
 */

public class MainFrame {

    // Το κύριο παράθυρο της εφαρμογής
    private static JFrame frame;

    /** Controller για λειτουργίες σχετικές με .tcx αρχεία (Open/Using Files) */
    private final TcxFileController tcxFileController;

    private final ProfileDialog profileDialog;

    /** Controller για λειτουργίες "About" (Creators/Info) */
    private final AboutController aboutController;

    /** Repository που διατηρεί το UserProfile (in-memory) */
    private final UserProfileRepository userProfileRepository;

    /**
     * Constructor της κλάσης.
     *
     * Δημιουργεί το JFrame και στη συνέχεια δημιουργεί/συνδέει
     * τους βασικούς controllers της εφαρμογής (menu + tabs).
     */
    public MainFrame(){
        createFrame();

        // Δημιουργούμε τα βασικά shared objects μία φορά
        tcxFileController = new TcxFileController(frame);
        userProfileRepository = new UserProfileRepository();
        profileDialog = new ProfileDialog(frame, userProfileRepository, userProfileRepository.getProfile());
        aboutController = new AboutController(frame);

        //userProfileRepository, userProfileRepository.getProfile()

        // Συνδέουμε το μενού με τους controllers και το repository
        new MenuController(frame, tcxFileController, profileDialog,aboutController);

        new TabController(frame, userProfileRepository);

        frame.setVisible(true);
    }

    /**
     * Δημιουργεί και ρυθμίζει το κύριο JFrame της εφαρμογής.
     */
    private static void createFrame(){
        frame = new JFrame("Assistant Coach");
        frame.setSize(800,500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
