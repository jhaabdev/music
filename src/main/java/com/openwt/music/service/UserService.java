package com.openwt.music.service;

import com.openwt.music.dto.ArtistCreateDto;
import com.openwt.music.dto.AuthenticationDto;
import com.openwt.music.enums.UserRole;
import com.openwt.music.exception.UserNotFoundException;
import com.openwt.music.model.JwtUserDetails;
import com.openwt.music.model.UserEntity;
import com.openwt.music.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public JwtUserDetails findById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
        return jwtUserDetailsService.loadUserByUsername(user.getUsername());
    }

    public JwtUserDetails findById(Long id, String role) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id, role));
        JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.getUsername());

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role))) {
            return userDetails;
        } else {
            throw new UserNotFoundException(id, role);
        }
    }

    public JwtUserDetails findByUsername(String username, String role) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username, role));
        JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.getUsername());

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role))) {
            return userDetails;
        } else {
            throw new UserNotFoundException(username, role);
        }
    }

    public List<UserEntity> findAllByRole(String role) {
        List<UserEntity> userEntityList = userRepository.findAll();
        return userEntityList.stream()
                .filter(user -> user.getRole().contains(role))
                .toList();
    }

    @Transactional
    public void deleteArtistById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        JwtUserDetails jwtUser = new JwtUserDetails(user);
        if (jwtUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + UserRole.ARTIST.label))) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id, UserRole.ARTIST.label);
        }
    }

    @Transactional
    public void deleteAdminById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        JwtUserDetails jwtUser = new JwtUserDetails(user);
        if (jwtUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + UserRole.ADMIN.label))) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id, UserRole.ADMIN.label);
        }
    }

    public JwtUserDetails registerArtist(ArtistCreateDto dto) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setArtistName(dto.getArtistName());
        newUser.setRole(UserRole.ARTIST.label);

        UserEntity user = userRepository.save(newUser);

        return new JwtUserDetails(user);
    }

    public AuthenticationDto login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, password));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        final AuthenticationDto authenticationResponse = new AuthenticationDto();
        authenticationResponse.setAccessToken(jwtService.generateToken(userDetails));
        return authenticationResponse;
    }

    public String logout(String bearerToken) {
        // revoke token
        // either
        // store blacklisted token in Redis
        // or
        // save token in cookies when signing in
        // and clear cookies when signing out
        return "trying to leave i see";
    }
}
