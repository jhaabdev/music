package com.openwt.music.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtUserDetails implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final String artistName;
    private final List<GrantedAuthority> grantedAuthorities;

    public JwtUserDetails(UserEntity user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.artistName = user.getArtistName();
        this.grantedAuthorities = new ArrayList<>(1);
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getArtistName() {
        return artistName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
