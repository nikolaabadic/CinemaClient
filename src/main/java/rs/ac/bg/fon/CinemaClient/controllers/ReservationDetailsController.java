package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.FrmReservationDetails;
import rs.ac.bg.fon.CinemaClient.forms.FrmReservationSearch;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;

/**
 *
 * @author Nikola
 */
public class ReservationDetailsController {
    private final FrmReservationDetails formDetails;
    private final FrmReservationSearch formSearch;
    private final Reservation reservation;
    private ReservationSearchController controller;

    public ReservationDetailsController(FrmReservationDetails formDetails, FrmReservationSearch formSearch, ReservationSearchController controller, Reservation reservation) {
        this.formDetails = formDetails;
        this.formSearch = formSearch;
        this.reservation = reservation;
        this.controller = controller;
        addActionListener();
    }
    
    public void openForm(){
        setUpForm();
        formDetails.setVisible(true);
    }

    private void setUpForm() {
        JOptionPane.showMessageDialog(
            formDetails,
            "Reservation loaded!",
            "Reservations", JOptionPane.INFORMATION_MESSAGE
        ); 
        formDetails.getTxtUser().setText(reservation.getUser().getUsername());
        formDetails.getTxtTerm().setText(reservation.getTerm().toString());
        formDetails.getTxtNumber().setText(String.valueOf(reservation.getNumberOfTickets()));
        formDetails.getTxtDate().setText(String.valueOf(reservation.getReservationDate()));
        
        formDetails.getTxtUser().setEditable(false);
        formDetails.getTxtTerm().setEditable(false);
        formDetails.getTxtNumber().setEditable(false);
        formDetails.getTxtDate().setEditable(false);        
    }
    
    public void addActionListener(){
        formDetails.btnDeleteAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Communication.getInstance().deleteReservation(reservation);
                    formDetails.dispose();
                    JOptionPane.showMessageDialog(
                        formDetails,
                        "Reservation deleted successfully!",
                        "Reservation", JOptionPane.INFORMATION_MESSAGE
                    );
                    refreshTable();
                }catch(SocketException ex){
                    JOptionPane.showMessageDialog(
                            formDetails,
                            ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                    MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, formDetails);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        formDetails,
                        "Error deleting reservation!\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
    
    private void refreshTable() throws Exception{
        String searchValue = controller.getSearchValue();
        
        switch(searchValue){
            case "username": controller.loadTableByUsername(); break;
            case "term": controller.loadTableByTerm(); break;
            case "date": controller.loadTableByDate(); break;
        }
    }
}

