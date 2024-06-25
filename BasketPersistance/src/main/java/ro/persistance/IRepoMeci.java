package ro.persistance;

import ro.model.Meci;

public interface IRepoMeci extends IRepository<Integer, Meci>{
    Iterable<Meci> findDisponibile();
}

