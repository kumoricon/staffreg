package org.kumoricon.staffserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Order(1)
public class SpringSecurityUIConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPoint authEntryPoint;
    private final DataSource dataSource;

    @Autowired
    public SpringSecurityUIConfig(AuthenticationEntryPoint authEntryPoint, DataSource dataSource) {
        this.authEntryPoint = authEntryPoint;
        this.dataSource = dataSource;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // No authorization required for static resources, images, etc
                .antMatchers("/css/**", "/js/**", "/img/**", "/healthcheck", "/favicon.ico").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/login?logout")
                .and()
                .formLogin()
                .loginPage("/login").permitAll();

        http.headers().contentSecurityPolicy("script-src 'self'");

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
        .authoritiesByUsernameQuery("select users.username as username, roles.name as authority " +
                "                      from users " +
                "                        join users_roles on users_roles.user_id = users.id " +
                "                        join roles on users_roles.role_id = roles.id " +
                "                      where users_roles.user_id = users.id AND " +
                "                        users_roles.role_id = roles.id AND username=?")
        .usersByUsernameQuery("select username, password, enabled from users where username=? AND enabled=true");
    }

}

