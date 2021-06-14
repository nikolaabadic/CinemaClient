package rs.ac.bg.fon.CinemaClient.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import javax.swing.JOptionPane;

import rs.ac.bg.fon.CinemaClient.communication.Communication;
import rs.ac.bg.fon.CinemaClient.constants.Constants;
import rs.ac.bg.fon.CinemaClient.cordinator.MainCordinator;
import rs.ac.bg.fon.CinemaClient.forms.FrmLogin;
import rs.ac.bg.fon.CinemaCommon.domain.Admin;

/**
 *
 * @author Nikola
 */
public class LoginController {
    private final FrmLogin frmLogin;

    public LoginController(FrmLogin frmLogin) {
        this.frmLogin = frmLogin;
        addActionListener();
    }

    public void openForm() {
        frmLogin.setVisible(true);
    }
    
    private void addActionListener(){
        frmLogin.loginAddActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser(e);
            }
            
            private void loginUser(ActionEvent actionEvent) {
                resetForm();
                try {
                    String username = frmLogin.getTxtUsername().getText().trim();
                    String password = String.copyValueOf(frmLogin.getTxtPassword().getPassword());

                    validateForm(username, password);

                    Admin admin = Communication.getInstance().login(username, password);
                    JOptionPane.showMessageDialog(
                            frmLogin,
                            "Welcome " + admin.getName() + " " + admin.getSurname(),
                            "Login", JOptionPane.INFORMATION_MESSAGE
                    );
                    frmLogin.dispose();
                    MainCordinator.getInstance().addParam(Constants.CURRENT_USER, admin);
                    MainCordinator.getInstance().openMainForm();
                }catch(SocketException e){
                    JOptionPane.showMessageDialog(
                            frmLogin,
                            "Connection error. Please try again later.",
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }catch (Exception e) {
                    JOptionPane.showMessageDialog(frmLogin, e.getMessage(), "Login error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void resetForm() {
                frmLogin.getLblValidation().setText("");
            }

            private void validateForm(String username, String password) throws Exception {
                String errorMessage = "";
                if (username.isEmpty()) {
                    frmLogin.getLblValidation().setText("Username can not be empty!");
                    errorMessage += "Username can not be empty!\n";
                }
                if (password.isEmpty()) {
                    frmLogin.getLblValidation().setText("Password can not be empty!");
                    errorMessage += "Password can not be empty!\n";
                }
                if (!errorMessage.isEmpty()) {
                    throw new Exception(errorMessage);
                }
            }
        });
    }
}

