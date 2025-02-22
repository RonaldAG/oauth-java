package br.com.ronald.oauth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
public class SecurityFilterConfig {

    @Bean
    @Order(1)
    SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // responsavel por iniciar as configurações basicas do oauth
        http.with(OAuth2AuthorizationServerConfigurer.authorizationServer(), Customizer.withDefaults());

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()); // Habilitando o OpenID Connect
        http.exceptionHandling((exception) -> exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))) // Redireciona para a pagina de login ao dar exception
                .oauth2ResourceServer(Customizer.withDefaults()); //define que tipo de token o servidor vai aceitar. Nesse cado esta usando o padrao, JWT

        return http.build();
    }

    // Garante que todas as requisicoes para o authorization server exija estar autenticado
    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults()); // caso nao esteja autenticado, redireciona para o formulario de login
        return http.build();
    }

}
