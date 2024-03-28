package eu.prechtel.bootcamp;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.r2dbc.core.DatabaseClient;

import java.util.Arrays;
import java.util.List;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class Application {

    final DatabaseClient databaseClient;

    Application(@Autowired DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @PostConstruct
    public void init() {
        List<String> statements = Arrays.asList(
                "DROP TABLE IF EXISTS Invoice;",
                "CREATE TABLE Invoice (id SERIAL PRIMARY KEY, type VARCHAR(255));",
                "INSERT INTO Invoice VALUES (1, 'PROFORMA');"
        );
        statements.forEach(it -> databaseClient.sql(it)
                .fetch()
                .rowsUpdated().block());

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
