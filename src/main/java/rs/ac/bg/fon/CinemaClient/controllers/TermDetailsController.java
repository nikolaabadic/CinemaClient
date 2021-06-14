package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.List;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermDetails;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermSearch;
import rs.ac.bg.fon.CinemaClient.forms.components.tables.FrmTermDetailsTableModel;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nikola
 */
public class TermDetailsController {
    private final FrmTermDetails formDetails;
    private final FrmTermSearch formSearch;
    private Term term;
    private TermSearchController controller;
    private TermDetailsController detailsController = this;

    public TermDetailsController(FrmTermDetails formDetails, FrmTermSearch formSearch, TermSearchController controller, Term term ) {
        this.formDetails = formDetails;
        this.formSearch = formSearch;
        this.term=term;
        this.controller = controller;
        addActionListeners();
    }
    
    public void openForm(){
        setUpForm();        
    }
    
    public void setTerm(Term term){
        try {
            this.term = term;
            loadForm();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                formDetails,
                "Error loading term!\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );             
        }
    }

    public void setUpForm() {
        try {
            loadForm();            
            JOptionPane.showMessageDialog(
                formDetails,
                "Term loaded!",
                "Terms", JOptionPane.INFORMATION_MESSAGE
            );   
            formDetails.setVisible(true);
        }catch(SocketException ex){
            JOptionPane.showMessageDialog(
                formDetails,
                ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
            MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, formDetails);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                formDetails,
                "Error loading term!\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );  
        }
    }
    
    public void addActionListeners(){
        formDetails.btnDeleteAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Communication.getInstance().deleteTerm(term);
                    JOptionPane.showMessageDialog(
                        formDetails,
                        "Term deleted successfully!",
                        "Terms", JOptionPane.INFORMATION_MESSAGE
                    );
                    refreshTable();
                    formDetails.dispose();
                }catch(SocketException ex){
                    JOptionPane.showMessageDialog(
                    formDetails,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
                    );
                    MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, formDetails);
                }catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        formDetails,
                        "Error deleting term!\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                    ); 
                }
            }
        });
        formDetails.btnEditAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openEditTermForm((FrmMain)formSearch.getParent(),formSearch, formDetails, controller, detailsController, term);
            }
        });
    }
    private void refreshTable() throws Exception{
        String searchValue = controller.getSearchValue();
        
        switch(searchValue){
            case "hall": controller.refreshTableByHall(); break;
            case "date": controller.refreshTableByDate(); break;
            case "title": controller.refreshTableByTitle(); break;
        }
    }
    
    private void loadForm() throws Exception {
        List<Reservation> reservations = Communication.getInstance().getReservations(term.getTermID());
        FrmTermDetailsTableModel model = new FrmTermDetailsTableModel(reservations);
        formDetails.getTblTable().setModel(model);

        formDetails.getTxtTitle().setText(term.getFilm().getName());
        formDetails.getTxtHall().setText(term.getHall().getName());
        formDetails.getTxtType().setText(term.getProjectionType());
        formDetails.getTxtDate().setText(String.valueOf(term.getDate()));

        formDetails.getTxtTitle().setEditable(false);
        formDetails.getTxtType().setEditable(false);
        formDetails.getTxtHall().setEditable(false);
        formDetails.getTxtDate().setEditable(false);        
    }
}

