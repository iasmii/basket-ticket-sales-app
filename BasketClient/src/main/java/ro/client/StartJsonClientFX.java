package ro.client;

import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.client.gui.BasketController;
import ro.client.gui.LogInController;
import ro.network.jsonprotocol.BasketServicesJsonProxy;
import ro.services.IBasketService;

public class StartJsonClientFX  extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartJsonClientFX.class.getResourceAsStream("/basketclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find basketclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("basket.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("basket.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IBasketService server = new BasketServicesJsonProxy(serverIP, serverPort);



        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("LogInW.fxml"));
        Parent root=loader.load();


        LogInController ctrl =
                loader.<LogInController>getController();
        ctrl.setServer(server);




        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("BasketW.fxml"));
        Parent croot=cloader.load();


        BasketController chatCtrl =
                cloader.<BasketController>getController();
        chatCtrl.setServer(server);

        ctrl.setBasketController(chatCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("MPP basket");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();

    }


}
