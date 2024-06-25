package ro.network.utils;

import ro.network.jsonprotocol.BasketClientJsonWorker;
import ro.services.IBasketService;

import java.net.Socket;

public class BasketJsonConcurrentServer extends AbsConcurrentServer{
    private IBasketService basketServer;
    public BasketJsonConcurrentServer(int port, IBasketService basketServer) {
        super(port);
        this.basketServer = basketServer;
        System.out.println("Basket- BasketJsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        BasketClientJsonWorker worker=new BasketClientJsonWorker(basketServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }
}
