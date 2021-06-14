package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmFilmCreate;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.components.tables.FrmFilmCreateTableModel;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

/**
 *
 * @author Nikola
 */
public class FilmCreateController {
    private final FrmFilmCreate frmCreate;
    
    public FilmCreateController(FrmFilmCreate frmCreate){
        this.frmCreate=frmCreate;
        addActionListeners();
    }
    
    public void openCreateForm() {
        setUpForm();
        frmCreate.setVisible(true);
    }
    
    public void addActionListeners(){
        frmCreate.btnAddAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFilm(e);
            }
            private void addFilm(ActionEvent actionEvent){
                try{      
                    if(frmCreate.getTxtTitle().getText().isEmpty() || frmCreate.getTxtYear().getText().isEmpty() 
                            || frmCreate.getTxtLanguage().getText().isEmpty() || frmCreate.getTxtDuration().getText().isEmpty()){
                        throw new Exception("All fields are required!");
                    }
                    
                    String name = frmCreate.getTxtTitle().getText().trim();
                    int year = Integer.parseInt(frmCreate.getTxtYear().getText().trim());
                    int duration = Integer.parseInt(frmCreate.getTxtDuration().getText().trim());
                    String language = frmCreate.getTxtLanguage().getText().trim();
                    List<Term> terms = ((FrmFilmCreateTableModel)frmCreate.getTblTable().getModel()).getList();
                    
                    for(int i = 0; i < terms.size(); i++){
                        for(int j = 0; j< terms.size(); j++){
                            if(terms.get(i).equals(terms.get(j)) && i!=j){
                                throw new Exception("Terms " + i+1 + " and " + j+1 + " are equal!");
                            }
                        }
                    }
                    
                    Communication.getInstance().addFilm(name, year, duration, language,terms);
                    
                    JOptionPane.showMessageDialog(
                            frmCreate,
                            "Film added!",
                            "Films", JOptionPane.INFORMATION_MESSAGE
                    );
                    resetForm();
                }catch(SocketException e){
                    JOptionPane.showMessageDialog(
                            frmCreate,
                            e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                    MainCordinator.getInstance().closeAllForms((FrmMain) frmCreate.getParent(), frmCreate, null);
                }catch (Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmCreate,
                            "Error creating film!\n" + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            private void resetForm(){
                frmCreate.getTxtTitle().setText("");
                frmCreate.getTxtDuration().setText("");
                frmCreate.getTxtYear().setText("");
                frmCreate.getTxtLanguage().setText("");
                frmCreate.getTblTable().setModel(new FrmFilmCreateTableModel(new ArrayList<Term>()));
            }
        });
        frmCreate.btnCancelAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmCreate.dispose();
            }
        });
        frmCreate.btnAddTermAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openCreateTermFormNewFilm((FrmMain)frmCreate.getParent(),frmCreate);
            }
        });
        frmCreate.btnDeleteTermAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = frmCreate.getTblTable().getSelectedRow();
                if(row!=-1){
                    FrmFilmCreateTableModel model = (FrmFilmCreateTableModel) frmCreate.getTblTable().getModel();
                    model.deleteItem(row);
                } else {
                    JOptionPane.showMessageDialog(
                            frmCreate,
                            "No rows selected!\n",
                            "Error!", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });        
    }

    private void setUpForm() {
        FrmFilmCreateTableModel model = new FrmFilmCreateTableModel(new ArrayList<Term>());
        frmCreate.getTblTable().setModel(model);
    }
}
