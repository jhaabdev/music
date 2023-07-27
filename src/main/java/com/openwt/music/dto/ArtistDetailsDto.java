package com.openwt.music.dto;

import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;

@Value
public class ArtistDetailsDto implements Serializable {
    @NonNull
    final Long id;
    @NonNull
    final String username;
    @NonNull
    final String artistName;

    public ArtistDetailsDto(Long id, String username, String artistName) {
        this.id = id;
        this.username = username;
        this.artistName = artistName;
    }
}

