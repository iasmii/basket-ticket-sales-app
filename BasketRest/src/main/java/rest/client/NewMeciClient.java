package rest.client;

import basket.services.rest.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ro.model.Meci;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class NewMeciClient {
    RestClient restClient = RestClient.builder().
            requestInterceptor(new CustomRestClientInterceptor()).
            build();

    public static final String URL = "http://localhost:8080/basket/meciuri";
    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    public Meci[] getAll() {
        return execute(() -> restClient.get().uri(URL).retrieve().body( Meci[].class));
    }

    public Meci getById(int id) {

        return execute(() -> restClient.get(). uri(String.format("%s/%d", URL, id)).retrieve().body( Meci.class));
    }

    public Meci create(Meci meci) {
        return execute(() -> restClient.post().uri(URL).contentType(APPLICATION_JSON).body(meci).retrieve().body(Meci.class));
    }


    public void delete(int id){
        execute(() -> restClient.delete().uri(String.format("%s/%d", URL, id)).retrieve().toBodilessEntity());
    }

    public Meci update(Meci meci) {
        return execute(() -> restClient.put().uri(String.format("%s/%d", URL, meci.getId()))
                .contentType(APPLICATION_JSON).body(meci).retrieve().body(Meci.class));
    }

    public class CustomRestClientInterceptor
            implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(
                HttpRequest request,
                byte[] body,
                ClientHttpRequestExecution execution) throws IOException {
            System.out.println("Sending a "+request.getMethod()+ " request to "+request.getURI()+ " and body ["+new String(body)+"]");
            ClientHttpResponse response=null;
            try {
                response = execution.execute(request, body);
                System.out.println("Got response code " + response.getStatusCode());
            }catch(IOException ex){
                System.err.println("Eroare executie "+ex);
            }
            return response;
        }
    }
}
