package org.huacoach.ui.menu;

import org.huacoach.interfaces.AboutActions;
import org.huacoach.interfaces.FileActions;

import javax.swing.*;

public class MenuController {

    private final JFrame frame;
    private final FileActions fileActions;
    private final ProfileDialog profileDialog;
    private final AboutActions aboutActions;


    public MenuController(JFrame frame, FileActions fileActions,ProfileDialog profileDialog ,AboutActions aboutActions){
        this.frame = frame;
        this.fileActions = fileActions;
        this.profileDialog = profileDialog;
        this.aboutActions = aboutActions;
        this.frame.setJMenuBar(createMenuBar());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openNewFile = new JMenuItem("Open File");
        JMenuItem seeTheFileTheProgramUse = new JMenuItem("Using Files");
        openNewFile.addActionListener(e -> openTcxFiles());
        seeTheFileTheProgramUse.addActionListener(e -> seeUseFiles());
        fileMenu.add(openNewFile);
        fileMenu.add(seeTheFileTheProgramUse);

        JMenu profile = new JMenu("Profile");
        JMenuItem editProfile = new JMenuItem("My Profile");
        editProfile.addActionListener(e->editProfile());
        profile.add(editProfile);

        JMenu aboutMenu = new JMenu("About");
        JMenuItem creators = new JMenuItem("Creators");
        JMenuItem info = new JMenuItem("Info");
        creators.addActionListener(e->seeCreators());
        info.addActionListener(e->seeInfo());
        aboutMenu.add(creators);
        aboutMenu.add(info);

        menuBar.add(fileMenu);
        menuBar.add(profile);
        menuBar.add(aboutMenu);
        return menuBar;
    }

    private void openTcxFiles(){
        fileActions.openFiles();
    }

    private void seeUseFiles(){
        fileActions.useFiles();
    }


    private void editProfile(){
        profileDialog.setVisible(true);
    }



    private void seeCreators(){
        aboutActions.getCreators();
    }

    private void seeInfo(){
        aboutActions.getInfo();
    }
}
