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
import rs.ac.bg.fon.CinemaClient.forms.FrmFilmCreate;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermCreateNewFilm;
import rs.ac.bg.fon.CinemaClient.forms.components.tables.FrmFilmCreateTableModel;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

/**
 *
 * @author Nikola
 */
public class TermCreateNewFilmController {
    private final FrmTermCreateNewFilm formCreate;
    private final FrmFilmCreate formFilmCreate;
    public TermCreateNewFilmController(FrmTermCreateNewFilm formCreate, FrmFilmCreate formFilmCreate) {
        this.formCreate = formCreate;
        this.formFilmCreate=formFilmCreate;
        addActionListeners();
    }
    
    public void openForm(){
        try {
            setUpForm();
            formCreate.setVisible(true);
        }catch(SocketException ex){
            JOptionPane.showMessageDialog(
                formCreate,
                ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
            MainCordinator.getInstance().closeAllForms((FrmMain) formFilmCreate.getParent(), formFilmCreate, formCreate);
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                formCreate,
                "Error loading form!\n"+ex.getMessage(),
                "Error!", JOptionPane.ERROR_MESSAGE
            );  
        }
    }

    private void setUpForm() throws Exception {
        List<Hall> halls = Communication.getInstance().getHalls();
        
        formCreate.getCbHall().removeAllItems();
        for(Hall h : halls){
            formCreate.getCbHall().addItem(h);
        }
    }

    private void addActionListeners() {
        formCreate.btnCancelAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formCreate.dispose();
            }
        });
        formCreate.btnAddTermAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    Hall hall = (Hall) formCreate.getCbHall().getSelectedItem();
               
                    String type =  formCreate.getRb2D().isSelected() ? "2D" : "3D";
                
                    Date date=sdf.parse(formCreate.getTxtDate().getText().trim());
                    
                    Term term = new Term();
                    term.setHall(hall);
                    term.setDate(date);
                    term.setProjectionType(type);
                
                    FrmFilmCreateTableModel model = (FrmFilmCreateTableModel) formFilmCreate.getTblTable().getModel();
                    model.addItem(term);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            formCreate,
                            "Term date format must be: dd.MM.yyyy hh:MM!\n"+ex.getMessage(),
                            "Error!", JOptionPane.ERROR_MESSAGE
                    );                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            formCreate,
                            "Error creating term!\n"+ex.getMessage(),
                            "Error!", JOptionPane.ERROR_MESSAGE
                    );                    
                }
            }
        });
    }
    
}

