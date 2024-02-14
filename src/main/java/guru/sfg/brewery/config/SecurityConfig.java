package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
    RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher(
        "/api/**"));
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .addFilterBefore(restHeaderAuthFilter(authenticationManager()),
            UsernamePasswordAuthenticationFilter.class)
        .csrf().disable();
    http
        .authorizeRequests(authorize ->
            authorize
                .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                .antMatchers("/beer/find", "/beers*", "/beers/find").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll()
        )
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .and()
        .httpBasic();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .inMemoryAuthentication()
        .withUser("spring")
        .password("{bcrypt}$2a$10$DQ84ZEd204ZKyiwJQMU3POi4kxR6MGCnEs6xdLML2mb3bc2rGLY/e")
        .roles("ADMIN")
        .and()
        .withUser("user")
        .password(
            "{sha256}fa8ae9d007c1db40dd4be3ffccd2e914f9afb5a21617a6037f5d4a6" +
                "7e07b3912b6812b3c97960f8d")
        .roles("USER");
    auth
        .inMemoryAuthentication()
        .withUser("scott")
        .password("{bcrypt10}$2a$10$leLFubqZRfEPlNSdRLH5X" +
            ".SlgpH3fwgsiqFfAhdMeb2xRcZTFrR3y")
        .roles("CUSTOMER");
  }
}