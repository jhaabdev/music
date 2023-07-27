package com.openwt.music.service;

import com.openwt.music.exception.UserNotFoundException;
import com.openwt.music.model.JwtUserDetails;
import com.openwt.music.model.UserEntity;
import com.openwt.music.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username)
        );
        return new JwtUserDetails(user);
    }
}
