import ro.network.utils.AbstractServer;
import ro.network.utils.BasketJsonConcurrentServer;
import ro.network.utils.ServerException;
import ro.persistance.IRepoBilet;
import ro.persistance.IRepoMeci;
import ro.persistance.IRepoPersoana;
import ro.persistance.repository.RepoBilet;
import ro.persistance.repository.RepoBiletHibernate;
import ro.persistance.repository.RepoMeci;
import ro.persistance.repository.RepoPersoana;
import ro.server.BasketServiceImpl;
import ro.services.IBasketService;

import java.io.IOException;
import java.util.Properties;

public class StartJsonServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("basketserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find basketserver.properties "+e);
            return;
        }
        IRepoBilet repoBilet=new RepoBiletHibernate();
        //IRepoBilet repoBilet=new RepoBilet(serverProps);
        IRepoMeci repoMeci=new RepoMeci(serverProps);
        IRepoPersoana repoPersoana=new RepoPersoana(serverProps);
        IBasketService basketServerImpl=new BasketServiceImpl(repoBilet, repoMeci, repoPersoana);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("basket.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new BasketJsonConcurrentServer(chatServerPort, basketServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
