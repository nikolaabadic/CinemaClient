package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.List;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmFilmDetails;
import rs.ac.bg.fon.CinemaClient.forms.FrmFilmSearch;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.components.tables.FrmFilmDetailsTableModel;
import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

/**
 *
 * @author Nikola
 */
public class FilmDetailsController {
    private final Film film;
    private final FrmFilmDetails formDetails;
    private final FrmFilmSearch formSearch;
    private final FilmSearchController controller;
    
    public FilmDetailsController(FrmFilmDetails formDetails, FrmFilmSearch formSearch, FilmSearchController controller, Film film) {
        this.formDetails=formDetails;
        this.formSearch = formSearch;
        this.film = film;
        this.controller = controller;
        addActionListener();
    }
    
    public void openDetailsForm(){
        try{
            formDetails.getTxtTitle().setText(film.getName());
            formDetails.getTxtYear().setText(String.valueOf(film.getYear()));
            formDetails.getTxtDuration().setText(String.valueOf(film.getDuration()));
            formDetails.getTxtLanguage().setText(film.getLanguage());
        
            formDetails.getTxtYear().setEditable(false);
            formDetails.getTxtTitle().setEditable(false);
            formDetails.getTxtLanguage().setEditable(false);
            formDetails.getTxtDuration().setEditable(false);
                        
            List<Term> terms = Communication.getInstance().getTerms(film.getFilmID());
            List<Hall> halls = Communication.getInstance().getHalls();
            for(Term t : terms){
                for(Hall h : halls){
                    if( h.getHallID() == t.getHall().getHallID()){
                        t.setHall(h);
                    }
                }
            }
            FrmFilmDetailsTableModel model = new FrmFilmDetailsTableModel(terms);
            formDetails.getTblTable().setModel(model);
            
            JOptionPane.showMessageDialog(
                formDetails,
                "Film loaded!",
                "Films", JOptionPane.INFORMATION_MESSAGE
            );   
            formDetails.setVisible(true);
        }catch(SocketException e){
            JOptionPane.showMessageDialog(
                formDetails,
                e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
            MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, formDetails);
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                formDetails,
                "Error loading film!\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );            
        }        
    }
    
    public void addActionListener(){
        formDetails.btnDeleteAddActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Communication.getInstance().deleteFilm(film);
                    JOptionPane.showMessageDialog(
                            formDetails,
                            "Film deleted successfully!",
                            "Success!",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    controller.findFilms();
                    formDetails.dispose();
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
                        "Error deleting film!\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
}

