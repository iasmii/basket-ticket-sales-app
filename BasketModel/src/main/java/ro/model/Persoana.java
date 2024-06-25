package ro.model;

import java.io.Serializable;

public class Persoana implements Identifiable<Integer>{
    private int id;
    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public Integer getId() {
        return id;
    }
    String username;
    String parola;

    public Persoana(String username,String parola){
        this.username=username;
        this.parola=parola;
    }

    public Persoana(int id,String username,String parola){
        this.id=id;
        this.username=username;
        this.parola=parola;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "Persoana{" + id+
                " username='" + username + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}
