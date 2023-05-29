package com.example.scraping.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.io.IOException;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests -> {
                    authorizeRequests.requestMatchers("/usr/page").authenticated();
                    authorizeRequests.requestMatchers("/usr/join").permitAll();
                    authorizeRequests.anyRequest().permitAll();
                }))
                .formLogin((formLogin) -> {
                    /* 권한이 필요한 요청은 해당 url로 리다이렉트 */
                    formLogin
                            .loginPage("/usr/login")
                            .loginProcessingUrl("/usr/loginProc")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .successHandler(
                                    new AuthenticationSuccessHandler() {
                                        @Override
                                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                            System.out.println("authentication: " + authentication.getName());
                                            response.sendRedirect("/");
                                        }
                                    }
                            )
                            .failureHandler(
                                    new AuthenticationFailureHandler() {
                                        @Override
                                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                            System.out.println("exception: " + exception.getMessage());
                                            response.sendRedirect("/usr/login");
                                        }
                                    }
                            )

                            .permitAll();

                })
                .logout((logout) -> {
                    logout.logoutUrl("/usr/logout")
                            .logoutSuccessUrl("/")
                            .permitAll();

                        })


                .build();

    }


}
