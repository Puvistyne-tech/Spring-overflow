package uge.fr.ugeoverflow.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import uge.fr.ugeoverflow.service.TokenExpiredService;
import uge.fr.ugeoverflow.service.UserService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static uge.fr.ugeoverflow.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtDecoder jwtDecoder;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenExpiredService tokenExpiredService;


    public SecurityConfiguration(JwtDecoder jwtDecoder, UserService userService, BCryptPasswordEncoder passwordEncoder, TokenExpiredService tokenExpiredService) {
        this.jwtDecoder = jwtDecoder;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenExpiredService = tokenExpiredService;
    }

    // custom securityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/index", "/css/*", "/js/*", "/**.css","/images/*").permitAll()
                        .requestMatchers("/register", "/auth/api/v1/register", "/auth/api/v1/signin","/api/v1/register").permitAll()
                        .requestMatchers("/admin/**").hasRole(ADMIN.name())
                        .requestMatchers("/auth/**").hasAnyRole(USER_AUTH.name(), ADMIN.name())//TODO
                        .anyRequest().permitAll())
                .anonymous(anonymous -> anonymous
                        .key(USER_ANONYMOUS.name())
                        .principal(USER_ANONYMOUS.name())
                        .authorities(USER_ANONYMOUS.getAuthoritiesList()
                        )
                )
                .formLogin()
                .loginProcessingUrl("/signin")
                .loginPage("/signin").permitAll()
                .defaultSuccessUrl("/", false)
                .and()
                .rememberMe(rememberMe -> rememberMe.tokenValiditySeconds((int) TimeUnit.DAYS.toDays(1)).key("notyetthere")//TODO change this, get from properties
                        .rememberMeParameter("remember-me")
                        .userDetailsService(userService))
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .clearAuthentication(true))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .oauth2ResourceServer(oauth -> oauth.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()))
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(jwtAuthenticationProvider())
                .addFilterAfter(myCustomFilter(), BasicAuthenticationFilter.class)

        ;
        return http.build();
    }

    private OncePerRequestFilter myCustomFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    if (tokenExpiredService.isTokenExpired(token)) {
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                        Authentication auth= new UsernamePasswordAuthenticationToken()
                        SecurityContextHolder.getContext().setAuthentication(null);
                        System.out.println("authentication = " + authentication);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }


    //TODO
    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider() {
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
//        jwtAuthenticationProvider.setJwtAuthenticationConverter(new JwtAuthenticationConverter());
        jwtAuthenticationProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        return jwtAuthenticationProvider;
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    /**
     * This is used to add the role_ prefix to the roles in the jwt token
     * Because DAOAuthenticationProvider is uses ROLE_ prefix but JwtAuthenticationProvider uses SCOPE_ prefix by default
     * to make the routes work with both authentication providers
     *
     * @return JwtAuthenticationConverter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }



}

/**
 * resource server configuration, JWT token Decoder and Encoder are inspired from Spring Documentation
 * https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html#oauth2resourceserver-jwt-authorization-extraction
 */
