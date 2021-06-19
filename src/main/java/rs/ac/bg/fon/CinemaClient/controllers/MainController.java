package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmMain;
import rs.ac.bg.fon.CinemaClient.forms.components.tables.FrmMainTableModel;
import rs.ac.bg.fon.CinemaClient.util.Movie;

/**
 *
 * @author Nikola
 */
public class MainController {
    private final FrmMain frmMain;
    
    public MainController(FrmMain frmMain){
        this.frmMain=frmMain;
        addActionListener();
    }
    
    public void openForm(){
        this.frmMain.setVisible(true);
        setUpTable();
    }
    
    private void setUpTable() {
    	final String BASE_URL = "https://imdb-api.com";
    	final String API_KEY = "k_uvj3422y";
		List<Movie> movies = new ArrayList<>();
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try {
			URL url = new URL(BASE_URL + "/en/API/Top250Movies/" + API_KEY);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			JsonObject res = gson.fromJson(reader, JsonObject.class);
			
			if(res.get("errorMessage").getAsString().isEmpty()) {
				JsonArray items = res.get("items").getAsJsonArray();
				for(JsonElement item : items) {
					if(movies.size() == 10) {
						break;
					}
					Movie movie = new Movie();
					JsonObject obj = (JsonObject)item;
					movie.setRating(obj.get("imDbRating").getAsDouble());
					movie.setTitle(obj.get("title").getAsString());
					movie.setYear(obj.get("year").getAsInt());
					movies.add(movie);
				}
				FrmMainTableModel model = new FrmMainTableModel(movies);
				frmMain.getTblTopMovies().setModel(model);
			} else {
	            JOptionPane.showMessageDialog(
	                    frmMain,
	                    res.get("errorMessage"),
	                    "IMDb Top 10 Movies", JOptionPane.ERROR_MESSAGE
	            ); 
			}
		} catch (Exception e) {
			e.printStackTrace();
            JOptionPane.showMessageDialog(
                    frmMain,
                    "Error receiving response from API.",
                    "IMDb Top 10 Movies", JOptionPane.ERROR_MESSAGE
            ); 			
		}		
	}

	private void addActionListener() {
        frmMain.miFilmCreateAddActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openCreateFilmForm(frmMain);
            }
        });
        frmMain.miFilmSearchAddActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchFilmForm(frmMain);
            }
            
        });
        frmMain.miTermCreateAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openCreateTermForm(frmMain);
            }
        });
        frmMain.miTermSearchAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchTermForm(frmMain);
            }
        });
        
        frmMain.miReservationCreateAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openCreateReservationForm(frmMain);
            }
        });
        frmMain.miReservationDeleteAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchReservationForm(frmMain);
            }
        });
        frmMain.miFilmDeleteAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchFilmForm(frmMain);
            }
        });
        frmMain.miTermEditAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchTermForm(frmMain);
            }
        });
        frmMain.miTermDeleteAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainCordinator.getInstance().openSearchTermForm(frmMain);
            }
        });        
    }
    
    
}

