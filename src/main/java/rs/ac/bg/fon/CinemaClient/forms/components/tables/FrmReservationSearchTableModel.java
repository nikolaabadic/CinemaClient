package rs.ac.bg.fon.CinemaClient.forms.components.tables;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import rs.ac.bg.fon.CinemaCommon.domain.Reservation;

/**
 *
 * @author Nikola
 */
public class FrmReservationSearchTableModel extends AbstractTableModel {
    private List<Reservation> reservations;

    public FrmReservationSearchTableModel(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    @Override
    public int getRowCount() {
        return reservations.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reservation r = reservations.get(rowIndex);
        switch(columnIndex){
            case 0: return rowIndex + 1;
            case 1: return r.getTerm();
            case 2: return r.getUser().getUsername();
            case 3: return r.getReservationDate();
            default: return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0: return "Num";
            case 1: return "Term";
            case 2: return "Username";
            case 3: return "Date";
            default: return "n/a";                    
        }
    }
    
    public List<Reservation> getList(){
        return reservations;
    }
    
}

