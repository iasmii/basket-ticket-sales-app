package ro.network.dto;

import java.io.Serializable;

public class BiletDTO implements Serializable {
    private int id;
    public void setId(Integer id) {this.id=id;}
    public Integer getId() {return id;}

    String numeClient;
    int meci;
    int nrLocuri;

    public BiletDTO(int meci, String numeClient, int nrLocuri){
        this.meci=meci;
        this.numeClient=numeClient;
        this.nrLocuri=nrLocuri;
    }

    public BiletDTO(int id,int meci, String numeClient, int nrLocuri){
        this.id=id;
        this.meci=meci;
        this.numeClient=numeClient;
        this.nrLocuri=nrLocuri;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public int getMeci() {
        return meci;
    }

    public void setMeci(int meci) {
        this.meci = meci;
    }

    public int getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "BiletDTO{" +
                "numeClient='" + numeClient + '\'' +
                ", meci=" + meci +
                ", nrLocuri=" + nrLocuri +
                '}';
    }
}
