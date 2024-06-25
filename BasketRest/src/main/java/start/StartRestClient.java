package start;

import org.springframework.web.client.RestClientException;
import rest.client.NewMeciClient;
import basket.services.rest.ServiceException;
import ro.model.Meci;

public class StartRestClient {
    private final static NewMeciClient meciuriClient=new NewMeciClient();
    public static void main(String[] args) {

        Meci meciTest=new Meci("test2024nou",4500,42);
        try{
            System.out.println("Adding a new meci "+meciTest);
            //show(()-> System.out.println(meciuriClient.create(meciTest)));

            Meci createdMeci = meciuriClient.create(meciTest);
            System.out.println("Created Meci: " + createdMeci);
            //meciTest=createdMeci;
            meciTest.setId(createdMeci.getId());

            System.out.println("\nPrinting all meciuri ...");
            show(()->{
                Meci[] res=meciuriClient.getAll();
                for(Meci m:res){
                    System.out.println(m);
                }
            });
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

        System.out.println("\nInfo for meci with id="+meciTest.getId());
        show(()-> System.out.println(meciuriClient.getById(meciTest.getId())));

        System.out.println("\nUpdating meci with id=" + meciTest.getId());
        meciTest.setNume("UPDATED");
        meciTest.setNrLocuri(5030);
        meciTest.setPret(50);
        show(() -> {
            Meci updatedMeci = meciuriClient.update(meciTest);
            System.out.println("Updated Meci: " + updatedMeci);
        });

        System.out.println("\nDeleting meci with id="+meciTest.getId());
        show(()-> meciuriClient.delete(meciTest.getId()));

    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }
}
