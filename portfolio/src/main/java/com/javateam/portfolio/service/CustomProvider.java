package com.javateam.portfolio.service;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.javateam.portfolio.domain.CustomUser;
import com.javateam.portfolio.domain.Role;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomProvider implements AuthenticationProvider, UserDetailsService {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.info("loadUserByUsername");
		
		try {
			return (CustomUser)jdbcTemplate.queryForObject(
					"SELECT id as username, password, enabled from member_tbl WHERE id=?",
					new BeanPropertyRowMapper<CustomUser>(CustomUser.class),
					new Object[] {username});
		} catch (EmptyResultDataAccessException e) {
			log.info("error1");
			return null;
		}
		
	}
	
	private List<Role> loadUserRole(String username){
		
		log.info("loadUserRole");
		
		try {
			return(List<Role>)jdbcTemplate.query(
					"SELECT username, role FROM user_roles WHERE username=?",
					new BeanPropertyRowMapper<Role>(Role.class),
					new Object[] {username});
		} catch (EmptyResultDataAccessException e) {
			log.info("error2");
			return null;
		}
		
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		log.info("---- authenticate");

		String username = authentication.getName();
		String password = "";
		
		CustomUser user = null;
		Collection<? extends GrantedAuthority> authorities = null;
		
		try {
			if(username.trim().contentEquals("")) {
				throw new InternalAuthenticationServiceException("회원 아이디를 입력하십시오.");
				
			}
			
			if(this.loadUserByUsername(username)==null) {
				throw new UsernameNotFoundException("회원 아이디가 없습니다.");
			}
			
			user = this.loadUserByUsername(username);
			
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			password = (String) authentication.getCredentials();
			
			if(passwordEncoder.matches(password, user.getPassword())) {
				log.info("비밀번호 일치!");
			} else {
				throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
			}
			
			log.info("role 점검");
			
			List<Role> roles = this.loadUserRole(username);
			
			roles.forEach(x -> {
				log.info("role : " + x);
			});
			
			user.setAuthorities(roles);
			
			authorities = user.getAuthorities();
			
		} catch (InternalAuthenticationServiceException e) {
			log.info("회원 아이디 미입력 : " + e.toString());
			throw new InternalAuthenticationServiceException(e.toString());
		} catch (UsernameNotFoundException e) {
			log.info("회원 아이디가 없음 : ", e.toString());
			throw new InternalAuthenticationServiceException(e.getMessage());
		} catch (BadCredentialsException e) {
			log.info("패쓰워드가 잘못됨 : ", e.toString());
			throw new BadCredentialsException(e.getMessage());
		} catch (Exception e) {
			log.info("다른 종류의 에러 : ", e.toString());
			e.printStackTrace();
		}
		
		return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
