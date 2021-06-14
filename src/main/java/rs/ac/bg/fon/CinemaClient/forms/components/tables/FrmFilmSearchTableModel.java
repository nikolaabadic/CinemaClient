package rs.ac.bg.fon.CinemaClient.forms.components.tables;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import rs.ac.bg.fon.CinemaCommon.domain.Film;

/**
 *
 * @author Nikola
 */
public class FrmFilmSearchTableModel extends AbstractTableModel {
    private final List<Film> films;

    public FrmFilmSearchTableModel(List<Film> films) {
        this.films = films;
    }
    
    @Override
    public int getRowCount() {
        if(films!=null){
            return films.size();
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Film film = films.get(rowIndex);
        switch(columnIndex){
            case 0: return rowIndex + 1;
            case 1: return film.getName();
            case 2: return film.getYear();
            case 3: return film.getDuration();
            case 4: return film.getLanguage();
            default: return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0: return "Num";
            case 1: return "Name";
            case 2: return "Year";
            case 3: return "Duration";
            case 4: return "Language";
            default: return "n/a";
        }
    }
    
    public Film getFilm(int index){
        if(index!=-1){
            return films.get(index);
        }
        return null;
    }
    
    
}

