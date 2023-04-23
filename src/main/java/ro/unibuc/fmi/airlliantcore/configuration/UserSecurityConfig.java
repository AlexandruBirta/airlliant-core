package ro.unibuc.fmi.airlliantcore.configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security")
public class UserSecurityConfig {

    private final List<AuthenticatingUser> users = new ArrayList<>();

    @Getter
    @Setter
    static class AuthenticatingUser {

        private String name;
        private String pass;
        private String roles;

    }

}