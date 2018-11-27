package learn.sharding.jdbc.example.annotation;

import java.lang.annotation.*;

/**
 * Created by sunyong on 2018-11-20.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {

    String value() default "";

}
