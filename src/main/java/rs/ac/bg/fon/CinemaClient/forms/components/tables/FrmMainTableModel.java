package rs.ac.bg.fon.CinemaClient.forms.components.tables;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import rs.ac.bg.fon.CinemaClient.util.Movie;
import rs.ac.bg.fon.CinemaCommon.domain.Term;

public class FrmMainTableModel extends AbstractTableModel{
	private final List<Movie> movies;
	
	public FrmMainTableModel(List<Movie> movies) {
		this.movies = movies;
	}

	@Override
	public int getRowCount() {
		if (movies == null) {
			return 0;
		}
		return movies.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Movie movie = movies.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return movie.getTitle();
		case 2:
			return movie.getRating();
		case 3:
			return movie.getYear();
		default:
			return "n/a";
		}
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Rank";
		case 1:
			return "Title";
		case 2:
			return "Rating";
		case 3:
			return "Year";
		default:
			return "n/a";
		}
	}

	public List<Movie> getList() {
		return movies;
	}

	public void addItem(Movie movie) {
		movies.add(movie);
		fireTableDataChanged();
	}
}
