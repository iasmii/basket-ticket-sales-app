package ro.network.jsonprotocol;

import com.google.gson.Gson;
import ro.model.Bilet;
import ro.model.Meci;
import ro.model.Persoana;
import ro.network.dto.BiletDTO;
import ro.network.dto.DTOUtils;
import ro.network.dto.PersoanaDTO;
import ro.services.BasketException;
import ro.services.IBasketObserver;
import ro.services.IBasketService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class BasketClientJsonWorker implements Runnable, IBasketObserver {
    private IBasketService server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;
    public BasketClientJsonWorker(IBasketService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter=new Gson();
        try{
            output=new PrintWriter(connection.getOutputStream());
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                String requestLine=input.readLine();
                Request request=gsonFormatter.fromJson(requestLine, Request.class);
                Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    public void biletVandut(Bilet bilet) throws BasketException {

        Response resp= JsonProtocolUtils.createBiletVandutResponse(bilet);
        System.out.println("Bilet vandut: "+bilet);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new BasketException("Vanzare bilet error: "+e);
        }
    }

    private static Response okResponse=JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request){
        Response response=null;
        if (request.getType()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.getType());
            PersoanaDTO udto=request.getPersoana();
            Persoana user= DTOUtils.getFromDTO(udto);
            System.out.println("AAAAAAAAAAAAAAAAAAAA Json worker: "+user);
            try {
                Persoana persoana=server.logIn(user, this);
                //return okResponse;
                System.out.println("AAAAAAAAAAAAAAAAAAAA Json worker: "+persoana);
                return JsonProtocolUtils.createLoginResponse(persoana);
            } catch (BasketException e) {
                connected=false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.LOGOUT){
            System.out.println("Logout request");

            PersoanaDTO udto=request.getPersoana();
            Persoana user=DTOUtils.getFromDTO(udto);
            try {
                server.logOut(user, this);
                connected=false;
                //return okResponse;
                return JsonProtocolUtils.createLogoutResponse(user);
            } catch (BasketException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.VANZARE_BILET){
            System.out.println("VanzareBiletRequest ...");
            BiletDTO mdto=(BiletDTO)request.getBilet();
            Bilet bilet=DTOUtils.getFromDTO(mdto);
            try {
                server.vanzareBilet(bilet);
                return okResponse;
            } catch (BasketException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType()== RequestType.GET_ALL){
            System.out.println("GetAll Request ...");
            try {
                Meci[] meciuri=server.findAll();

                return JsonProtocolUtils.createGetAllResponse(meciuri);
            } catch (BasketException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.GET_DISPONIBILE){
            System.out.println("GetDisponibile Request ...");
            try {
                Meci[] meciuri=server.cautaDisponibile();

                return JsonProtocolUtils.createGetDisponibileResponse(meciuri);
            } catch (BasketException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        String responseLine=gsonFormatter.toJson(response);
        System.out.println("sending response "+responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }
}
