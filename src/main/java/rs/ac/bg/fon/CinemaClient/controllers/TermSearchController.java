package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.FrmTermSearch;
import rs.ac.bg.fon.CinemaClient.forms.components.tables.FrmTermSearchTableModel;
import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

/**
 *
 * @author Nikola
 */
public class TermSearchController {
    private final FrmTermSearch formSearch;
    private TermSearchController controller = this;
    private String searchValue = "title";
    
    public TermSearchController(FrmTermSearch formSearch) {
        this.formSearch = formSearch;
        addActionListeners();
        
    }
    
    public FrmTermSearch getForm(){
        return formSearch;
    }
    
    public String getSearchValue(){
        return searchValue;
    }
    
    public void openSearchForm(){
        try {
            setUpForm();        
            formSearch.setVisible(true);
        } catch(SocketException ex){
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
                "Error loading form!\n"+ex.getMessage(),
                "Error!", JOptionPane.ERROR_MESSAGE
            );
        }        
    }
    
    public void addActionListeners(){
        formSearch.btnSearchTitleAddActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                formSearch.getTxtDate().setText("");
                searchValue = "title";
                try {
                    List<Film> films = refreshTableByTitle();
                    if(films !=null && !films.isEmpty()){
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "Terms found!",
                            "Terms", JOptionPane.INFORMATION_MESSAGE
                        );                       
                    } else {
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "No terms found!",
                            "Terms", JOptionPane.ERROR_MESSAGE
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
                        "Error searching terms.\n" + ex.getMessage(),
                        "Terms", JOptionPane.ERROR_MESSAGE
                    );                     
                }
            }
        });
        formSearch.btnSearchHallAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formSearch.getTxtTitle().setText("");
                formSearch.getTxtDate().setText("");
                searchValue = "hall";
                try {
                    List<Term> terms = refreshTableByHall();
                    
                    if(terms!=null && !terms.isEmpty()){
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "Terms found!",
                            "Terms", JOptionPane.INFORMATION_MESSAGE
                        );                        
                    } else {
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "No terms found!",
                            "Terms", JOptionPane.ERROR_MESSAGE
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
                        "Error searching terms.\n" + ex.getMessage(),
                        "Terms", JOptionPane.ERROR_MESSAGE
                    ); 
                }
            }
        });
        formSearch.btnSearchDateAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formSearch.getTxtTitle().setText("");
                searchValue = "date";
                try {
                    List<Term> terms = refreshTableByDate();
                    if(terms!=null && !terms.isEmpty()){
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "Terms found!",
                            "Terms", JOptionPane.INFORMATION_MESSAGE
                        );                     
                    } else {
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "No terms found!",
                            "Terms", JOptionPane.ERROR_MESSAGE
                        );                                                
                    }
                }catch (ParseException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            formSearch,
                            "Term date format must be: dd.MM.yyyy!\n"+ex.getMessage(),
                            "Error!", JOptionPane.ERROR_MESSAGE
                    );
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
                        "Error searching terms.\n" + ex.getMessage(),
                        "Terms", JOptionPane.ERROR_MESSAGE
                    ); 
                }
            }
        });
        formSearch.btnDetailsAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = formSearch.getTblTable().getSelectedRow();
                if(row!=-1){
                    FrmTermSearchTableModel model = (FrmTermSearchTableModel) formSearch.getTblTable().getModel();
                    List<Term> terms = model.getList();
                    Term term = terms.get(row);
                    
                    try {
                        Term dbTerm = Communication.getInstance().getTermById(term.getTermID());
                        MainCordinator.getInstance().openDetailsTermForm((FrmMain)formSearch.getParent(),formSearch, controller,dbTerm);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(
                            formSearch,
                            "Error loading term!",
                            "Error!", JOptionPane.ERROR_MESSAGE
                        );
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(
                        formSearch,
                        "No rows selected!",
                        "Terms", JOptionPane.ERROR_MESSAGE
                    );                     
                }
                
            }
        });       
    
    }

    private void setUpForm() throws Exception{
        List<Hall> halls = Communication.getInstance().getHalls();
        formSearch.getCbHall().removeAllItems();
           
        for(Hall h : halls){
           formSearch.getCbHall().addItem(h);
        }
          
        List<Term> terms = Communication.getInstance().getTerms();
        formSearch.getTblTable().setModel(new FrmTermSearchTableModel(terms));
    }
    
    public List<Film> refreshTableByTitle() throws Exception{
        String title = formSearch.getTxtTitle().getText().trim();
        List<Film> films;
        if(title.isEmpty()){
            films = Communication.getInstance().getFilms();
            List<Term> terms = Communication.getInstance().getTerms();

            if(terms!=null && !terms.isEmpty()){
                FrmTermSearchTableModel model = new FrmTermSearchTableModel(terms);
                formSearch.getTblTable().setModel(model);
            } else { 
                formSearch.getTblTable().setModel(new FrmTermSearchTableModel(new ArrayList<Term>()));                          
            }
            
            return films;
        } 
        films = Communication.getInstance().getFilms(title);
                
        if(films !=null && !films.isEmpty()){
            List<Term> list = new ArrayList<>();
            for(int i = 0; i < films.size(); i++){
                int id = films.get(i).getFilmID();
                List<Term> terms = Communication.getInstance().getTerms(id);
                list.addAll(terms);
            }

            FrmTermSearchTableModel model = new FrmTermSearchTableModel(list);
            formSearch.getTblTable().setModel(model);                        
        } else {                        
            formSearch.getTblTable().setModel(new FrmTermSearchTableModel(new ArrayList<Term>()));                        
        }
        return films;
    }
    
    public List<Term> refreshTableByHall() throws Exception {
        int id = ((Hall)formSearch.getCbHall().getSelectedItem()).getHallID();
        List<Term> terms = Communication.getInstance().getTermsByHall(id);
        
        if(terms!=null && !terms.isEmpty()){
            FrmTermSearchTableModel model = new FrmTermSearchTableModel(terms);
            formSearch.getTblTable().setModel(model);   
        } else {   
            formSearch.getTblTable().setModel(new FrmTermSearchTableModel(new ArrayList<Term>()));                         
        }
        
        return terms;
    }
    
    public List<Term> refreshTableByDate() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateTxt = formSearch.getTxtDate().getText().trim();
        Date date = sdf.parse(dateTxt);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Term> terms = Communication.getInstance().getTermsByDate(format.format(date));
        
        if(terms!=null && !terms.isEmpty()){
            FrmTermSearchTableModel model = new FrmTermSearchTableModel(terms);
            formSearch.getTblTable().setModel(model);
        } else { 
            formSearch.getTblTable().setModel(new FrmTermSearchTableModel(new ArrayList<Term>()));                          
        }
        
        return terms;
    }
}

