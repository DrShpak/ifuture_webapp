package com.ifuture.webclient;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Random;

@Data
public class Client implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private String URL = "http://localhost:8080/";
    private final RestTemplate template;

    public Client (RestTemplate template, int id) {
        System.out.println(URL);
        this.template = template;
        this.URL += id;
    }

    @Override
    public void run() {
        var delay = new Random();
        while (true) {
            var balance = template.getForObject(URL, Long.class);
            log.info(String.valueOf(balance));
            try {
                Thread.sleep(delay.nextInt(2000) + 1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(URL, client.URL) && Objects.equals(template, client.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(URL, template);
    }
}