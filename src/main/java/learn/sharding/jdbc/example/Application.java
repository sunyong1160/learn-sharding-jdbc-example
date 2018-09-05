package learn.sharding.jdbc.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * Hello world!
 */
@SpringBootApplication
@MapperScan("learn.sharding.jdbc.example.mapper")
@ComponentScan("learn.sharding.jdbc.example")
@ImportResource(locations = {"classpath:sharding-jdbc.xml"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
