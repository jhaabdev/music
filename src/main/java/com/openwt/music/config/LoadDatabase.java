package com.openwt.music.config;

import com.openwt.music.enums.UserRole;
import com.openwt.music.model.UserEntity;
import com.openwt.music.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private final String adminUsername;
    private final String adminPassword;
    private final String artistUsername;
    private final String artistPassword;

    public LoadDatabase(
            @Value("${admin.username}") final String adminUsername,
            @Value("${admin.password}") final String adminPassword,
            @Value("${artist.username}") final String artistUsername,
            @Value("${artist.password}") final String artistPassword) {
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.artistUsername = artistUsername;
        this.artistPassword = artistPassword;
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {

        UserEntity user0 = new UserEntity();
        user0.setUsername(this.adminUsername);
        user0.setPassword(passwordEncoder.encode(this.adminPassword));
        user0.setRole(UserRole.ADMIN.label);
        userRepository.save(user0);

        UserEntity user1 = new UserEntity();
        user1.setUsername(this.artistUsername);
        user1.setPassword(passwordEncoder.encode(this.artistPassword));
        user1.setArtistName("The Doors");
        user1.setRole(UserRole.ARTIST.label);
        userRepository.save(user1);

        String preloadingMessage = "Preloading {}";
        return args -> {
            log.info(preloadingMessage, user0);
            log.info(preloadingMessage, user1);
        };
    }
}
