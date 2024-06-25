package ro.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.model.Persoana;
import ro.services.BasketException;
import ro.services.IBasketService;

import java.io.IOException;

public class LogInController {
    @FXML
    private Button logInButton;
    //public Button logInButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private IBasketService server;

    Parent mainBasketParent;
    private BasketController basketController;
    private Persoana persoanaLogata;

    public void setServer(IBasketService server){
        this.server=server;
    }
    public void setParent(Parent p){
        this.mainBasketParent=p;
    }

    public void setBasketController(BasketController basketController) {
        this.basketController = basketController;
    }
    void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    @FXML
    protected void onLogInButtonCLick(ActionEvent event){
        String numeUtilizator=username.getText();
        String parola=password.getText();
        Persoana persoana=new Persoana(numeUtilizator,parola);
        try {
            System.out.println("CONTROLLER "+persoana);
            persoanaLogata=server.logIn(persoana,basketController);
            System.out.println("CONTROLLER DUPA SERVER"+persoanaLogata);
            Stage stage=new Stage();
            stage.setTitle("Logged in as: " +persoanaLogata.getUsername());//getUsername
            stage.setScene(new Scene(mainBasketParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    basketController.logOut();
                    System.exit(0);
                }
            });

            stage.show();
            basketController.setPersoana(persoanaLogata);
            basketController.initData();
            ((Node)(event.getSource())).getScene().getWindow().hide();

        }
        catch (BasketException e) {
            showErrorMessage("Logare esuata! "+e.getMessage());
        }
    }
}
