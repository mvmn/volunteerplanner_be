package com.volunteer.api.config;

import com.volunteer.api.security.filter.JWTAuthorizationFilter;
import com.volunteer.api.service.JWTService;
import com.volunteer.api.service.impl.JWTServiceImpl;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
  private UserDetailsService userService;

  @Override
  protected void configure(final AuthenticationManagerBuilder builder) throws Exception {
    builder.userDetailsService(userService).passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlAuthConf =
        http.authorizeRequests()
            .antMatchers("/actuator/**").permitAll()
            .antMatchers("/api/v1/authenticate").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/address/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/users/password/reset").permitAll();
    if(enableCors) {
      urlAuthConf = urlAuthConf.antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
    }
    configureOpenApi(urlAuthConf).anyRequest().authenticated();

    http.addFilterBefore(new JWTAuthorizationFilter(jwtService()),
        UsernamePasswordAuthenticationFilter.class);
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
