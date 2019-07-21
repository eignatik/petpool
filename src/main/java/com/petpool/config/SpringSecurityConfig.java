package com.petpool.config;

import com.petpool.config.security.CustomAccessDeniedHandler;
import com.petpool.config.security.CustomAuthenticationProvider;
import com.petpool.config.security.CustomSavedRequestAwareAuthenticationSuccessHandler;
import com.petpool.config.security.RestAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private CustomSavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;

    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Autowired
    private CustomAuthenticationProvider authProvider;


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);

    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .antMatchers("/").permitAll()
                .antMatchers("/administration/**").hasAnyRole("ADMIN")
                .and()
                .formLogin()
                    .successHandler(mySuccessHandler)
                    .failureHandler(myFailureHandler)
                .and()
                .httpBasic()
                .and()
                .logout()
                    .deleteCookies("remove")
                    .invalidateHttpSession(false)
                    .logoutUrl("/logout");
                    //.logoutSuccessUrl("/logout-success")
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(4);
    }


}
