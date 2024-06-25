package ro.network.jsonprotocol;

import com.google.gson.Gson;
import ro.model.Persoana;
import ro.model.Bilet;
import ro.model.Meci;
import ro.network.dto.DTOUtils;
import ro.network.dto.MeciDTO;
import ro.network.dto.PersoanaDTO;
import ro.services.BasketException;
import ro.services.IBasketObserver;
import ro.services.IBasketService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BasketServicesJsonProxy implements IBasketService {
    private String host;
    private int port;

    private IBasketObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public BasketServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    public Persoana logIn(Persoana user, IBasketObserver client) throws BasketException {
        initializeConnection();
        //System.out.println("fac ceva");
        Request req= JsonProtocolUtils.createLoginRequest(user);
        sendRequest(req);
        //System.out.println("CCCCCCCCCC services json"+user.getId()+user);
        Response response=readResponse();
        //System.out.println(response);
        if (response.getType()== ResponseType.LOGGED_IN){
            this.client=client;
        }
        //System.out.println("fac ceva");
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            closeConnection();
            throw new BasketException(err);
        }
        PersoanaDTO persoanaDTO=response.getPersoana();
        Persoana persoana= DTOUtils.getFromDTO(persoanaDTO);
        return persoana;
    }

    public void vanzareBilet(Bilet bilet) throws BasketException {

        Request req=JsonProtocolUtils.createBiletVandutRequest(bilet);
        sendRequest(req);
        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new BasketException(err);
        }
    }

    public void logOut(Persoana user, IBasketObserver client) throws BasketException {

        Request req=JsonProtocolUtils.createLogoutRequest(user);
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new BasketException(err);
        }
    }

    @Override
    public Meci[] cautaDisponibile() throws BasketException {
        Request req=JsonProtocolUtils.createGetDisponibileRequest();
        sendRequest(req);
        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new BasketException(err);
        }
        MeciDTO[] meciuriDTO=response.getMeciuri();
        Meci[] meciuri= DTOUtils.getFromDTO(meciuriDTO);
        return meciuri;
    }

    @Override
    public Meci[] findAll() throws BasketException {
        Request req=JsonProtocolUtils.createGetAllRequest();
        sendRequest(req);
        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new BasketException(err);
        }
        MeciDTO[] meciuriDTO=response.getMeciuri();
        Meci[] meciuri= DTOUtils.getFromDTO(meciuriDTO);
        return meciuri;
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws BasketException {
        String reqLine=gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new BasketException("Error sending object "+e);
        }

    }

    private Response readResponse() throws BasketException {
        Response response=null;
        try{

            response=qresponses.take();
            System.out.println(response);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws BasketException {
        try {
            gsonFormatter=new Gson();
            connection=new Socket(host,port);
            output=new PrintWriter(connection.getOutputStream());
            output.flush();
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response){
        if (response.getType()== ResponseType.BILET_VANDUT){
            Bilet bilet=DTOUtils.getFromDTO(response.getBilet());
            System.out.println("Bilet vandut: "+bilet);
            try {
                client.biletVandut(bilet);
            } catch (BasketException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.getType()== ResponseType.BILET_VANDUT;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    String responseLine=input.readLine();
                    System.out.println("response received "+responseLine);
                    Response response=gsonFormatter.fromJson(responseLine, Response.class);
                    if (isUpdate(response)){
                        handleUpdate(response);
                    }else{

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
