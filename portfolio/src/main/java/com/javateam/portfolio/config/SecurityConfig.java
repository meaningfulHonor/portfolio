package com.javateam.portfolio.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.javateam.portfolio.service.CustomProvider;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
@Slf4j
public class SecurityConfig {

	@Autowired
	private CustomProvider customProvider;
	
	private UserDetailsService userDetailsService;
	
	private DataSource dataSource;
	
	public SecurityConfig(UserDetailsService userDetailsService, DataSource dataSource) {
		log.info("생성자 주입");
		this.dataSource = dataSource;
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/css/**", "/webjars/**", 
				"/images/**", "/js/**", "/v2/api-docs", "/swagger-resources/**", "/swagger/**", "/swagger-ui.html",
				"/axios/**", "/bootstrap-icons/**", "/bootstrap/**", "/summernote/**", "/jquery/**");	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
			.userDetailsService(userDetailsService)
			.authenticationProvider(customProvider)
			.headers().frameOptions().disable()
			.and()
				.authorizeRequests()
				.antMatchers("/", "/index", "/css/**", "/webjars/**","/images/**", "/js/**", "/thumbnails/**").permitAll()
				.antMatchers("/product/soccer", "/product/itemPage.do/**", "/product/replica", "/product/itemPage2.do/**",
						"/product/searchProduct.do", "/product/searchReplica.do").permitAll()
				.antMatchers("/swagger-resources/**", "/swagger/**", "/swagger-ui.html").permitAll()
				.antMatchers("/member/hasFld/**", "/member/hasFldForUpdate/**").permitAll()
				.antMatchers("/member/view.do").authenticated()
				.antMatchers("/member/update.do", "/member/updateProc.do").authenticated()
				.antMatchers("/member/updateSess.do", "/member/updateSessProc.do").authenticated()
				.antMatchers("/member/join.do", "/member/joinProc.do", "/member/joinProcRest.do").permitAll()
				.antMatchers("/member/updateRoles/**", "/member/changeMemberState/**", 
           			 "/member/updateMemberByAdmin/**", "/member/deleteMemberByAdmin/**").authenticated()
				.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/board/write.do","/board/writeProc.do").authenticated()
				.antMatchers("/board/image", "/board/image/**", "/board/list.do", 
           			 "/board/view.do", "/board/searchList.do",
           			 "/board/update.do", "/board/updateProc.do",
           			 "/board/replyWrite.do", "/board/replyUpdate.do", 
           			 "/board/getRepliesAll.do", "/board/replyDelete.do",
           			 "/board/deleteProc.do").authenticated()
				.antMatchers("/notice/list.do", "/notice/view.do/**", "/notice/image", "/notice/image/**",
					 "/notice/searchList.do").permitAll()
				.antMatchers("/notice/write.do", "/notice/writeProc.do", "/notice/update.do", "/notice/updateProc.do",
           			 "/notice/deleteProc.do").hasAuthority("ROLE_ADMIN")
				.antMatchers("/order/**").authenticated()
				.anyRequest().authenticated()
			.and()
				.formLogin()
					.loginPage("/login")
					.usernameParameter("username")
					.passwordParameter("password")
					.defaultSuccessUrl("/index")
					.failureUrl("/loginError")
					.permitAll()
			.and()
				.logout().permitAll()
			.and()
				.exceptionHandling().accessDeniedPage("/403");
			
		return http.build();
	}
	
	// remember-me 관련 메서드
	private PersistentTokenRepository getJDBCRepository() {
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		
		return repo;
	}
}
