package com.erivan.produto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
public class WebSecutiry extends WebSecurityConfigurerAdapter{
	

	@Autowired
	UsuarioDetails usuarioDetails;
	
	 public WebSecutiry(UsuarioDetails usuarioDetails) {
		this.usuarioDetails = usuarioDetails;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		
		.antMatchers(HttpMethod.GET, "/").permitAll()   //todos acessam o login
		.anyRequest().authenticated()
		.and().formLogin()
		.loginPage("/login").permitAll()   //pagina de login customizada
        .loginProcessingUrl("/appLogin")   //requisição da url de login.html
        .usernameParameter("app_nomeusuario")   //usuario e senha vindo do name do login.html
        .passwordParameter("app_senha")
        .defaultSuccessUrl("/produtos")        //passou vai para a /
        .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}

	//autenticação usuário salvo no banco de dados
			@Override
			protected void configure(AuthenticationManagerBuilder auth) throws Exception{
				auth.userDetailsService(usuarioDetails)
				.passwordEncoder(new BCryptPasswordEncoder()); //gera uma senha criptografada, nao vou precisar
			}
			
			//a autenticação ignora esses arquivos
			public void configure(WebSecurity web) throws Exception{
				web.ignoring().antMatchers("/css/**", "/js/**");
			}
	
	
	

}
