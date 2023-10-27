package com.poly.datn.config;

import com.poly.datn.entity.Role;
import com.poly.datn.entity.User;
import com.poly.datn.repository.RoleRepository;
import com.poly.datn.repository.UserRepository;
import com.poly.datn.service.UserService;
import com.poly.datn.service.impl.UserServiceImpl;
import com.poly.datn.util.RoleUtil;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SercurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(c -> c.requestMatchers("/admin/**")
                .hasRole("ADMIN")
                .anyRequest().permitAll()
            )
                .formLogin( c -> c.loginPage("/login").defaultSuccessUrl("/home",true))
                .logout(c -> c.logoutUrl("/logout").logoutSuccessUrl("/home")).build();

//            .formLogin(c -> c.loginPage("/login")
//                .successForwardUrl("/home"))
//            .logout(lo -> lo.logoutUrl("/logout"))
//            .httpBasic(Customizer.withDefaults())
//            .build();
//        return http.csrf().disable().authorizeRequests().anyRequest().permitAll().and().build();
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
    public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // initial roles
            if (roleRepository.count() < 2) {
                roleRepository.saveAll(Arrays.asList(Role.builder()
                                .roleName("ROLE_USER")
                                .build(),
                        Role.builder()
                                .roleName("ROLE_ADMIN")
                                .build()
                ));
            }
            // initial default user "admin"
            if (userRepository.getByUser("admin").isEmpty())
                userRepository.save(User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123456"))
                        .avatar("/assets/images/users/avatar-2.jpg")
                        .name("FourStore Shop")
                        .roles(Collections.singletonList(roleRepository
                                .getByName(RoleUtil.ADMIN.getValue())))
                        .build()
                );
        };
    }
}
