package com.betting.ground.config;

import com.betting.ground.config.filter.JwtTokenFilter;
import com.betting.ground.config.jwt.JwtUtils;
import com.betting.ground.user.domain.Role;
import com.betting.ground.user.dto.login.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailServiceImpl userDetailService;
    private final JwtUtils jwtUtils;
    private final CustomAuthenticationEntryPoint entryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenFilter authenticationJwtTokenFilter() {
        return new JwtTokenFilter(jwtUtils);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .addFilter(corsFilter())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/index.html").permitAll()
                        .requestMatchers("/api/user/code").permitAll()
                        .requestMatchers("/api/user/login/kakao").permitAll()
                        .requestMatchers("/api/user/reissue").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/auction/{auctionId}").permitAll()
                        .requestMatchers("/api/auction/{auctionId}/seller").permitAll()
                        .requestMatchers("/api/auction/{auctionId}/bidHistory").permitAll()
                        .requestMatchers("/api/auction/view").permitAll()
                        .requestMatchers("/api/auction/search").permitAll()
                        .requestMatchers("/api/auction/new").permitAll()
                        .requestMatchers("/api/auction/deadline").permitAll()
                        .requestMatchers("/api/schedule/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()

                        .requestMatchers("/api/deal/{dealId}").hasRole("USER")
                        .requestMatchers("/api/auction").hasRole("USER")
                        .requestMatchers("/api/auction/{auctionId}/instant").hasRole("USER")
                        .requestMatchers("/api/auction/{auctionId}/bid").hasRole("USER")
                        .requestMatchers("/api/auction/{auctionId}").hasRole("USER")
                        .requestMatchers("/api/delivery").hasRole("USER")
                        .requestMatchers("/api/payment/ready").hasRole("USER")
                        .requestMatchers("/api/payment/succes").hasRole("USER")

                        .anyRequest().authenticated()
                ).build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addExposedHeader("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
