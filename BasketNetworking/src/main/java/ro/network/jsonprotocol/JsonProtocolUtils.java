package ro.network.jsonprotocol;

import ro.model.Bilet;
import ro.model.Meci;
import ro.model.Persoana;
import ro.network.dto.DTOUtils;
import ro.network.dto.PersoanaDTO;

public class JsonProtocolUtils {
    public static Response createOkResponse(){
        Response resp=new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Response createErrorResponse(String errorMessage){
        Response resp=new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

    public static Response createLoginResponse(Persoana user){
        Response req=new Response();
        req.setType(ResponseType.LOGGED_IN);
        req.setPersoana(DTOUtils.getDTO(user));
        return req;
    }

    public static Response createLogoutResponse(Persoana user){
        Response req=new Response();
        req.setType(ResponseType.LOGGED_OUT);
        req.setPersoana(DTOUtils.getDTO(user));
        return req;
    }

    public static Response createBiletVandutResponse(Bilet bilet){
        Response req=new Response();
        req.setType(ResponseType.BILET_VANDUT);
        req.setBilet(DTOUtils.getDTO(bilet));
        return req;
    }

    public static Response createGetAllResponse(Meci[] meciuri){
        Response req=new Response();
        req.setType(ResponseType.GET_ALL);
        req.setMeciuri(DTOUtils.getDTO(meciuri));
        return req;
    }

    public static Response createGetDisponibileResponse(Meci[] meciuri){
        Response req=new Response();
        req.setType(ResponseType.GET_DISPONIBILE);
        req.setMeciuri(DTOUtils.getDTO(meciuri));
        return req;
    }

    //requests

    public static Request createLoginRequest(Persoana user){
        Request req=new Request();
        req.setType(RequestType.LOGIN);
        req.setPersoana(DTOUtils.getDTO(user));
        return req;
    }

    public static Request createLogoutRequest(Persoana user){
        Request req=new Request();
        req.setType(RequestType.LOGOUT);
        req.setPersoana(DTOUtils.getDTO(user));
        return req;
    }

    public static Request createBiletVandutRequest(Bilet bilet){
        Request req=new Request();
        req.setType(RequestType.VANZARE_BILET);
        req.setBilet(DTOUtils.getDTO(bilet));
        return req;
    }

//    public static Request createGetAllRequest(Meci[] meciuri){
//        Request req=new Request();
//        req.setType(RequestType.GET_ALL);
//        req.setMeciuri(DTOUtils.getDTO(meciuri));
//        return req;
//    }
//
//    public static Request createGetDisponibileRequest(Meci[] meciuri){
//        Request req=new Request();
//        req.setType(RequestType.GET_DISPONIBILE);
//        req.setMeciuri(DTOUtils.getDTO(meciuri));
//        return req;
//    }
        public static Request createGetAllRequest(){
            Request req=new Request();
            req.setType(RequestType.GET_ALL);
            //req.setMeciuri(DTOUtils.getDTO(meciuri));
            return req;
        }

    public static Request createGetDisponibileRequest(){
        Request req=new Request();
        req.setType(RequestType.GET_DISPONIBILE);
        //req.setMeciuri(DTOUtils.getDTO(meciuri));
        return req;
    }
}
