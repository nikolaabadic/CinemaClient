package rs.ac.bg.fon.CinemaClient.main;

import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;

public class Main {
    public static void main(String[] args) {
        MainCordinator.getInstance().openLoginForm();
    }
}
