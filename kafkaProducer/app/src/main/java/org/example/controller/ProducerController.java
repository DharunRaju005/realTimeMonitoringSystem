package org.example.controller;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("producer")
public class ProducerController {
    private final String TOPIC_NAME_NORMAL="normal";
    private final String TOPIC_NAME_URGENT="urgent";

    //mobile
    //private final String BOOTSTRAP_SERVERS="192.168.19.215:9092";
    //psg
    private final String BOOTSTRAP_SERVERS="10.1.222.78:9092";

    //kafkaBroker
    private final Producer<String, String> kafkaProducer;

    public ProducerController() {
//        A serializer in Kafka transforms an object into a byte array so it can be:
    //        1.Sent by the producer over the network.
    //        2.Stored in Kafka topics in a compact format.
    //        3.Deserialized by consumers when reading from Kafka.
//        Kafka requires serializers because it only understands byte arrays, but we often work with structured data like JSON, Avro, or Java objects.
        Properties props=new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.kafkaProducer = new KafkaProducer<>(props);
//        StringSerializer converts Java String into bytes before sending.
//        The consumer will use StringDeserializer to convert it back into a String.
    }

    @PostMapping("/event/normal")
    public String produceNormalEvent() {
        sendEventToKafka(TOPIC_NAME_NORMAL,"Button CLiecked: Enroll Now");
        return "normal button click event is being sent to kafka";
    }

    @PostMapping("/event/urgent")
    public String produceUrgentEvent() {
        sendEventToKafka(TOPIC_NAME_URGENT,"Button CLiecked: Buy Now");
        return "urgent button click event is being sent to kafka";
    }

    private void sendEventToKafka(String topic, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic,"buttonClick", message); //public ProducerRecord(String topic, K key, V value)
        kafkaProducer.send(record,(metadata, exception) -> {
            if (exception != null) {
                System.out.println("Error in sending message to kafka "+exception.getMessage());
                exception.printStackTrace();
            }else{
                System.out.println("Message sent to kafka messge: "+message + " to topic: "+topic+" offset: "+metadata.offset());
            }
        });
    }


}
