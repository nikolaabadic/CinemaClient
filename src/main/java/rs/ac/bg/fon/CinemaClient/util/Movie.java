package rs.ac.bg.fon.CinemaClient.util;

public class Movie {
	private String title;
	private double rating;
	private int year;
	
	public Movie() {
	}

	public Movie(String title, double rating) {
		super();
		this.title = title;
		this.rating = rating;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	
	
}
