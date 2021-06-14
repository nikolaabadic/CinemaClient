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
import rs.ac.bg.fon.CinemaClient.forms.FrmTermCreate;
import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;

/**
 *
 * @author Nikola
 */
public class TermCreateController {
    private final FrmTermCreate formCreate;
    
    public TermCreateController(FrmTermCreate formCreate){
        this.formCreate=formCreate;
        addActionListeners();
    }
    
    public void openCreateForm(){
        try {
            setUpCreateForm();
            formCreate.setVisible(true);
        }catch(SocketException ex){
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
                "Error loading form!\n"+ex.getMessage(),
                "Error!", JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    public void setUpCreateForm() throws Exception{
        List<Film> films =  Communication.getInstance().getFilms();
        List<Hall> halls = Communication.getInstance().getHalls();
        
        formCreate.getCbFilm().removeAllItems();
        for(Film f : films){
            formCreate.getCbFilm().addItem(f);
        }
        
        formCreate.getCbHall().removeAllItems();
        for(Hall h : halls){
            formCreate.getCbHall().addItem(h);
        }
        formCreate.getTxtDate().setText("");        
    }
    
    public void addActionListeners(){
        formCreate.btnAddTermAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    Film film = (Film) formCreate.getCbFilm().getSelectedItem();
                    Hall hall = (Hall) formCreate.getCbHall().getSelectedItem();
                
                    String type =  formCreate.getRb2D().isSelected() ? "2D" : "3D";
                
                    Date date = sdf.parse(formCreate.getTxtDate().getText());
                    
                    Communication.getInstance().addTerm(film,hall,date,type);
                    
                    JOptionPane.showMessageDialog(
                            formCreate,
                            "Term added successfully!",
                            "Success!", JOptionPane.INFORMATION_MESSAGE
                    );
                    setUpCreateForm();
                } catch (ParseException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            formCreate,
                            "Term date format must be: dd.MM.yyyy hh:MM!\n"+ex.getMessage(),
                            "Error!", JOptionPane.ERROR_MESSAGE
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
                            "Error creating term!\n" + ex.getMessage(),
                            "Error!", JOptionPane.ERROR_MESSAGE
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

