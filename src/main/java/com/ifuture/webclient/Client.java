package com.ifuture.webclient;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

@Data
public class Client implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private String URL_TO_GET_AMOUNT = "http://localhost:8080/";
    private String URL_TO_ADD_AMOUNT = "http://localhost:8080/amount/";
    private final RestTemplate template;
    private final int id;

    public Client (RestTemplate template, int id) {
        System.out.println(URL_TO_GET_AMOUNT);
        this.template = template;
        this.URL_TO_GET_AMOUNT += id;
        this.URL_TO_ADD_AMOUNT += id;
        this.id = id;
    }

    @Override
    public void run() {
        var delay = new Random();
        var value = new Random();
        while (true) {
            doGetBalance();
            makeDelay(delay);
            doAddAmountRequest(value.nextLong());
            makeDelay(delay);
        }
    }

    private void makeDelay(Random delay) {
        try {
            Thread.sleep(delay.nextInt(2000) + 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void doAddAmountRequest(Long value) {
        var requestEntity = new HttpEntity<>(value);
        template.patchForObject(URL_TO_ADD_AMOUNT, requestEntity,String.class);
    }

    private void doGetBalance() {
        template.getForObject(URL_TO_GET_AMOUNT, Long.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(URL_TO_GET_AMOUNT, client.URL_TO_GET_AMOUNT) && Objects.equals(template, client.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(URL_TO_GET_AMOUNT, template);
    }
}