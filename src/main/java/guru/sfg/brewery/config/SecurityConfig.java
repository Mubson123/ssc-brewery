package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
    RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher(
        "/api/**"));
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }

  public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
    RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    final String USER = "USER";
    final String ADMIN = "ADMIN";
    final String CUSTOMER = "CUSTOMER";
    http
        .addFilterBefore(restHeaderAuthFilter(authenticationManager()),
            UsernamePasswordAuthenticationFilter.class)
        .csrf().disable();

    http.addFilterBefore(restUrlAuthFilter(authenticationManager()),
        UsernamePasswordAuthenticationFilter.class);

    http
        .authorizeRequests(authorize ->
            authorize
                .antMatchers("/h2-console/**")
                .permitAll() // do not use in production!

                .antMatchers("/", "/webjars/**", "/login", "/resources/**")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/api/v1/beer/**")
                .hasAnyRole(ADMIN, CUSTOMER, USER)

                .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}")
                .hasAnyRole(ADMIN, CUSTOMER, USER)

                .mvcMatchers("/brewery/breweries")
                .hasAnyRole(ADMIN, CUSTOMER)

                .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries")
                .hasAnyRole(ADMIN, CUSTOMER)

                .mvcMatchers("/beers/find", "beers/{beerId}")
                .hasAnyRole(ADMIN, CUSTOMER, USER)

        )
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .and()
        .httpBasic();

    // h2 console config to accepts headers
    http.headers().frameOptions().sameOrigin();
  }
}