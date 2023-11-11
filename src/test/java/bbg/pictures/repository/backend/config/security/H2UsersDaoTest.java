package bbg.pictures.repository.backend.config.security;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(classes = H2UsersDao.class)
class H2UsersDaoTest {
    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    void whenContextIsInitialized_shouldJdbcTemplateCreateTables() {
        verify(jdbcTemplate).execute("CREATE TABLE IF NOT EXISTS "
                + "USERS(username VARCHAR(255) PRIMARY KEY, password VARCHAR(255), enabled BOOLEAN);");
        verify(jdbcTemplate).execute("CREATE TABLE IF NOT EXISTS "
                + "authorities(username VARCHAR(255), authority  VARCHAR(255), foreign key (username) references users(username));");
    }
}