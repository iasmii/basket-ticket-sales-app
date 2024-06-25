package ro.services;

public interface IBasketService {
    ro.model.Persoana logIn(ro.model.Persoana persoana, ro.services.IBasketObserver iBasketObserver) throws ro.services.BasketException;

    void vanzareBilet(ro.model.Bilet bilet) throws ro.services.BasketException;

    void logOut(ro.model.Persoana persoana, ro.services.IBasketObserver iBasketObserver) throws ro.services.BasketException;

    ro.model.Meci[] cautaDisponibile() throws ro.services.BasketException;

    ro.model.Meci[] findAll() throws ro.services.BasketException;//Iterable<ro.model.Meci>  ro.model.Meci[]
}
