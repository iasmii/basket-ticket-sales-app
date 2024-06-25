package ro.services;

public interface IBasketObserver {
    void biletVandut(ro.model.Bilet bilet) throws ro.services.BasketException;

    //void persoanaLoggedIn(ro.model.Persoana persoana) throws ro.services.BasketException;

    //void persoanaLoggedOut(ro.model.Persoana persoana) throws ro.services.BasketException;
}
