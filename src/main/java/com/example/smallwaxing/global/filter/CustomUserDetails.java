package com.example.smallwaxing.global.filter;


import com.example.smallwaxing.user.domain.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

	private String username;
	private Role role;
	private Collection<? extends GrantedAuthority> authorities;

	@Builder
	private CustomUserDetails(String username, Role role, Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.role = role;
		this.authorities = authorities;
	}

	public static CustomUserDetails of(String username, Role role, Collection<? extends GrantedAuthority> authorities) {
		return CustomUserDetails.builder()
			.username(username)
			.role(role)
			.authorities(authorities)
			.build();
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	private static List<? extends GrantedAuthority> convertAuthorities(String... roles) {
		return Arrays.stream(roles)
			.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
			.toList();
	}

}
