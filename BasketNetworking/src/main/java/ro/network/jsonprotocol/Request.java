package ro.network.jsonprotocol;

import ro.network.dto.BiletDTO;
import ro.network.dto.MeciDTO;
import ro.network.dto.PersoanaDTO;

import java.util.Arrays;

public class Request {
    private RequestType type;
    private BiletDTO bilet;
    private PersoanaDTO persoana;
    private MeciDTO meci;
    private MeciDTO[] meciuri;

    public Request(){}
    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public PersoanaDTO getPersoana() {
        return persoana;
    }

    public void setPersoana(PersoanaDTO persoana) {
        this.persoana = persoana;
    }

    public BiletDTO getBilet() {
        return bilet;
    }

    public void setBilet(BiletDTO bilet) {
        this.bilet = bilet;
    }

    public MeciDTO getMeci() {
        return meci;
    }

    public void setMeci(MeciDTO meci) {
        this.meci = meci;
    }

    public MeciDTO[] getMeciuri() {
        return meciuri;
    }

    public void setMeciuri(MeciDTO[] meciuri) {
        this.meciuri = meciuri;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", persoana=" + persoana +
                ", bilet=" + bilet +
                ", meci=" + meci +
                ", meciuri=" + Arrays.toString(meciuri) +
                '}';
    }
}
