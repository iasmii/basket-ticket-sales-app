package ro.server;

import ro.model.Bilet;
import ro.model.Meci;
import ro.model.Persoana;
import ro.network.dto.PersoanaDTO;
import ro.network.jsonprotocol.Response;
import ro.network.jsonprotocol.ResponseType;
import ro.persistance.IRepoBilet;
import ro.persistance.IRepoMeci;
import ro.persistance.IRepoPersoana;
import ro.services.BasketException;
import ro.services.IBasketObserver;
import ro.services.IBasketService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasketServiceImpl implements IBasketService {
    private final int defaultThreadsNo=5;
    private IRepoBilet repoBilet;
    private IRepoMeci repoMeci;
    private IRepoPersoana repoPersoana;
    private Map<Integer, IBasketObserver> loggedClients;

    public BasketServiceImpl(IRepoBilet repoBilet,IRepoMeci repoMeci, IRepoPersoana repoPersoana){
        this.repoBilet=repoBilet;
        this.repoMeci=repoMeci;
        this.repoPersoana=repoPersoana;
        loggedClients=new ConcurrentHashMap<>();
    }

    public synchronized Persoana logIn(Persoana user, IBasketObserver client) throws BasketException {
        Persoana userR=repoPersoana.logIn(user.getUsername(),user.getParola());
        if (userR!=null){
            if(loggedClients.get(userR.getId())!=null)
                throw new BasketException("User already logged in.");
            loggedClients.put(userR.getId(), client);
            //notifyFriendsLoggedIn(user);
            return userR;
        }else
            throw new BasketException("Authentication failed.");
    }
//public synchronized Persoana logIn(Persoana user, IBasketObserver client) throws BasketException {
//    Persoana userR = repoPersoana.logIn(user.getUsername(), user.getParola());
//    if (userR != null) {
//        if (loggedClients.get(userR.getId()) != null)
//            throw new BasketException("User already logged in.");
//        loggedClients.put(userR.getId(), client);
//
//        // Logging statement to verify the ID value
//        System.out.println("Server-side ID before creating response: " + userR.getId());
//
//        // Create PersoanaDTO with the correct id
//        PersoanaDTO persoanaDTO = new PersoanaDTO(userR.getId(), userR.getUsername(), userR.getParola());
//        System.out.println("Server-side ID DTO: " + persoanaDTO);
//        // Create the response with PersoanaDTO
//        Response response = new Response();
//        response.setType(ResponseType.LOGGED_IN);
//        response.setPersoana(persoanaDTO);
//
//        return userR;
//    } else {
//        throw new BasketException("Authentication failed.");
//    }
//}


    public synchronized void logOut(Persoana user, IBasketObserver client) throws BasketException {
        IBasketObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new BasketException("User "+user.getId()+" is not logged in.");
        //notifyFriendsLoggedOut(user);
    }

    public synchronized void vanzareBilet(Bilet bilet) throws BasketException {
        System.out.println("in service "+bilet.getMeci());
        Meci meci=repoMeci.findOne(bilet.getMeci());
        if(meci.getNrLocuri()!=0){
            repoBilet.save(bilet);
            notifyVanzare(bilet);
        }
        else{
            throw new BasketException("Sold out match!");
        }
    }

    private void notifyVanzare(Bilet bilet) throws BasketException {
        Iterable<Meci> meciuri=repoMeci.findAll();
        System.out.println("Meciuri "+meciuri);

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);

        for (IBasketObserver client : loggedClients.values()) {
            executor.execute(() -> {
                try {
                    client.biletVandut(bilet);
                } catch (BasketException e) {
                    System.err.println("Error notifying client about ticket sold: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
    }

    public synchronized Meci[] findAll() throws BasketException{
        try{
            Iterable<Meci> allIterable=repoMeci.findAll();
            int count = 0;
            for (Meci meci : allIterable) {
                count++;
            }
            Meci[] disponibileArray = new Meci[count];
            int index = 0;
            for (Meci meci : allIterable) {
                disponibileArray[index++] = meci;
            }

            return disponibileArray;
        }catch (Exception ex){
            throw new BasketException("findAll error: "+ex.getMessage());
        }
    }

    public synchronized Meci[] cautaDisponibile() throws BasketException{
        try{
            Iterable<Meci> disponibileIterable=repoMeci.findDisponibile();
            int count = 0;
            for (Meci meci : disponibileIterable) {
                count++;
            }
            Meci[] disponibileArray = new Meci[count];
            int index = 0;
            for (Meci meci : disponibileIterable) {
                disponibileArray[index++] = meci;
            }

            return disponibileArray;
        }catch (Exception ex){
            throw new BasketException("cautaDisponibile error: "+ex.getMessage());
        }
    }
}
