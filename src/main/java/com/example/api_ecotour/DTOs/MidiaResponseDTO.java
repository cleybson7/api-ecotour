package com.example.api_ecotour.DTOs;

import java.util.UUID;

public record MidiaResponseDTO(
        UUID id,
        String nome,
        String type,
        String imageData) {
}
