package ro.network.dto;

import java.io.Serializable;

public class MeciDTO implements Serializable {
    private int id;
    public void setId(Integer id) {this.id=id;}
    public Integer getId() {return id;}

    String nume;
    int nrLocuri;
    float pret;

    public MeciDTO(String nume, int nrLocuri, float pret){
        this.nume=nume;
        this.nrLocuri=nrLocuri;
        this.pret=pret;
    }

    public MeciDTO(int id,String nume, int nrLocuri, float pret){
        this.id=id;
        this.nume=nume;
        this.nrLocuri=nrLocuri;
        this.pret=pret;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    public float getPret() {
        return pret;
    }

    public void setPret(float pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "MeciDTO{" +
                "nume='" + nume + '\'' +
                ", nrLocuri=" + nrLocuri +
                ", pret=" + pret +
                '}';
    }
}
