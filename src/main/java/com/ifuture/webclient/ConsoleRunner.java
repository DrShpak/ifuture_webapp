package com.ifuture.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsoleRunner implements CommandLineRunner {
    @Value("${rCount}")
    private int rCount;

    @Value("${wCount}")
    private int wCount;

    @Value("#{'${idList}'.split(',')}")
    private List<Integer> idList;

    @Override
    public void run(String... args) throws Exception {
        startThreads(fillClients(rCount));
        startThreads(fillClients(wCount));
    }

    private List<Thread> fillClients(int count) {
        var clients = new ArrayList<Thread>();
        for (Integer id : idList) {
            clients.add(new Thread(new Client(new RestTemplate(), id)));
        }
        return clients;
    }

    private void startThreads(List<Thread> clients) {
        clients.forEach(Thread::start);
    }
}
