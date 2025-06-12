package com.example.api_ecotour.DTOs;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record ComentarioRequestDTO(
    String nome,
    String email,
    Integer avaliacao,
    String comment,
    List<MultipartFile> midia
) {}
