package rest.client;

import basket.services.rest.ServiceException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ro.model.Meci;

import java.io.IOException;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class MeciClient {
    public static final String URL = "http://localhost:8080/basket/meciuri";

    private RestTemplate restTemplate = new RestTemplate();

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
        return execute(() -> restTemplate.getForObject(URL, Meci[].class));
    }

    public Meci getById(int id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%d", URL, id), Meci.class));
    }

    public Meci create(Meci meci) {
        return execute(() -> restTemplate.postForObject(URL, meci, Meci.class));
    }

    public void update(Meci meci) {
        execute(() -> {
            restTemplate.put(String.format("%s/%d", URL, meci.getId()), meci);
            return null;
        });
    }

    public void delete(int id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%d", URL, id));
            return null;
        });
    }
}
