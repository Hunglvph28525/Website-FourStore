package com.poly.datn.config;

import com.poly.datn.entity.Role;
import com.poly.datn.entity.User;
import com.poly.datn.repository.RoleRepository;
import com.poly.datn.service.UserService;
import com.poly.datn.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SercurityConfig {

    @Autowired
    private UserService userService;
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserServiceImpl();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c->c.requestMatchers("/admin/**")
                        .hasRole("ADMIN")
                        .anyRequest().permitAll()

                )
                .formLogin(c->c.loginPage("/login"))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean

    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }


    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository ,PasswordEncoder passwordEncoder){

        return args -> {
            System.out.println( "\n\n\t"+ userService.loadUserByUsername("admin").getUsername());
            if (userService.getAll().isEmpty())

            userService.add(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .roles(Arrays.asList(Role.builder()
                        .roleName("ROLE_ADMIN")
                        .build()))

                .build()
            );
                if (roleRepository.count()<2) {
                    roleRepository.save(Role.builder()
                            .roleName("ROLE_USER")
                            .build());
                }
        };
    }
}
