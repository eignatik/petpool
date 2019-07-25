package com.petpool.config;

import com.petpool.config.security.AuthenticationTokenFilter;
import com.petpool.config.security.AuthenticationTokenProvider;
import com.petpool.config.security.CustomAccessDeniedHandler;
import com.petpool.config.security.CustomAuthenticationProvider;
import com.petpool.config.security.CustomSavedRequestAwareAuthenticationSuccessHandler;
import com.petpool.config.security.RestAuthenticationEntryPoint;
import com.petpool.domain.model.user.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@PropertySource({"environment.properties"})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${encryption.key}")
  private String encKey;

  @Autowired
  private CustomAccessDeniedHandler accessDeniedHandler;

  @Autowired
  private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


  private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

  @Autowired
  private AuthenticationTokenProvider authProvider;

  @Override
  public void configure(final WebSecurity webSecurity) {
    webSecurity.ignoring().antMatchers("/token/**");
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authProvider);

  }

  private static final RequestMatcher PUBLIC_API_URL =  new AntPathRequestMatcher("/api/public/**");
  private static final RequestMatcher PRIVATE_API_URL =  new AntPathRequestMatcher("/api/private/**");
  private static final RequestMatcher ADMIN_API_URL =  new AntPathRequestMatcher("/api/administrative/**");

  private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
      PRIVATE_API_URL,
      ADMIN_API_URL
  );

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler)
        .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
        .authenticationProvider(authProvider)
        .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
        .authorizeRequests()//.anyRequest().authenticated()
        .requestMatchers(PUBLIC_API_URL).permitAll()
        .requestMatchers(PRIVATE_API_URL).authenticated()
        .requestMatchers(ADMIN_API_URL).hasRole(UserType.ADMIN.getValue())
        .and()
        .formLogin().disable()
        .httpBasic().disable()
        .logout().disable();
  }


  private AuthenticationTokenFilter authenticationFilter() throws Exception {
    final AuthenticationTokenFilter filter = new AuthenticationTokenFilter(PROTECTED_URLS);
    filter.setAuthenticationManager(authenticationManager());
    //filter.setAuthenticationSuccessHandler(successHandler());
    return filter;
  }

  @Bean
  AuthenticationEntryPoint forbiddenEntryPoint() {
    return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
  }



}
