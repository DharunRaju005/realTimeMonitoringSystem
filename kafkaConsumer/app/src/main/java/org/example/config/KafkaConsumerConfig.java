package org.example.config;

import org.apache.kafka.common.serialization.Deserializer;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka //Enables Kafka listener support in Spring Boot.
public class KafkaConsumerConfig {
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(){
        return registry -> registry.config().commonTags("application", "MYAPPNAME");
    }



    //Creates a Kafka ConsumerFactory, which is responsible for creating Kafka consumers.
    //Configurations:
        //BOOTSTRAP_SERVERS_CONFIG → Kafka broker URL (localhost:9092).
        //KEY_DESERIALIZER_CLASS_CONFIG → Deserializes message keys (converts bytes to String).
        //VALUE_DESERIALIZER_CLASS_CONFIG → Deserializes message values (converts bytes to String).
        //GROUP_ID_CONFIG → Consumers with the same group ID will consume messages together.
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.19.215:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "mygroup");
        return new DefaultKafkaConsumerFactory<>(props);
    }


    //This creates a Kafka listener container factory, which is needed to run Kafka consumers in a multi-threaded environment.
    //It uses the ConsumerFactory (defined earlier) to create listeners.
    //listen to Kafka topics asynchronously.
    //It connects it to a Kafka consumer using consumerFactory().
    //ConcurrentKafkaListenerContainerFactory allows handling multiple Kafka messages in parallel.
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


}
