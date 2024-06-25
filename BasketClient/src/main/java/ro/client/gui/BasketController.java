package ro.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ro.model.Bilet;
import ro.model.Meci;
import ro.model.Persoana;
import ro.services.BasketException;
import ro.services.IBasketObserver;
import ro.services.IBasketService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BasketController implements Initializable, IBasketObserver {
    @FXML
    private TableView<Meci> table;

    @FXML
    private TextField clientText,locuriText;

    ObservableList<String> meciuri = FXCollections.observableArrayList();

    private IBasketService server;

    private Persoana persoanaLogata;

    public BasketController(){}

    public void setServer(IBasketService server){
        this.server=server;
    }

    public void setPersoana(Persoana user) {
        this.persoanaLogata = user;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void logOutButton(ActionEvent actionEvent) {
        logOut();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    void logOut() {
        try {
            server.logOut(persoanaLogata, this);
        } catch (BasketException e) {
            showErrorMessage("Logout error " + e.getMessage());
        }

    }

    public void initData() {
        System.out.println("persoana logata: "+persoanaLogata+persoanaLogata.getId());
        try{
            table.getItems().clear();
            for (Meci meci : server.findAll()) {
                table.getItems().add(meci);
            }
            TableColumn<Meci, String> numeColumn = (TableColumn<Meci, String>) table.getColumns().get(1);
            numeColumn.setCellFactory(column -> new TableCell<Meci, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setTextFill(Color.BLACK);
                    } else {
                        setText(item);
                        Meci meci = getTableView().getItems().get(getIndex());
                        if (meci.getNrLocuri() == 0) {
                            setTextFill(Color.RED);
                            setText(item + " SOLD OUT");
                        } else {
                            setTextFill(Color.BLACK);
                            setText(item);
                        }
                    }
                }
            });
        }catch (BasketException e){
            showErrorMessage(e.getMessage());
        }
    }

    public void biletVandut(Bilet bilet) throws BasketException{
        Platform.runLater(this::initData);
    }

    private void clearFields() {
        clientText.setText("");
        locuriText.setText("");
    }

    void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    @FXML
    private void vanzareButton(ActionEvent ec) {
        int index=table.getSelectionModel().getSelectedIndex();
        if (index<0) {
            showErrorMessage("Eroare la vanzare: trebuie sa selectati un meci!");
            return;
        }
        String client = clientText.getText();
        try{
            int locuri = Integer.parseInt(locuriText.getText());
            int meci=table.getSelectionModel().getSelectedItem().getId();
            Bilet bilet=new Bilet(meci,client,locuri);
            System.out.println("in controller "+meci);
            Meci selectedMeci = table.getSelectionModel().getSelectedItem();
            System.out.println("Selected Meci: " + selectedMeci + selectedMeci.getId());
            server.vanzareBilet(bilet);
        }
        catch(Exception ex){
            showErrorMessage("Eroare: "+ex.getMessage());
        }
    }

    @FXML
    private void cautareButton(ActionEvent ec){
        try {
            table.getItems().clear();
            for (Meci meci : server.cautaDisponibile()) {
                table.getItems().add(meci);
            }
        }catch (BasketException e){
            showErrorMessage(e.getMessage());
        }
    }

    @FXML
    private void refreshButton(ActionEvent ec){
        initData();
    }

//    @FXML
//    private void logOutButton(ActionEvent ec) throws IOException {
//        FXMLLoader stageLoader = new FXMLLoader();
//        stageLoader.setLocation(getClass().getResource("/logIn.fxml"));
//        Stage stage = (Stage)((Node)ec.getSource()).getScene().getWindow();
//
//        AnchorPane logInLayout = stageLoader.load();
//        Scene scene = new Scene(logInLayout);
//        stage.setScene(scene);
//
//        LogInController logInController = stageLoader.getController();
//
//        stage.show();
//    }
}
