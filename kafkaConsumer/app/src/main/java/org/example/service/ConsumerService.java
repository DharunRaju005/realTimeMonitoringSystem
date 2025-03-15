package org.example.service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import io.prometheus.client.Counter;

import java.io.IOException;

@Service
public class ConsumerService {
    private final Counter normalCounter;
    private final Counter urgentCounter;
    public ConsumerService() throws IOException {
        this.normalCounter=Counter
                .build()
                .name("kafka_normal_events_total")
                .help("total normal events consumed by kafka")
                .register();
        this.urgentCounter=Counter
                .build()
                .name("kafka_urgent_events_total")
                .help("total urgent events consumed by kafka")
                .register();
    }

    @KafkaListener(topics="normal",groupId = "mygroup")
    public void listenNormalTopic(String eventData){
        try{
            System.out.println("got the  normal event "+eventData);
            normalCounter.inc();
        }
        catch(Exception e){
            System.out.println("error in normal event "+e.getMessage());
            e.printStackTrace();
        }
    }

    @KafkaListener (topics="urgent",groupId="mygroup")
    public void listenUrgentTopic(String eventData){
        try{
            System.out.println("got the  urgent event "+eventData);
            urgentCounter.inc();
        }
        catch(Exception e){
            System.out.println("error in urgent event "+e.getMessage());
            e.printStackTrace();
        }
    }

}
