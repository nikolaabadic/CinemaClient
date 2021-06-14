package rs.ac.bg.fon.CinemaClient.communication;

import java.net.Socket;
import java.util.Date;
import java.util.List;

import rs.ac.bg.fon.CinemaCommon.communication.Operation;
import rs.ac.bg.fon.CinemaCommon.communication.Receiver;
import rs.ac.bg.fon.CinemaCommon.communication.Request;
import rs.ac.bg.fon.CinemaCommon.communication.Response;
import rs.ac.bg.fon.CinemaCommon.communication.Sender;
import rs.ac.bg.fon.CinemaCommon.domain.Admin;
import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaCommon.domain.User;

/**
 *
 * @author Nikola
 */
public class Communication {
    private Socket socket;
    private Sender sender;
    private Receiver receiver;
    private static Communication instance;
    private Communication() throws Exception{
        socket=new Socket("localhost", 9000);
        sender=new Sender(socket);
        receiver=new Receiver(socket);
    }
    public static Communication getInstance() throws Exception{
        if(instance==null){
            instance=new Communication();
        }
        return instance;
    }
    
    public static void restart(){
        instance = null;
    }
    
    public Admin login(String username, String password) throws Exception {
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        
        Request request=new Request(Operation.LOGIN, user);
        sender.send(request);
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (Admin)response.getResult();
        }else{
            throw response.getException();
        }
    }
    
    public boolean addFilm(String name, int year, int duration, String language, List<Term> terms) throws Exception{
        Film film = new Film();
        film.setName(name);
        film.setYear(year);
        film.setDuration(duration);
        film.setLanguage(language);
        film.setTerms(terms);
        
        Request request=new Request(Operation.ADD_FILM, film);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (boolean)response.getResult();
        }else{
            throw response.getException();
        }
    }
    
    public List<Film> getFilms() throws Exception{
        Request request=new Request(Operation.GET_FILMS, null);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Film>)response.getResult();
        }else{
            throw response.getException();
        }
    }
    
    public List<Hall> getHalls() throws Exception{
        Request request=new Request(Operation.GET_HALLS, null);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Hall>)response.getResult();
        }else{
            throw response.getException();
        }
    }
    
    public List<User> getUsers() throws Exception{
        Request request=new Request(Operation.GET_USERS, null);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<User>)response.getResult();
        }else{
            throw response.getException();
        }
    }
    
    public boolean addReservation(Term term, User user, int number, Date date) throws Exception{
        Reservation reservation = new Reservation(term, user, date, number);
        Request request=new Request(Operation.ADD_RESERVATION, reservation);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (boolean)response.getResult();
        }else{
            throw response.getException();
        }
    }
    
    public List<Term> getTerms() throws Exception{
        Request request=new Request(Operation.GET_TERMS, null);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Term>)response.getResult();
        }else{
            throw response.getException();
        }
    }
    
        public List<Reservation> getReservations() throws Exception{
        Request request=new Request(Operation.GET_RESERVATIONS, null);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Reservation>)response.getResult();
        }else{
            throw response.getException();
        }
    }
    
    public boolean deleteFilm(Film film) throws Exception{
        Request request=new Request(Operation.DELETE_FILM, film);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return true;
        }else{
            throw response.getException();
        }
    }
    
    public boolean addTerm(Film film, Hall hall, Date date, String type) throws Exception{
        Term term = new Term();
        term.setFilm(film);
        term.setHall(hall);
        term.setDate(date);
        term.setProjectionType(type);
        
        Request request=new Request(Operation.ADD_TERM, term);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (boolean)response.getResult();
        }else{
            throw response.getException();
        }
    }
    
    public boolean deleteTerm(Term term) throws Exception{
        Request request=new Request(Operation.DELETE_TERM, term);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return true;
        }else{
            throw response.getException();
        }
    }
    
    public void editTerm(Term term) throws Exception{
        Request request = new Request(Operation.EDIT_TERM, term);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()!=null){
            throw response.getException();
        }        
    }

    public void deleteReservation(Reservation reservation) throws Exception {
        Request request = new Request(Operation.DELETE_RESERVATION, reservation);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()!=null){
            throw response.getException();
        } 
    }
    
    public List<Film> getFilms(String where) throws Exception{
        Request request=new Request(Operation.GET_FILMS_WHERE, where);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Film>)response.getResult();
        }else{
            throw response.getException();
        }        
    }
    public List<Term> getTerms(int filmID) throws Exception{
        Request request=new Request(Operation.GET_TERMS_BY_FILM_ID, filmID);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Term>)response.getResult();
        }else{
            throw response.getException();
        }         
    }

    public List<Term> getTermsByHall(int hallID) throws Exception {
        Request request=new Request(Operation.GET_TERMS_BY_HALL, hallID);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Term>)response.getResult();
        }else{
            throw response.getException();
        } 
    }

    public List<Term> getTermsByDate(String date) throws Exception {
        Request request=new Request(Operation.GET_TERMS_BY_DATE, date);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Term>)response.getResult();
        }else{
            throw response.getException();
        } 
    }

    public List<Reservation> getReservations(int termID) throws Exception {
        Request request=new Request(Operation.GET_RESERVATIONS_BY_TERM_ID, termID);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Reservation>)response.getResult();
        }else{
            throw response.getException();
        }         
    }

    public List<Reservation> getReservations(String username) throws Exception{
        Request request=new Request(Operation.GET_RESERVATIONS_BY_USERNAME, username);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Reservation>)response.getResult();
        }else{
            throw response.getException();
        } 
    }

    public List<Reservation> getReservationsByDate(String date) throws Exception{
        Request request=new Request(Operation.GET_RESERVATIONS_BY_DATE, date);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (List<Reservation>)response.getResult();
        }else{
            throw response.getException();
        } 
    }
    
    public Film getFilmById(int id) throws Exception{
        Request request=new Request(Operation.GET_FILM_BY_ID, id);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (Film)response.getResult();
        }else{
            throw response.getException();
        } 
    }
    
    public Term getTermById(int id) throws Exception{
        Request request=new Request(Operation.GET_TERM_BY_ID, id);
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (Term)response.getResult();
        }else{
            throw response.getException();
        } 
    }

    public Reservation getReservationsById(int termID, int userID) throws Exception {
        Request request=new Request(Operation.GET_RESERVATION_BY_ID, new int[]{termID, userID});
        sender.send(request);
        
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (Reservation)response.getResult();
        }else{
            throw response.getException();
        } 
    }
}
