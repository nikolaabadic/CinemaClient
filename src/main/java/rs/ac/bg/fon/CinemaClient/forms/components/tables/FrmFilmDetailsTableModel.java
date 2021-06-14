package rs.ac.bg.fon.CinemaClient.forms.components.tables;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

/**
 *
 * @author Nikola
 */
public class FrmFilmDetailsTableModel extends AbstractTableModel {
    private final List<Term> terms;

    public FrmFilmDetailsTableModel(List<Term> terms) {
        this.terms = terms;
    }
    
    @Override
    public int getRowCount() {
        if(terms ==null){
            return 0;
        }
        return terms.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Term term = terms.get(rowIndex);
        switch(columnIndex){
            case 0: return rowIndex+1;
            case 1: return term.getDate();
            case 2: return term.getHall().getName();
            case 3: return term.getProjectionType();
            default: return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0: return "Num";
            case 1: return "Date";
            case 2: return "Hall";
            case 3: return "Projection type";
            default: return "n/a";
        }
    }
}
