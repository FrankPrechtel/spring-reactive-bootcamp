package eu.prechtel.reciprocus;

import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class H2DataSourceConfiguration extends AbstractR2dbcConfiguration {

	@Bean
    public ConnectionFactory connectionFactory() {
		return H2ConnectionFactory.inMemory("testdb");
    }
}
