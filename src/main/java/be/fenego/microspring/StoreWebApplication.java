package be.fenego.microspring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:db-config.properties")
public class StoreWebApplication {

}
