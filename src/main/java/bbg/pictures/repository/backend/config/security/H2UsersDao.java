package bbg.pictures.repository.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class H2UsersDao { //TODO Only for testing purposes. Remove this once migrating to proper db.
    @Autowired
    public H2UsersDao(final JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS "
                + "USERS(username VARCHAR(255) PRIMARY KEY, password VARCHAR(255), enabled BOOLEAN);");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS "
                + "authorities(username VARCHAR(255), authority  VARCHAR(255), foreign key (username) references users(username));");
    }
}
