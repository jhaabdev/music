package com.openwt.music.dto;

import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;

@Value
public class AdminDetailsDto implements Serializable {
    @NonNull
    final Long id;
    @NonNull
    final String username;
    public AdminDetailsDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}

