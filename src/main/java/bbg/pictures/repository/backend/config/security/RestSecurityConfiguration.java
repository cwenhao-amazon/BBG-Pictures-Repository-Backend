package bbg.pictures.repository.backend.config.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class RestSecurityConfiguration {
    @Bean
    public UserDetailsManager userDetailsService(final PasswordEncoder passwordEncoder,
                                                 final DataSource dataSource,
                                                 final JdbcTemplate jdbcTemplate) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        final UserDetails admin = User.withUsername("admin") //TODO Only for testing purposes. Remove this when going into prod
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        users.setJdbcTemplate(jdbcTemplate);
        users.createUser(admin);

        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auths -> auths
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/user")).permitAll()
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults()) //TODO Swap to commented implementation once frontend is working
//            .formLogin(form -> form
//                    .loginPage("/login").permitAll())
            .csrf(AbstractHttpConfigurer::disable); //TODO Only for testing purposes. Enable csrf once frontend works and is deployed to prod


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}