package learn.sharding.jdbc.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by sunyong on 2018-11-08.
 */
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "${kafka.third.topic.self}")
    public void receive(ConsumerRecord<String, String> record) {
        log.info("get message from kafka:topic==>{},partition==>{}, offset==>{},key==>{},value==>{}",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
    }
}
