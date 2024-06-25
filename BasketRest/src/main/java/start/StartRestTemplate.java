package start;

import org.springframework.web.client.RestClientException;
import rest.client.MeciClient;
import ro.model.Meci;
import basket.services.rest.ServiceException;

public class StartRestTemplate {
    private final static MeciClient meciuriClient=new MeciClient();
    public static void main(String[] args) {
        //  RestTemplate restTemplate=new RestTemplate();
        Meci meciTest=new Meci("test2024",4500, 35.5F);
        try{
            //  User result= restTemplate.postForObject("http://localhost:8080/chat/users",userT, User.class);

            //  System.out.println("Result received "+result);
      /*  System.out.println("Updating  user ..."+userT);
        userT.setName("New name 2");
        restTemplate.put("http://localhost:8080/chat/users/test124", userT);

*/
            // System.out.println(restTemplate.postForObject("http://localhost:8080/chat/users",userT, User.class));
            //System.out.println( restTemplate.postForObject("http://localhost:8080/chat/users",userT, User.class));
            System.out.println("Adding a new meci "+meciTest);
            show(()-> System.out.println(meciuriClient.create(meciTest)));
            System.out.println("\n  Printing all meciuri ...");
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
