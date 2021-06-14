package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.FrmReservationCreate;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaCommon.domain.User;

/**
 *
 * @author Nikola
 */
public class ReservationCreateController {
    private final FrmReservationCreate formCreate;

    public ReservationCreateController(FrmReservationCreate formCreate) {
        this.formCreate = formCreate;
        addActionListeners();
    }
    
    public void openForm(){
        try{            
            setUpForm();
            formCreate.setVisible(true);
        } catch(SocketException ex){
            JOptionPane.showMessageDialog(
                    formCreate,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            MainCordinator.getInstance().closeAllForms((FrmMain) formCreate.getParent(), formCreate, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                formCreate,
                "Error loading form\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void setUpForm() throws Exception {
        formCreate.getTxtNumber().setText("");
        formCreate.getTxtDate().setText("");
        List<User> users = Communication.getInstance().getUsers();
        List<Term> terms = Communication.getInstance().getTerms();

        formCreate.getCbUsername().removeAllItems();
        for(User u: users){
            formCreate.getCbUsername().addItem(u);
        }
        formCreate.getCbTerm().removeAllItems();
        for(Term t: terms){
            formCreate.getCbTerm().addItem(t);
        }
    }
    
    public void addActionListeners(){
        formCreate.btnCreateAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(formCreate.getTxtNumber().getText().isEmpty() || formCreate.getTxtDate().getText().isEmpty()){
                        throw new Exception("All fields are required!");
                    }
                    
                    Term term =(Term) formCreate.getCbTerm().getSelectedItem();
                    User user = (User)formCreate.getCbUsername().getSelectedItem();
                    int number = Integer.parseInt(formCreate.getTxtNumber().getText().trim());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    Date date = sdf.parse(formCreate.getTxtDate().getText().trim());
                                    
                    Communication.getInstance().addReservation(term,user,number,date);
                    
                    JOptionPane.showMessageDialog(
                        formCreate,
                        "Reservation created!",
                        "Reservation", JOptionPane.INFORMATION_MESSAGE
                    );                 
                   
                    setUpForm();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        formCreate,
                        "Error parsing date! Date format must be dd.MM.yyyy hh:mm!",
                        "Error", JOptionPane.ERROR_MESSAGE
                    );
                }catch(SocketException ex){
                    JOptionPane.showMessageDialog(
                            formCreate,
                            ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                    MainCordinator.getInstance().closeAllForms((FrmMain) formCreate.getParent(), formCreate, null);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        formCreate,
                        "Error creating reservation!\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        formCreate.btnCancelAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formCreate.dispose();
            }
        });
    }
}

