package ro.network.protobufProtocol;

import ro.model.Meci;
import ro.model.Persoana;
import ro.model.Bilet;

public class ProtoUtils {
    public static BasketProtobuffs.BasketRequest createLoginRequest(Persoana persoana){
        BasketProtobuffs.Persoana persoanaDTO=BasketProtobuffs.Persoana.newBuilder().setUsername(persoana.getUsername()).setParola(persoana.getParola()).build();
        BasketProtobuffs.BasketRequest request=BasketProtobuffs.BasketRequest.newBuilder().setType(BasketProtobuffs.BasketRequest.Type.Login)
                .setPersoana(persoanaDTO).build();
        return request;
    }

    public static BasketProtobuffs.BasketRequest createLogoutRequest(Persoana persoana){
        BasketProtobuffs.Persoana persoanaDTO=BasketProtobuffs.Persoana.newBuilder().setUsername(persoana.getUsername()).setParola(persoana.getParola()).build();
        BasketProtobuffs.BasketRequest request=BasketProtobuffs.BasketRequest.newBuilder().setType(BasketProtobuffs.BasketRequest.Type.Logout)
                .setPersoana(persoanaDTO).build();
        return request;
    }

    public static BasketProtobuffs.BasketRequest createVanzareBiletRequest(Bilet bilet){
        BasketProtobuffs.Bilet biletDTO=BasketProtobuffs.Bilet.newBuilder()
                .setMeci(bilet.getMeci())
                .setNumeClient(bilet.getNumeClient())
                .setNrLocuri(bilet.getNrLocuri()).build();
        BasketProtobuffs.BasketRequest request=BasketProtobuffs.BasketRequest.newBuilder()
                .setType(BasketProtobuffs.BasketRequest.Type.VindeBilet)
                .setBilet(biletDTO).build();
        return request;
    }

    public static BasketProtobuffs.BasketRequest createGetAllRequest(){
        BasketProtobuffs.BasketRequest request=BasketProtobuffs.BasketRequest.newBuilder()
                .setType(BasketProtobuffs.BasketRequest.Type.GetAll).build();
        return request;
    }

    public static BasketProtobuffs.BasketRequest createGetDisponibileRequest(){
        BasketProtobuffs.BasketRequest request=BasketProtobuffs.BasketRequest.newBuilder()
                .setType(BasketProtobuffs.BasketRequest.Type.GetDisponibile).build();
        return request;
    }

    public static BasketProtobuffs.BasketResponse createOkResponse(){
        BasketProtobuffs.BasketResponse response=BasketProtobuffs.BasketResponse.newBuilder()
                .setType(BasketProtobuffs.BasketResponse.Type.Ok).build();
        return response;
    }

    public static BasketProtobuffs.BasketResponse createErrorResponse(String text){
        BasketProtobuffs.BasketResponse response=BasketProtobuffs.BasketResponse.newBuilder()
                .setType(BasketProtobuffs.BasketResponse.Type.Error)
                .setError(text).build();
        return response;
    }

    public static BasketProtobuffs.BasketResponse createLoggedinResponse(Persoana persoana){
        BasketProtobuffs.Persoana persoanaDTO=BasketProtobuffs.Persoana.newBuilder().setUsername(persoana.getUsername()).setParola(persoana.getParola()).build();

        BasketProtobuffs.BasketResponse response= BasketProtobuffs.BasketResponse.newBuilder().setType(BasketProtobuffs.BasketResponse.Type.Loggedin).setPersoana(persoanaDTO).build();
        return response;
    }

    public static BasketProtobuffs.BasketResponse createLoggedoutResponse(Persoana persoana){
        BasketProtobuffs.Persoana persoanaDTO=BasketProtobuffs.Persoana.newBuilder().setUsername(persoana.getUsername()).setParola(persoana.getParola()).build();

        BasketProtobuffs.BasketResponse response= BasketProtobuffs.BasketResponse.newBuilder().setType(BasketProtobuffs.BasketResponse.Type.Loggedout).setPersoana(persoanaDTO).build();
        return response;
    }

    public static BasketProtobuffs.BasketResponse createVanzareBiletResponse(Bilet bilet){
        BasketProtobuffs.Bilet biletDTO=BasketProtobuffs.Bilet.newBuilder()
                .setMeci(bilet.getMeci())
                .setNrLocuri(bilet.getNrLocuri())
                .setNumeClient(bilet.getNumeClient()).build();

        BasketProtobuffs.BasketResponse response= BasketProtobuffs.BasketResponse.newBuilder().setType(BasketProtobuffs.BasketResponse.Type.BiletVandut).setBilet(biletDTO).build();
        return response;
    }

    public static BasketProtobuffs.BasketResponse createGetAllResponse(Meci[] meciuri){
        BasketProtobuffs.BasketResponse.Builder response=BasketProtobuffs.BasketResponse.newBuilder()
                .setType(BasketProtobuffs.BasketResponse.Type.GetAll);
        for(Meci meci: meciuri){
            BasketProtobuffs.Meci meciDTO=BasketProtobuffs.Meci.newBuilder()
                    .setNrLocuri(meci.getNrLocuri())
                    .setNume(meci.getNume())
                    .setPret(meci.getPret()).build();
            response.addMeciuri(meciDTO);
        }
        return response.build();
    }

    public static BasketProtobuffs.BasketResponse createGetDisponibileResponse(Meci[] meciuri){
        BasketProtobuffs.BasketResponse.Builder response=BasketProtobuffs.BasketResponse.newBuilder()
                .setType(BasketProtobuffs.BasketResponse.Type.GetDisponibile);
        for(Meci meci: meciuri){
            BasketProtobuffs.Meci meciDTO=BasketProtobuffs.Meci.newBuilder()
                    .setNrLocuri(meci.getNrLocuri())
                    .setNume(meci.getNume())
                    .setPret(meci.getPret()).build();
            response.addMeciuri(meciDTO);
        }
        return response.build();
    }
}
