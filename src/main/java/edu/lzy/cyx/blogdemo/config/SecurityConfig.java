package edu.lzy.cyx.blogdemo.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/page/**", "/article/**", "/comments/**", "/login").permitAll()
                        .requestMatchers("/back/**", "/assets/**", "/user/**", "/article_img/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                String url = request.getParameter("url");
                                // 获取被拦截登录的原始路径
                                RequestCache requestCache = new HttpSessionRequestCache();
                                SavedRequest savedRequest = requestCache.getRequest(request, response);
                                if (savedRequest != null) {
                                    // 如果存在原始拦截路径，登录成功后重定向到原始访问路径
                                    response.sendRedirect(savedRequest.getRedirectUrl());
                                } else if (url != null && !url.equals("")) {
                                    URL fullURL = new URL(url);
                                    response.sendRedirect(fullURL.getPath());
                                } else {
                                    // 针对直接登陆到用户，根据用户角色分别重定向到后台首页和前台首页
                                    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                                    boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_admin"));
                                        if (isAdmin) {
                                        response.sendRedirect("/admin");
                                        } else {
                                        response.sendRedirect("/");
                                        }
                                }
                            }
                        })
                        .failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                // 登陆失败后，取出原始数据url并追加在重定向路径上
                                String url = request.getParameter("url");
                                response.sendRedirect("/login?error&url="+url);
                            }
                        })
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe
                        .alwaysRemember(true)
                        .tokenValiditySeconds(5000))
                .exceptionHandling(exceptionHandle -> exceptionHandle
                        .accessDeniedHandler(new AccessDeniedHandler() {
                            @Override
                            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                                RequestDispatcher dispatcher = request.getRequestDispatcher("/errorPage/comm/error_403");
                                dispatcher.forward(request, response);
                            }
                        }))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 基于JDBC的身份验证 - 修复后的SQL
        String userSQL = "select username, password, valid from t_user where username = ?";

        // 修复表关联查询语法（假设表结构正确）
        String authoritySQL = "select c.username, a.authority from " +
                "t_user c, " +
                "t_authority a, " +
                "t_user_authority ca " +
                "where ca.user_id = c.id and ca.authority_id = a.id and c.username=?";

        JdbcUserDetailsManager users = new JdbcUserDetailsManager();
        users.setDataSource(dataSource);
        users.setUsersByUsernameQuery(userSQL);
        users.setAuthoritiesByUsernameQuery(authoritySQL);
        return users;
    }
}
