package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermDetails;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermEdit;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermSearch;
import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

/**
 *
 * @author Nikola
 */
public class TermEditController {
    private final FrmTermEdit formEdit;
    private final FrmTermSearch formSearch;
    private final FrmTermDetails formDetails;
    private Term term;
    private TermSearchController controller;
    private TermDetailsController detailsController;

    public TermEditController(FrmTermEdit formEdit, FrmTermSearch formSearch, FrmTermDetails formDetails, TermSearchController controller, TermDetailsController detailsController, Term term) {
        this.formEdit = formEdit;
        this.formSearch = formSearch;
        this.formDetails = formDetails;
        this.term = term;
        this.controller = controller;
        this.detailsController = detailsController;
        addActionListeners();
    }
    
    public void openForm(){
        try {
            setUpForm();
            formEdit.setVisible(true);
        }catch(SocketException ex){
            JOptionPane.showMessageDialog(
                formEdit,
                ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
            MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, formDetails, formEdit);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                formEdit,
                "Error loading term!\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );         
        }
    }

    private void setUpForm() throws Exception{
        Film film =  Communication.getInstance().getFilmById(term.getFilm().getFilmID());
        List<Hall> halls = Communication.getInstance().getHalls();

        formEdit.getTxtTitle().setText(film.getName());
        formEdit.getTxtTitle().setEditable(false);
                
        int index=0;
        int current=0;
        formEdit.getCbHall().removeAllItems();
        for(Hall h : halls){
            if(h.getHallID()==term.getHall().getHallID()){
                index=current;
            }
            formEdit.getCbHall().addItem(h);
            current++;
        }
        formEdit.getCbHall().setSelectedIndex(index);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        formEdit.getTxtDate().setText(sdf.format(term.getDate()));

        if(term.getProjectionType().equals("2D")){
            formEdit.getRb2D().setSelected(true);
            formEdit.getRb3D().setSelected(false);
        } else {
            formEdit.getRb2D().setSelected(false);
            formEdit.getRb3D().setSelected(true);
        }
    }
    
    private void addActionListeners() {
        formEdit.btnCancelAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formEdit.dispose();
            }
        });
        formEdit.btnEditAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    term.setHall((Hall) formEdit.getCbHall().getSelectedItem());
                
                    String type = formEdit.getRb2D().isSelected() ? "2D" : "3D";
                    term.setProjectionType(type);
                    term.setDate(sdf.parse(formEdit.getTxtDate().getText().trim()));
                                        
                    Communication.getInstance().editTerm(term);
                    
                    JOptionPane.showMessageDialog(
                            formEdit,
                            "Term edited successfully!",
                            "Success!", JOptionPane.INFORMATION_MESSAGE
                    );
                    detailsController.setTerm(term);
                    refreshTable();
                    formEdit.dispose();                    
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            formEdit,
                            "Term date format must be: dd.MM.yyyy hh:MM!\n"+ex.getMessage(),
                            "Error!", JOptionPane.ERROR_MESSAGE
                    );
                }catch(SocketException ex){
                    JOptionPane.showMessageDialog(
                       formEdit,
                       ex.getMessage(),
                       "Error", JOptionPane.ERROR_MESSAGE
                    );
                    MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, formDetails, formEdit);    
                }catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            formEdit,
                            "Error editing term!\n" + ex.getMessage(),
                            "Error!", JOptionPane.ERROR_MESSAGE
                    );
                }
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
}

