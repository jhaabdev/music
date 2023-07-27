package com.openwt.music.controller;

import com.openwt.music.dto.AdminDetailsDto;
import com.openwt.music.dto.ArtistCreateDto;
import com.openwt.music.dto.ArtistDetailsDto;
import com.openwt.music.dto.AuthenticationDto;
import com.openwt.music.enums.UserRole;
import com.openwt.music.model.JwtUserDetails;
import com.openwt.music.model.UserEntity;
import com.openwt.music.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // GET ------------------------------------------------------------------------------
    @GetMapping("/admins")
    public List<AdminDetailsDto> findAllAdmins () {
        List<UserEntity> users = userService.findAllByRole(UserRole.ADMIN.label);

        List<AdminDetailsDto> usersDetails = new ArrayList<>();
        users.forEach(user ->
                usersDetails.add(
                        new AdminDetailsDto(user.getId(), user.getUsername())
        ));
        return usersDetails;
    }

    @GetMapping("/admins/{id}")
    public AdminDetailsDto findAdminById (@PathVariable Long id) {
        JwtUserDetails user = userService.findById(id, UserRole.ADMIN.label);
        return new AdminDetailsDto(user.getId(), user.getUsername());
    }

    @GetMapping("/admins/{username}")
    public AdminDetailsDto findAdminByUsername (@PathVariable String username) {
        JwtUserDetails user = userService.findByUsername(username, UserRole.ADMIN.label);
        return new AdminDetailsDto(user.getId(), user.getUsername());
    }

    // TODO: add artist research filter: artistname, username, songs, artist creation date, etc...
    @GetMapping("/artists")
    public List<ArtistDetailsDto> findAllArtists () {
        List<UserEntity> users = userService.findAllByRole(UserRole.ARTIST.label);

        List<ArtistDetailsDto> usersDetails = new ArrayList<>();

        users.forEach(user ->
                usersDetails.add(
                        new ArtistDetailsDto(
                                user.getId(),
                                user.getUsername(),
                                user.getArtistName()
        )));
        return usersDetails;
    }

    // DELETE ----------------------------------------------------------------------------
    @DeleteMapping("/artists/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteArtistById (@PathVariable Long id) {
        // only an admin can delete an artist
        // remember to revoke token
        userService.deleteArtistById(id);
    }

    @DeleteMapping("/admins/{id}")
    public void deleteAdminstById (@PathVariable Long id) {
        // only an admin can delete an admin
        // remember to revoke token
        userService.deleteAdminById(id);
    }

    // POST ----------------------------------------------------------------------------
    @PostMapping("/sign-up")
    public ArtistDetailsDto registerArtist(@RequestBody ArtistCreateDto request) {
        JwtUserDetails newArtist = userService.registerArtist(request);

        return new ArtistDetailsDto(
                newArtist.getId(),
                newArtist.getUsername(),
                newArtist.getArtistName()
        );
    }

    @PostMapping("/login")
    public AuthenticationDto login(@RequestHeader("Authorization") String authorizationHeader) {
        String credentials = authorizationHeader.substring("Basic".length()).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(credentials);

        String decodedCredentialsString = new String(decodedCredentials);
        String[] usernameAndPassword = decodedCredentialsString.split(":");

        return userService.login(usernameAndPassword[0], usernameAndPassword[1]);
    }

    @RequestMapping("/tschuessli")
    public String tschuess() {
        return "Tschüssli!";
    }
}
