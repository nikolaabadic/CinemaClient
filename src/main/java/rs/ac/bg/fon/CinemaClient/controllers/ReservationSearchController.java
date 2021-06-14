package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.FrmReservationSearch;
import rs.ac.bg.fon.CinemaClient.forms.components.tables.FrmReservationSearchTableModel;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

/**
 *
 * @author Nikola
 */
public class ReservationSearchController {
    private final FrmReservationSearch formSearch;
    private ReservationSearchController controller = this;
    private String searchValue = "username";

    public ReservationSearchController(FrmReservationSearch formSearch) {
        this.formSearch = formSearch;
        addActionListeners();
    }
    
    public FrmReservationSearch getForm(){
        return formSearch;
    }
    
    public void openForm(){
        try {
            setUpForm();
            formSearch.setVisible(true);
        }catch(SocketException ex){
            JOptionPane.showMessageDialog(
                formSearch,
                ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
            MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                formSearch,
                "Error loading form!\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void setUpForm() throws Exception {
        List<Reservation> reservations = Communication.getInstance().getReservations();
        formSearch.getCbTerms().removeAllItems();
        List<Term> terms = Communication.getInstance().getTerms();
        for(Term t : terms){
            formSearch.getCbTerms().addItem(t);
        }

        formSearch.getTblTable().setModel(new FrmReservationSearchTableModel(reservations));
    }
    
    public void addActionListeners(){
        formSearch.btnSearchTermAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formSearch.getTxtUsername().setText("");
                formSearch.getTxtDate().setText("");
                searchValue = "term";
                try {
                    List<Reservation> reservations = loadTableByTerm();
                    
                    if(reservations!=null && !reservations.isEmpty()){
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "Reservations found!",
                            "Reservations", JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "No reservations found!",
                            "Reservations", JOptionPane.ERROR_MESSAGE
                        );                        
                    }                    
                }catch(SocketException ex){
                    JOptionPane.showMessageDialog(
                            formSearch,
                            ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                    MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        formSearch,
                        "Error searching reservations.\n" + ex.getMessage(),
                        "Reservations", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        formSearch.btnSearchUsernameAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formSearch.getTxtDate().setText("");
                searchValue = "username";
                try {
                    List<Reservation> reservations = loadTableByUsername();
                    
                    if(reservations!=null && !reservations.isEmpty()){
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "Reservations found!",
                            "Reservations", JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "No reservations found!",
                            "Reservations", JOptionPane.ERROR_MESSAGE
                        );                        
                    }
                    
                }catch(SocketException ex){
                    JOptionPane.showMessageDialog(
                            formSearch,
                            ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                    MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        formSearch,
                        "Error searching reservations.\n" + ex.getMessage(),
                        "Reservations", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        formSearch.btnSearchDateAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formSearch.getTxtUsername().setText("");
                searchValue = "date";
                try {
                    List<Reservation> reservations = loadTableByDate();
                    
                    if(reservations!=null && !reservations.isEmpty()){
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "Reservations found!",
                            "Reservations", JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "No reservations found!",
                            "Reservations", JOptionPane.ERROR_MESSAGE
                        );                        
                    }
                    
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            formSearch,
                            "Reservation date format must be: dd.MM.yyyy!\n"+ex.getMessage(),
                            "Error!", JOptionPane.ERROR_MESSAGE
                    );                    
                }catch(SocketException ex){
                    JOptionPane.showMessageDialog(
                            formSearch,
                            ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                    MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, null);
                }catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        formSearch,
                        "Error searching reservations.\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                    );                     
                }
            }
        });
        formSearch.btnDetailsAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = formSearch.getTblTable().getSelectedRow();
                if(row != -1){
                    FrmReservationSearchTableModel model = (FrmReservationSearchTableModel) formSearch.getTblTable().getModel();
                    Reservation r = model.getList().get(row);
                    
                    try {
                        Reservation dbReservation = Communication.getInstance().getReservationsById(r.getTerm().getTermID(), r.getUser().getUserID());
                        MainCordinator.getInstance().openDetailsReservationForm((FrmMain)formSearch.getParent(),formSearch, controller, dbReservation);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "Error loading reservation!",
                            "Error!", JOptionPane.ERROR_MESSAGE
                        );
                    }

                } else {
                    JOptionPane.showMessageDialog(
                            formSearch,
                            "No rows selected!",
                            "Error!", JOptionPane.ERROR_MESSAGE
                    );
                }  
            }
        });
    }
    
    public List<Reservation> loadTableByTerm() throws Exception{
        Term t = (Term) formSearch.getCbTerms().getSelectedItem();
        List<Reservation> reservations = Communication.getInstance().getReservations(t.getTermID());        
        
        if(reservations!=null && !reservations.isEmpty()){
            FrmReservationSearchTableModel model = new FrmReservationSearchTableModel(reservations);
            formSearch.getTblTable().setModel(model);
        } else {
            formSearch.getTblTable().setModel(new FrmReservationSearchTableModel(new ArrayList<Reservation>()));
        }
        
        return reservations;
    }
    
    public List<Reservation> loadTableByUsername() throws Exception{
        String username = formSearch.getTxtUsername().getText().trim();
        List<Reservation> reservations;
        
        if(!username.isEmpty()){
            reservations = Communication.getInstance().getReservations(username);
        } else{
            reservations = Communication.getInstance().getReservations();
        }
        
        if(reservations!=null && !reservations.isEmpty()){
            FrmReservationSearchTableModel model = new FrmReservationSearchTableModel(reservations);
            formSearch.getTblTable().setModel(model);
        } else {
            formSearch.getTblTable().setModel(new FrmReservationSearchTableModel(new ArrayList<Reservation>()));
        }
        
        return reservations;
    }
    
    public List<Reservation> loadTableByDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = formSearch.getTxtDate().getText().trim();
        List<Reservation> reservations = Communication.getInstance().getReservationsByDate(date);
        
        if(reservations!=null && !reservations.isEmpty()){
            FrmReservationSearchTableModel model = new FrmReservationSearchTableModel(reservations);
            formSearch.getTblTable().setModel(model);
        } else {
            formSearch.getTblTable().setModel(new FrmReservationSearchTableModel(new ArrayList<Reservation>()));
        }
                
        return reservations;
    }
    
    public String getSearchValue(){
        return searchValue;
    }
}

