package com.openwt.music.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ArtistCreateDto {
    @NonNull
    String username;
    @NonNull
    String artistName;
    @NonNull
    String password;
}

