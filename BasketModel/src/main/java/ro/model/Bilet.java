package ro.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

@Entity
@Table(name="bilet")
public class Bilet implements Identifiable<Integer>{
    private int id;
    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    @Id
    @GeneratedValue(generator = "increment")
    public Integer getId() {
        return id;
    }
    String numeClient;
    int meci;
    int nrLocuri;

    public Bilet(){}

    public Bilet(int meci, String numeClient, int nrLocuri){
        this.meci=meci;
        this.numeClient=numeClient;
        this.nrLocuri=nrLocuri;
    }

    public Bilet(int id,int meci, String numeClient, int nrLocuri){
        this.id=id;
        this.meci=meci;
        this.numeClient=numeClient;
        this.nrLocuri=nrLocuri;
    }

    @Column(name="client")
    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    @Column(name="meci")
    public int getMeci() {
        return meci;
    }

    public void setMeci(int meci) {
        this.meci = meci;
    }

    @Column(name="locuri")
    public int getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "numeClient='" + numeClient + '\'' +
                ", meci=" + meci +
                ", nrLocuri=" + nrLocuri +
                '}';
    }
}
