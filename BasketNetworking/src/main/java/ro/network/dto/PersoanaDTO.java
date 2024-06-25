package ro.network.dto;

import java.io.Serializable;

public class PersoanaDTO implements Serializable {
    private int id;
    public void setId(Integer id) {this.id=id;}
    public Integer getId() {return id;}

    String username;
    String parola;

    public PersoanaDTO(String username,String parola){
        this.username=username;
        this.parola=parola;
    }

    public PersoanaDTO(int id,String username,String parola){
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
        return "PersoanaDTO{" + id+
                " username='" + username + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}
