//package org.kumoricon.staffserver;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//@Configuration
//@EnableWebSecurity
//@Order(2)
//public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final AuthenticationEntryPoint authEntryPoint;
//
//    @Autowired
//    public SpringSecurityConfig(AuthenticationEntryPoint authEntryPoint) {
//        this.authEntryPoint = authEntryPoint;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.csrf().disable().authorizeRequests()
//                .antMatchers("/healthcheck").permitAll()
//                .antMatchers("/users/**").hasRole("ADMIN")
//                .antMatchers("/staff/buildbadges").hasRole("ADMIN")
//                .anyRequest().authenticated()
//                .and().httpBasic()
//                .authenticationEntryPoint(authEntryPoint);
//    }
//}
//
