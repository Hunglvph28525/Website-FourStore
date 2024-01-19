package com.poly.datn.config;

import com.poly.datn.entity.PaymentMethod;
import com.poly.datn.entity.Role;
import com.poly.datn.entity.User;
import com.poly.datn.repository.PaymentMethodRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
                        .requestMatchers("/cart/**","/checkout/**","/invoice/**","/tai-khoan")
                        .hasAnyRole("ADMIN", "USER","NV")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().permitAll()
                )
                .formLogin(c -> c.loginPage("/login").defaultSuccessUrl("/home", false))
                .logout(c -> c.logoutUrl("/logout").logoutSuccessUrl("/login").deleteCookies("JSESSIONID")).httpBasic(Customizer.withDefaults()).build();

//            http.formLogin()
//            .logout(lo -> lo.logoutUrl("/logout"))
//
//            .build();
//        return http.csrf().disable().authorizeRequests().anyRequest().permitAll().and().build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
    public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, PaymentMethodRepository paymentMethodRepository) {
        return args -> {
            // initial roles
            if (roleRepository.count() < 2) {
                roleRepository.saveAll(Arrays.asList(Role.builder()
                                .roleName("ROLE_USER")
                                .build(),
                        Role.builder()
                                .roleName("ROLE_ADMIN")
                                .build(),
                        Role.builder()
                                .roleName("ROLE_NV")
                                .build()
                ));
            }
            // initial default user "admin"
            if (userRepository.getByUser("admin").isEmpty())
                userRepository.save(User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("123456"))
//                        .avatar("/assets/images/users/avatar-2.jpg")
                                .avatar("https://res.cloudinary.com/dg8hhxkah/image/upload/v1703776131/other/bixhah1m4u2nu0zqsq7m.jpg")
                                .name("FourStore Shop")
                                .roles(Collections.singletonList(roleRepository
                                        .getByName(RoleUtil.ADMIN.getValue())))
                                .build()
                );
            if (paymentMethodRepository.findAll().stream().count() <= 0){
                List<PaymentMethod> paymentMethods = new ArrayList<>();
                paymentMethods.add(PaymentMethod.builder()
                        .id(1)
                        .methodName("Thanh toán khi nhận hàng (COD)")
                        .createDate(Date.valueOf(LocalDate.now()))
                        .description("Khi nhận hàng thì thanh toán ")
                        .status("1")
                        .build());
                paymentMethods.add(PaymentMethod.builder()
                        .id(2)
                        .methodName("Thanh toán bằng VN Pay")
                        .createDate(Date.valueOf(LocalDate.now()))
                        .description("Thanh toán bằng tài khoản ngân hàng ")
                        .status("1")
                        .build());
                paymentMethodRepository.saveAll(paymentMethods);
            }
        };
    }
}
