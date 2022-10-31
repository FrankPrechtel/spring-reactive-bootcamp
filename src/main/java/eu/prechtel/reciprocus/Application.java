package eu.prechtel.reciprocus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.r2dbc.core.DatabaseClient;

import javax.annotation.PostConstruct;
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
                "DROP TABLE IF EXISTS gingerbread;",
                "CREATE TABLE gingerbread (id SERIAL PRIMARY KEY, flavor VARCHAR(255));",
                "INSERT INTO gingerbread VALUES (1, 'cinnamon');"
        );
        statements.forEach(it -> databaseClient.sql(it)
                .fetch()
                .rowsUpdated().block());

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
