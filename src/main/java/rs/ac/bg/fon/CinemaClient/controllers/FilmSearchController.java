package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmFilmSearch;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.components.tables.FrmFilmSearchTableModel;
import rs.ac.bg.fon.CinemaCommon.domain.Film;

/**
 *
 * @author Nikola
 */
public class FilmSearchController {
    private final FrmFilmSearch formSearch;
    private FilmSearchController controller = this;

    public FilmSearchController(FrmFilmSearch formSearch) {
        this.formSearch = formSearch;
        addActionListeners();
    }
    
    public void openSearchForm(){
        try {
            List<Film> films = Communication.getInstance().getFilms();
            formSearch.getTblTable().setModel(new FrmFilmSearchTableModel(films));
            formSearch.setVisible(true);
        }catch(SocketException e){
            JOptionPane.showMessageDialog(
                formSearch,
                e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
            MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, null);
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                formSearch,
                "Error searching films.\n" + ex.getMessage(),
                "Films", JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    public void addActionListeners(){
        formSearch.btnSearchAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Film> films = findFilms();
                    if(films != null && films.size() != 0){
                         JOptionPane.showMessageDialog(
                             formSearch,
                             "Films found!",
                             "Films", JOptionPane.INFORMATION_MESSAGE
                         );
                     } else {
                         JOptionPane.showMessageDialog(
                             formSearch,
                             "No films found!",
                             "Films", JOptionPane.ERROR_MESSAGE
                         );                        
                         formSearch.getTblTable().setModel(new FrmFilmSearchTableModel(new ArrayList<Film>()));
                    }  
                }catch(SocketException ex){
                    JOptionPane.showMessageDialog(
                            formSearch,
                            ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                    MainCordinator.getInstance().closeAllForms((FrmMain) formSearch.getParent(), formSearch, null);
                }catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        formSearch,
                        "Error searching films.\n" + ex.getMessage(),
                        "Films", JOptionPane.ERROR_MESSAGE
                    );                     
                }
            }
        });
        formSearch.btnDetailsAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = formSearch.getTblTable().getSelectedRow();
                                
                if(row!=-1){
                    FrmFilmSearchTableModel model = (FrmFilmSearchTableModel) formSearch.getTblTable().getModel();
                    Film film = model.getFilm(row);
                
                    try {
                        Film dbFilm = Communication.getInstance().getFilmById(film.getFilmID());
                        MainCordinator.getInstance().openDetailsFilmForm((FrmMain) formSearch.getParent(), formSearch, controller, dbFilm);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "Error loading film!",
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
    
    public List<Film> findFilms() throws Exception{
       String title = formSearch.getTxtTitle().getText().trim();
       List<Film> films = Communication.getInstance().getFilms(title);
       
       if(films != null && !films.isEmpty()){
           FrmFilmSearchTableModel model = new FrmFilmSearchTableModel(films);
           formSearch.getTblTable().setModel(model);
       }
       return films;
    }
}

