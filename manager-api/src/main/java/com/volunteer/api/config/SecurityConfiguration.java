package com.volunteer.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volunteer.api.data.model.api.ErrorResponse;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.security.filter.JWTAuthorizationFilter;
import com.volunteer.api.service.JWTService;
import com.volunteer.api.service.UserService;
import com.volunteer.api.service.impl.JWTServiceImpl;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.io.IOException;
import java.time.Duration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${security.jwt.secret}")
  private String secret;

  @Value("${cors.enable:false}")
  private boolean enableCors;

  @Value("${swagger.enable:false}")
  private boolean enableSwagger;

  @Value("${security.jwt.access-token-ttl:1m}")
  private Duration accessTokenTtl;
  @Value("${security.jwt.refresh-token-ttl:5m}")
  private Duration refreshTokenTtl;

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserService userService;
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver exceptionResolver;

  @Override
  protected void configure(final AuthenticationManagerBuilder builder) throws Exception {
    builder.userDetailsService(userService).passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlAuthConf =
        http.authorizeRequests()
            .antMatchers("/actuator/**").permitAll()
            .antMatchers("/api/v1/authenticate").permitAll()
            .antMatchers("/api/v1/config").permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/users/password/reset").permitAll();
    if (enableCors) {
      urlAuthConf = urlAuthConf.antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
    }
    configureOpenApi(urlAuthConf).anyRequest().authenticated();

    http.addFilterBefore(new JWTAuthorizationFilter(jwtService(), exceptionResolver, enableCors),
        UsernamePasswordAuthenticationFilter.class);
  }
  
  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new AccessDeniedHandler() {
      @Override
      public void handle(HttpServletRequest request, HttpServletResponse response,
          AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String reason = HttpStatus.FORBIDDEN.getReasonPhrase();
        String code = "NO_PERMISSION";
        try {
          VPUser user = userService.getCurrentUser();
          if (user.isLocked()) {
            reason = "User locked";
            code = "LOCKED";
          }
          if (userService.hasDisasterRating(user)) {
            reason = "User rating too low";
            code = "DISASTER_RATING";
          }
          if (!user.isPhoneNumberVerified()) {
            reason = "User phone number not verified";
            code = "UNVERIFIED_PHONE";
          }
          if (!user.isUserVerified()) {
            reason = "User not verified";
            code = "UNVERIFIED";
          }
        } catch (Exception e) {
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(),
            ErrorResponse.builder().errorMessage(reason).errorCode(code).build());
      }
    };
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public JWTService jwtService() {
    return new JWTServiceImpl(secret, accessTokenTtl, refreshTokenTtl);
  }

  @Bean
  public OpenAPI customOpenAPI() {
    final String securitySchemeName = "bearerAuth";
    return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(new Components().addSecuritySchemes(securitySchemeName,
            new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP)
                .scheme("bearer").bearerFormat("JWT")));
  }

  private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry configureOpenApi(
      ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry) {
    if (enableSwagger) {
      return urlRegistry.antMatchers("/swagger-ui/**").permitAll()
          .antMatchers("/swagger-ui.html").permitAll()
          .antMatchers("/v3/**").permitAll();
    }
    return urlRegistry;
  }
}
