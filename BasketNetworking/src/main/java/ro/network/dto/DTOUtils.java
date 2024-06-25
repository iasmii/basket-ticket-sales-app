package ro.network.dto;

import ro.model.Bilet;
import ro.model.Meci;
import ro.model.Persoana;

public class DTOUtils {
    public static Bilet getFromDTO(BiletDTO entity){
        String numeClient=entity.getNumeClient();
        int meci=entity.getMeci();
        int nrLocuri= entity.getNrLocuri();
        int id=entity.getId();
        return new Bilet(id,meci,numeClient,nrLocuri);
    }

    public static BiletDTO getDTO(Bilet entity){
        String numeClient=entity.getNumeClient();
        int meci=entity.getMeci();
        int nrLocuri= entity.getNrLocuri();
        int id=entity.getId();
        return new BiletDTO(id,meci,numeClient,nrLocuri);
    }

    public static Meci getFromDTO(MeciDTO entity){
        String nume=entity.getNume();
        int nrLocuri=entity.getNrLocuri();
        float pret=entity.getPret();
        int id=entity.getId();
        return new Meci(id,nume,nrLocuri,pret);
    }

    public static MeciDTO getDTO(Meci entity){
        String nume=entity.getNume();
        int nrLocuri=entity.getNrLocuri();
        float pret=entity.getPret();
        int id=entity.getId();
        return new MeciDTO(id,nume,nrLocuri,pret);
    }

    public static Persoana getFromDTO(PersoanaDTO entity){
        String username=entity.getUsername();
        String parola=entity.getParola();
        int id=entity.getId();
        return new Persoana(id,username,parola);
    }

    public static PersoanaDTO getDTO(Persoana entity){
        String username=entity.getUsername();
        String parola=entity.getParola();
        int id=entity.getId();
        return new PersoanaDTO(id,username,parola);
    }

    public static MeciDTO[] getDTO(Meci[] users){
        MeciDTO[] meciuriDTO=new MeciDTO[users.length];
        for(int i=0;i<users.length;i++)
            meciuriDTO[i]=getDTO(users[i]);
        return meciuriDTO;
    }

    public static Meci[] getFromDTO(MeciDTO[] users){
        Meci[] meciuri=new Meci[users.length];
        for(int i=0;i<users.length;i++){
            meciuri[i]=getFromDTO(users[i]);
        }
        return meciuri;
    }
}
