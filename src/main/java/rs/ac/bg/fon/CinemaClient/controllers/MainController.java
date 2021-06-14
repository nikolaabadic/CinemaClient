package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;

/**
 *
 * @author Nikola
 */
public class MainController {
    private final FrmMain frmMain;
    
    public MainController(FrmMain frmMain){
        this.frmMain=frmMain;
        addActionListener();
    }
    
    public void openForm(){
        this.frmMain.setVisible(true);
    }
    
    private void addActionListener() {
        frmMain.miFilmCreateAddActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openCreateFilmForm(frmMain);
            }
        });
        frmMain.miFilmSearchAddActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchFilmForm(frmMain);
            }
            
        });
        frmMain.miTermCreateAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openCreateTermForm(frmMain);
            }
        });
        frmMain.miTermSearchAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchTermForm(frmMain);
            }
        });
        
        frmMain.miReservationCreateAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openCreateReservationForm(frmMain);
            }
        });
        frmMain.miReservationDeleteAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchReservationForm(frmMain);
            }
        });
        frmMain.miFilmDeleteAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchFilmForm(frmMain);
            }
        });
        frmMain.miTermEditAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchTermForm(frmMain);
            }
        });
        frmMain.miTermDeleteAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchTermForm(frmMain);
            }
        });        
    }
    
    
}

