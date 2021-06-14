package rs.ac.bg.fon.CinemaClient.forms.components.tables;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import rs.ac.bg.fon.CinemaCommon.domain.Reservation;

/**
 *
 * @author Nikola
 */
public class FrmTermDetailsTableModel extends AbstractTableModel{
    private final List<Reservation> reservations;

    public FrmTermDetailsTableModel(List<Reservation> reservations) {
        this.reservations = reservations;
    }
                
    @Override
    public int getRowCount() {
        return reservations.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reservation r = reservations.get(rowIndex);
        switch(columnIndex){
            case 0: return rowIndex+1;
            case 1: return r.getUser().getUsername();
            case 2: return r.getNumberOfTickets();
            default: return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0: return "Num";
            case 1: return "Username";
            case 2: return "Number of tickets";
            default: return "n/a";
        }
    }
    
}

