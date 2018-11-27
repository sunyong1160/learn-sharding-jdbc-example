package learn.sharding.jdbc.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 */
@SpringBootApplication
@MapperScan("learn.sharding.jdbc.example.mapper")
@EnableScheduling
//@ImportResource(locations = {"classpath:sharding-jdbc.xml"}) // xml形式
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
