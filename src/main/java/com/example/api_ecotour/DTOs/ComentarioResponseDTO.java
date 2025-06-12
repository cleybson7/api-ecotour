package com.example.api_ecotour.DTOs;

import java.util.List;
import java.util.UUID;

public record ComentarioResponseDTO(
    UUID id,
    String nome,
    String email,
    Integer avaliacao,
    String comment,
    List<MidiaResponseDTO> midia
) {}
