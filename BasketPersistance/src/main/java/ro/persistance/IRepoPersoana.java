package ro.persistance;

import ro.model.Persoana;

public interface IRepoPersoana extends IRepository<Integer, Persoana>{
    Persoana logIn(String username, String parola);
}