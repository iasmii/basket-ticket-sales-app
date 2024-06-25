package ro.model;

import java.io.Serializable;

public class Meci implements Identifiable<Integer>,Comparable<Meci>,Serializable{
    private int id;
    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public Integer getId() {
        return id;
    }
    String nume;
    int nrLocuri;
    float pret;

    public Meci(){}

    public Meci(String nume, int nrLocuri, float pret){
        this.nume=nume;
        this.nrLocuri=nrLocuri;
        this.pret=pret;
        //this.id=0;
    }

    public Meci(int id,String nume, int nrLocuri, float pret){
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
        return "Meci{ " +id+
                ", nume='" + nume + '\'' +
                ", nrLocuri=" + nrLocuri +
                ", pret=" + pret +
                '}';
    }

    @Override
    public int compareTo(Meci o) {
        return 0;
    }
}
