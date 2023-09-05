package org.example.config;
// Source
// securityFilterChain(HttpSecurity http) taken from https://www.youtube.com/watch?v=d7ZmZFbE_qY +
// https://habr.com/ru/articles/482552/
// jdbc config https://www.youtube.com/watch?v=HvovW6Uh1yU&t=276s

// Main video https://www.youtube.com/watch?v=HvovW6Uh1yU&t=276s


import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        //Доступ только для не зарегистрированных пользователе
                        //                       .requestMatchers("/registration").anonymous()
                        //Доступ только для пользователей с ролью Администратор
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //Доступ только для пользователей с ролью Юзер
                        .requestMatchers("/user/**").hasRole("USER")
                        //For authenticated users
                        .requestMatchers("/authenticated").authenticated()
                        //Доступ разрешен всем пользователей
                        .requestMatchers("/").permitAll()
                        //Все остальные страницы требуют аутентификации
                        .anyRequest().authenticated()
                )
                //Настройка для входа в систему
                .formLogin((form) -> form
                                .defaultSuccessUrl("/")
                                .permitAll()
                )
                .logout((logout) -> logout.permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }

}

