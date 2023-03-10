package com.example.financialfinalproject.global.config;

import com.example.financialfinalproject.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.financialfinalproject.global.jwt.service.JwtService;
import com.example.financialfinalproject.global.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.example.financialfinalproject.global.login.handler.LoginFailureHandler;
import com.example.financialfinalproject.global.login.handler.LoginSuccessHandler;
import com.example.financialfinalproject.global.login.service.LoginService;
import com.example.financialfinalproject.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.example.financialfinalproject.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.example.financialfinalproject.global.oauth2.service.CustomOAuth2UserService;
import com.example.financialfinalproject.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * ????????? CustomJsonUsernamePasswordAuthenticationFilter?????? authenticate()??? ????????? ???????????? ??????
 * JwtAuthenticationProcessingFilter??? AccessToken, RefreshToken ?????????
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    private final String[] SWAGGER = {
            "/v3/api-docs",
            "/swagger-resources/**", "/configuration/security", "/webjars/**",
            "/swagger-ui.html", "/swagger/**", "/swagger-ui/**"};


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .formLogin().disable()
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .mvcMatchers("/**").permitAll()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/users/join", "/api/v1/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
                .antMatchers("/api/v1/users/{userId}/role").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .anyRequest().permitAll() // ?????? ?????? ???????????? ?????? ?????? ??????
                .and()
                //== ?????? ????????? ?????? ==//
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler) // ???????????? ??????????????? ????????? ??? Handler ??????
                .failureHandler(oAuth2LoginFailureHandler) // ?????? ????????? ?????? ??? ????????? ??????
                .userInfoEndpoint().userService(customOAuth2UserService); // customUserService ??????

        // ?????? ????????? ???????????? ?????? ????????? LogoutFilter ????????? ????????? ?????? ??????
        // ?????????, LogoutFilter ????????? ????????? ?????? ?????? ??????????????? ??????
        // ?????? : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * AuthenticationManager ?????? ??? ??????
     * PasswordEncoder??? ???????????? AuthenticationProvider ?????? (PasswordEncoder??? ????????? ????????? PasswordEncoder ??????)
     * FormLogin(?????? ????????? ???????????? ?????????)??? ???????????? DaoAuthenticationProvider ??????
     * UserDetailsService??? ????????? LoginService??? ??????
     * ??????, FormLogin??? ???????????? AuthenticationManager?????? ???????????? ProviderManager ??????(return ProviderManager)
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * ????????? ?????? ??? ???????????? LoginSuccessJWTProviderHandler ??? ??????
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository, redisTemplate);
    }

    /**
     * ????????? ?????? ??? ???????????? LoginFailureHandler ??? ??????
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter ??? ??????
     * ????????? ????????? ???????????? ?????? ?????? ????????? ????????? Bean?????? ??????
     * setAuthenticationManager(authenticationManager())??? ????????? ????????? AuthenticationManager(ProviderManager) ??????
     * ????????? ?????? ??? ????????? handler, ?????? ??? ????????? handler??? ????????? ????????? handler ??????
     */
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository,redisTemplate);
        return jwtAuthenticationFilter;
    }
}
