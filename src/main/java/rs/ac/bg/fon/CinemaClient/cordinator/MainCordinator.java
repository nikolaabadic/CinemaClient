package rs.ac.bg.fon.CinemaClient.cordinator;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.controllers.FilmCreateController;
import rs.ac.bg.fon.CinemaClient.controllers.FilmDetailsController;
import rs.ac.bg.fon.CinemaClient.controllers.FilmSearchController;
import rs.ac.bg.fon.CinemaClient.controllers.LoginController;
import rs.ac.bg.fon.CinemaClient.controllers.MainController;
import rs.ac.bg.fon.CinemaClient.controllers.ReservationCreateController;
import rs.ac.bg.fon.CinemaClient.controllers.ReservationDetailsController;
import rs.ac.bg.fon.CinemaClient.controllers.ReservationSearchController;
import rs.ac.bg.fon.CinemaClient.controllers.TermCreateController;
import rs.ac.bg.fon.CinemaClient.controllers.TermCreateNewFilmController;
import rs.ac.bg.fon.CinemaClient.controllers.TermDetailsController;
import rs.ac.bg.fon.CinemaClient.controllers.TermEditController;
import rs.ac.bg.fon.CinemaClient.controllers.TermSearchController;
import rs.ac.bg.fon.CinemaClient.forms.FrmFilmCreate;
import rs.ac.bg.fon.CinemaClient.forms.FrmFilmDetails;
import rs.ac.bg.fon.CinemaClient.forms.FrmFilmSearch;
import rs.ac.bg.fon.CinemaClient.forms.FrmLogin;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.FrmReservationCreate;
import rs.ac.bg.fon.CinemaClient.forms.FrmReservationDetails;
import rs.ac.bg.fon.CinemaClient.forms.FrmReservationSearch;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermCreate;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermCreateNewFilm;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermDetails;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermEdit;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermSearch;
import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

/**
 *
 * @author Nikola
 */
public class MainCordinator {
    private static MainCordinator instance;
    private final MainController mainController;
    private final Map<String, Object> params;
    
    private MainCordinator(){
        mainController = new MainController(new FrmMain());
        params = new HashMap<>();
    }
    
    public static MainCordinator getInstance(){
        if(instance == null){
            instance = new MainCordinator();
        }
        return instance;
    }
    
    public void addParam(String name, Object key) {
        params.put(name, key);
    }

    public Object getParam(String name) {
        return params.get(name);
    }
    
    public void openLoginForm() {
        LoginController loginContoller = new LoginController(new FrmLogin());
        loginContoller.openForm();
    }
    
    public void openMainForm(){
        mainController.openForm();
    }
    
    public void openCreateFilmForm(FrmMain form){
        FilmCreateController filmController = new FilmCreateController(new FrmFilmCreate(form, true));
        filmController.openCreateForm();
    }
    
    public void openSearchFilmForm(FrmMain form){
        FilmSearchController filmController = new FilmSearchController(new FrmFilmSearch(form, true));
        filmController.openSearchForm();
        
    }
    public void openDetailsFilmForm(FrmMain form, FrmFilmSearch searchForm, FilmSearchController controller,  Film film){
        FrmFilmDetails formDetails = new FrmFilmDetails(form, true);
        FilmDetailsController filmController = new FilmDetailsController(formDetails, searchForm, controller, film);
        filmController.openDetailsForm();
    } 
    
    public void openCreateTermForm(FrmMain form){
        TermCreateController termController = new TermCreateController(new FrmTermCreate(form, true));
        termController.openCreateForm();
    }
    
    public void openSearchTermForm(FrmMain form){
        TermSearchController termController = new TermSearchController(new FrmTermSearch(form, true));
        termController.openSearchForm();
        
    }
    public void openCreateReservationForm(FrmMain form){
        ReservationCreateController reservationController = new ReservationCreateController(new FrmReservationCreate(form, true));
        reservationController.openForm();
    }
    public void openDetailsTermForm(FrmMain form, FrmTermSearch formSearch, TermSearchController controller, Term term){
        TermDetailsController termController = new TermDetailsController(new FrmTermDetails(form, true),formSearch, controller, term);
        termController.openForm();
    }

    public void openEditTermForm(FrmMain frmMain, FrmTermSearch frmSearch, FrmTermDetails frmDetails, TermSearchController controller, TermDetailsController detailsController, Term term) {
        TermEditController termController = new TermEditController(new FrmTermEdit(frmMain, true), frmSearch, frmDetails, controller, detailsController, term);
        termController.openForm();
    }

    public void openSearchReservationForm(FrmMain frmMain) {
        ReservationSearchController reservationController = new ReservationSearchController(new FrmReservationSearch(frmMain, true));
        reservationController.openForm();
    }

    public void openDetailsReservationForm(FrmMain formMain, FrmReservationSearch formSearch, ReservationSearchController controller, Reservation r) {
        ReservationDetailsController reservationController = new ReservationDetailsController(new FrmReservationDetails(formMain,true), formSearch,controller, r);
        reservationController.openForm();
    }

    public void openCreateTermFormNewFilm(FrmMain form, FrmFilmCreate formCreate) {
        TermCreateNewFilmController termController = new TermCreateNewFilmController(new FrmTermCreateNewFilm(form, true),formCreate);
        termController.openForm();
    }
    
    public void closeAllForms(FrmMain main, JDialog parent, JDialog current){

        if(current != null){
            current.dispose();
        }
                
        parent.dispose();
        main.dispose();
        
        try {
            Communication.restart();
        } catch (Exception ex) {
            Logger.getLogger(MainCordinator.class.getName()).log(Level.SEVERE, null, ex);
        }
        openLoginForm();
    }
    
        public void closeAllForms(FrmMain main, JDialog parent, JDialog parent2, JDialog current){
        main.dispose();
        parent.dispose();
        parent2.dispose();
        current.dispose();
        
        Communication.restart();
        openLoginForm();
    }
}

