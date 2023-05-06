package ro.unibuc.fmi.airlliantcore.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Profile("test")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableConfigurationProperties
public class TestWebSecurityConfigurerAdapter {

    private final UserSecurityConfig userSecurityConfig;

    @Autowired
    public TestWebSecurityConfigurerAdapter(UserSecurityConfig userSecurityConfig) {
        this.userSecurityConfig = userSecurityConfig;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        userSecurityConfig.getUsers()
                .stream()
                .map(user -> User.withUsername(user.getName())
                        .password(encoder.encode(user.getPass()))
                        .roles(user.getRoles()).build()
                )
                .forEach(manager::createUser);

        return manager;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/actuator/*").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .httpBasic();

        return http.build();

    }

}