package learn.sharding.jdbc.example.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by sunyong on 2018-11-08.
 */
@Component(value = "kafkaSender")
public class KafkaSender {

    @Value("${kafka.third.topic.self}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String key, String msg) {
        kafkaTemplate.send(topic, key, msg);
    }
}
